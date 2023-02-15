package com.slowerror.locationreminders.presentation.ui.select_location

import android.annotation.SuppressLint
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentSelectLocationBinding
import com.slowerror.locationreminders.presentation.ui.add_reminder.AddReminderViewModel
import com.slowerror.locationreminders.presentation.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectLocationFragment : Fragment() {

    private lateinit var binding: FragmentSelectLocationBinding
    private val viewModel: AddReminderViewModel by navGraphViewModels(R.id.addReminder_nav_graph)

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private val registerRequestPermissions: RegisterRequestPermissions by lazy {
        RegisterRequestPermissions(requireContext(), locationPermissionRequest, requireView())
    }

    private val gpsUtil: GpsUtil by lazy { GpsUtil(requireContext()) }
    private val userLocationUtil: UserLocationUtil by lazy {
        UserLocationUtil(
            requireContext(),
            viewLifecycleOwner.lifecycleScope
        )
    }

    private var locationFlow: Job? = null

    private lateinit var googleMap: GoogleMap
    private lateinit var lastLocation: Location

    private var titleMarker: String? = null
    private var latMarker: Double? = null
    private var lngMarker: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            registerRequestPermissions.processPermissions(permissions)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        observeViewModels()

        binding.myLocationFab.setOnClickListener { checkPermissionsAndGps() }

        binding.saveLocationFab.setOnClickListener { saveLocation() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                googleMap = mapFragment.awaitMap()
                setSavedMarker()
                googleMap.awaitMapLoad()

                formatMap()
                getLocationData()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        Timber.i("onResume is called")

        if (::googleMap.isInitialized) {
            Timber.i("onResume: googleMap.isInitialized")
            enableMyLocation()
            getLocationData()
        }

    }

    /* ============ Настройки карты ============ */
    private fun formatMap() {
        googleMap.apply {
            isBuildingsEnabled = false
            uiSettings.isMapToolbarEnabled = false
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isCompassEnabled = false

            enableMyLocation()

            setOnPoiClick(this)
            setOnLongClick(this)
            setOnClick(this)
        }
    }

    private fun setOnLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener {
            map.clear()

            val marker = map.addMarker {
                position(it)
                title(getString(R.string.default_title_marker))
            }

            Timber.i("Маркер в Лонг: ${marker?.title} ${marker?.position}")
            putDataLocation(marker)
        }
    }

    private fun setOnPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            map.clear()

            val marker = map.addMarker {
                position(poi.latLng)
                title(poi.name)
            }

            marker?.showInfoWindow()

            Timber.i("Маркер в ПОИ: ${marker?.title} ${marker?.position}")
            putDataLocation(marker)
        }
    }

    private fun setOnClick(map: GoogleMap) {
        map.setOnMapClickListener {
            titleMarker = null
            latMarker = null
            lngMarker = null

            map.clear()
        }
    }

    private fun setSavedMarker() {
        if (markerIsNotNull()) {
            val latLng = LatLng(latMarker as Double, lngMarker as Double)
            val name = titleMarker

            googleMap.addMarker {
                title(name)
                position(latLng)
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL))
        }
    }

    private fun markerIsNotNull(): Boolean =
        !(titleMarker == null || latMarker == null || lngMarker == null)

    private fun putDataLocation(marker: Marker?) {
        titleMarker = marker?.title
        latMarker = marker?.position?.latitude
        lngMarker = marker?.position?.longitude
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!googleMap.isMyLocationEnabled && requireContext().hasLocationPermissions()) {
            Timber.i("enableMyLocation was called")
            googleMap.isMyLocationEnabled = true
        }
    }
    /* ============ --------------- ============ */


    private fun observeViewModels() {
        with(viewModel) {
            nameMarker.observe(viewLifecycleOwner) { titleMarker = it }
            lat.observe(viewLifecycleOwner) { latMarker = it }
            lng.observe(viewLifecycleOwner) { lngMarker = it }
        }
    }

    private fun checkPermissionsAndGps() {
        Timber.i("invokeLocation() was called")
        when {
            !requireContext().hasLocationPermissions() -> {
                registerRequestPermissions.checkPermissions()
            }
            !requireContext().hasGpsEnabled() -> {
                gpsUtil.turnOnGps()
            }
            requireContext().hasGpsEnabled() -> {
                moveCameraToMyLocation()
            }
        }
    }

    private fun moveCameraToMyLocation() {
        if (::lastLocation.isInitialized) {
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lastLocation.latitude, lastLocation.longitude),
                    DEFAULT_ZOOM_LEVEL
                )
            )
        }
    }

    private fun saveLocation() {
        if (markerIsNotNull()) {
            viewModel.saveMarker(
                title = titleMarker,
                lat = latMarker,
                lng = lngMarker
            )
            findNavController().popBackStack()
        } else {
            Snackbar.make(requireView(), "Установите маркер", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun getLocationData() {
        if (!requireContext().hasLocationPermissions() || !requireContext().hasGpsEnabled()) {
            return
        }
        Timber.i("getLocationData: permissions and gps success")
        if(locationFlow?.isActive != true) {
            Timber.i("getLocationData: locationFlow?.isActive != true")
            locationFlow = userLocationUtil.getLocationUpdates()
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .catch { e ->
                    e.printStackTrace()
                    Timber.d("exception: $e")
                }
                .onEach { location ->
                    lastLocation = location
                    Timber.i("lastLocation: $lastLocation")
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
        
    }
}