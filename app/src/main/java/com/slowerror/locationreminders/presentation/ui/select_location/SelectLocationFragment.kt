package com.slowerror.locationreminders.presentation.ui.select_location

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
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
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectLocationFragment : Fragment() {

    private lateinit var binding: FragmentSelectLocationBinding

    private val viewModel: AddReminderViewModel by navGraphViewModels(R.id.addReminder_nav_graph)

    private var title: String? = null
    private var lat: Double? = null
    private var lng: Double? = null

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

        viewModel.nameMarker.observe(viewLifecycleOwner) {
            title = it
        }
        viewModel.lat.observe(viewLifecycleOwner) {
            lat = it
        }
        viewModel.lng.observe(viewLifecycleOwner) {
            lng = it
        }

        viewLifecycleOwner.lifecycleScope.launch {

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {

                val googleMap = mapFragment.awaitMap()

                if ((title != null && lat != null && lng != null)) {
                    val latLng = LatLng(lat as Double, lng as Double)
                    val tt = title
                    googleMap.addMarker {
                        title(tt)
                        position(latLng)
                    }
                }

                googleMap.awaitMapLoad()

                setMapSettings(googleMap)
            }
        }

        binding.saveLocationFab.setOnClickListener {
            saveLocation()
        }
    }

    private fun saveLocation() {
        if ((title != null && lat != null && lng != null)) {
            viewModel.getMarker(title, lat, lng)
            findNavController().popBackStack()
        } else {
            Snackbar.make(requireView(), "Установите маркер", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun setMapSettings(map: GoogleMap) {
        val moscow = LatLng(55.756, 37.617)
        val zoom = 10f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, zoom))

        setOnPoiClick(map)
        setOnLongClick(map)
        setOnClick(map)
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

            Timber.i("Маркер в ПОИ: ${marker?.title} ${marker?.position}")
            marker?.showInfoWindow()
            putDataLocation(marker)
        }
    }

    private fun setOnClick(map: GoogleMap) {
        map.setOnMapClickListener {

            title = null
            lat = null
            lng = null

            /*viewModel.getMarker(title = null,
            lat = null,
            lng = null)*/

            map.clear()
        }
    }

    private fun putDataLocation(marker: Marker?) {
        /*viewModel.getMarker(
            marker?.title,
            marker?.position?.latitude,
            marker?.position?.longitude
        )*/
        title = marker?.title
        lat = marker?.position?.latitude
        lng = marker?.position?.longitude

        Timber.i("Маркер в putDataLocation: $title $lat")
    }

}