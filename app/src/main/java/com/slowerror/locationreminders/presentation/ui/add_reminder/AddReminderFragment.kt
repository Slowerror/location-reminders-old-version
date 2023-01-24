package com.slowerror.locationreminders.presentation.ui.add_reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentAddReminderBinding
import timber.log.Timber

class AddReminderFragment : Fragment() {

    private lateinit var binding: FragmentAddReminderBinding

    private val viewModel: AddReminderViewModel by activityViewModels()

    private var titleLocation: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReminderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { _, bundle ->
            val name = bundle.getString("bundleKey")
            val lat = bundle.getDouble("bundleLat")
            val lng = bundle.getDouble("bundleLng")
            Timber.i("GE_1_RES: $name")
            viewModel.getMarker(name, lat, lng)
        }*/


        viewModel.location.observe(viewLifecycleOwner) {
            binding.titleLocationTextView.text = it?.title
        }

        /*binding.titleLocationTextView.text = titleLocation*/

//        binding.titleLocationTextView.text = titleLocation

        binding.addLocationTextView.setOnClickListener {
            findNavController().navigate(R.id.action_addReminderFragment_to_selectLocationFragment)
        }

        binding.saveReminderFab.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
            findNavController().popBackStack(R.id.loginFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView is called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy is called")
    }
}