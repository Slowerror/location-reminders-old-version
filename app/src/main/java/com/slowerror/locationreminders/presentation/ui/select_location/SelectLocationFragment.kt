package com.slowerror.locationreminders.presentation.ui.select_location

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Location
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
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
import com.slowerror.locationreminders.presentation.utils.DefaultUserLocation
import com.slowerror.locationreminders.presentation.utils.hasLocationPermission
import com.slowerror.locationreminders.presentation.utils.hasGpsEnabled
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectLocationFragment : Fragment() {

    private lateinit var binding: FragmentSelectLocationBinding
    private val viewModel: AddReminderViewModel by navGraphViewModels(R.id.addReminder_nav_graph)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var userLocation: DefaultUserLocation

    private lateinit var lastLocation: LatLng

    private lateinit var googleMap: GoogleMap

    private var titleMarker: String? = null
    private var latMarker: Double? = null
    private var lngMarker: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectLocationBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        userLocation =
            DefaultUserLocation(requireContext(), requireActivity(), fusedLocationClient)

        viewModel.nameMarker.observe(viewLifecycleOwner) {
            titleMarker = it
        }

        viewModel.lat.observe(viewLifecycleOwner) {
            latMarker = it
        }

        viewModel.lng.observe(viewLifecycleOwner) {
            lngMarker = it
        }

        binding.myLocationFab.setOnClickListener {
            getCurrentLocation()
        }

        binding.saveLocationFab.setOnClickListener {
            saveLocation()
        }

        viewLifecycleOwner.lifecycleScope.launch {

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            googleMap = mapFragment.awaitMap()
            googleMap.awaitMapLoad()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {

                if (isNullableMarker()) {
                    val latLng = LatLng(latMarker as Double, lngMarker as Double)
                    val name = titleMarker

                    googleMap.addMarker {
                        title(name)
                        position(latLng)
                    }
                }

                setOnPoiClick(googleMap)
                setOnLongClick(googleMap)
                setOnClick(googleMap)
            }
        }
    }

    private fun saveLocation() {
        if (isNullableMarker()) {
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

    private fun putDataLocation(marker: Marker?) {
        titleMarker = marker?.title
        latMarker = marker?.position?.latitude
        lngMarker = marker?.position?.longitude
    }

    private fun isNullableMarker(): Boolean =
        !(titleMarker == null || latMarker == null || lngMarker == null)

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (requireContext().hasLocationPermission()) {
            if (requireContext().hasGpsEnabled()) {

                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = false

                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
//                        lastLocation = location
                        val lastLatLng = LatLng(location.latitude, location.longitude)

                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                lastLatLng,
                                DEFAULT_ZOOM_LEVEL
                            )
                        )
                    }
                }
            } else {
                userLocation.locationSettings()
            }

        } else {
            requireActivity().requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 101
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, DEFAULT_INTERVAL_MILLIS
        ).build()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation as Location
            lastLocation = LatLng(location.latitude, location.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    /*private fun locationEnabled(map: GoogleMap) {
        if (requireContext().hasLocationPermission()) {
            map.isMyLocationEnabled = true

            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled) {
                locationSettings()
            }

            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                if (location != null) {
                    lastLocation = location
                    val lastLatLng = LatLng(location.latitude, location.longitude)

                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            lastLatLng,
                            DEFAULT_ZOOM_LEVEL
                        )
                    )
                } *//*else {
                    val moscow = LatLng(55.756, 37.617)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(moscow, DEFAULT_ZOOM_LEVEL))
                }*//*
            }
        }
    }*/

    private fun locationSettings() {

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            DEFAULT_INTERVAL_MILLIS
        )

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest.build())

        val client = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                } catch (_: IntentSender.SendIntentException) {

                }
            }
        }
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15f
        private const val DEFAULT_INTERVAL_MILLIS = 10000L
        private const val REQUEST_CHECK_SETTINGS = 100

    }


}