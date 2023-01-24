package com.slowerror.locationreminders.presentation.ui.select_location

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentSelectLocationBinding
import com.slowerror.locationreminders.presentation.model.Location
import com.slowerror.locationreminders.presentation.ui.add_reminder.AddReminderViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectLocationFragment : Fragment() {

    private lateinit var binding: FragmentSelectLocationBinding

    private val viewModel: AddReminderViewModel by activityViewModels()

    private var location: Location? = null

    /*private var isSetMarker = false*/

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

        viewModel.location.observe(viewLifecycleOwner) {
            location = it
        }

        Timber.i("location: ${location?.title}")
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                val googleMap = mapFragment.awaitMap()

                if (location != null) {
                    val latLng = LatLng(location?.lat as Double, location?.lng  as Double)
                    googleMap.addMarker {
                        position(latLng)
                        title(location!!.title)

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
        if (location != null) {
            /* setFragmentResult(
                 "requestKey",
                 bundleOf(
                     "bundleKey" to point?.title,
                     "bundleLat" to point?.position?.latitude,
                     "bundleLng" to point?.position?.longitude
                 )
             )*/
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

//            isSetMarker = true

            Timber.i("Маркер в Лонг: ${marker?.title} ${marker?.position}")

            viewModel.getMarker(
                Location(
                    marker?.title,
                    marker?.position?.latitude,
                    marker?.position?.longitude
                )
            )

            Timber.i("Маркер в Лонг: ${marker?.title} ${marker?.position}")
        }
    }

    private fun setOnPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            map.clear()

            val marker = map.addMarker {
                position(poi.latLng)
                title(poi.name)
            }

//            isSetMarker = true

            Timber.i("Маркер в ПОИ: ${marker?.title} ${marker?.position}")
            viewModel.getMarker(
                Location(
                    marker?.title,
                    marker?.position?.latitude,
                    marker?.position?.longitude
                )
            )
            Timber.i("Маркер в ПОИ: ${marker?.title} ${marker?.position}")
        }
    }

    private fun setOnClick(map: GoogleMap) {
        map.setOnMapClickListener {
            location = null
            map.clear()
        }
    }

}