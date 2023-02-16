package com.slowerror.locationreminders.presentation.ui.add_reminder

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentAddReminderBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddReminderFragment : Fragment() {

    private lateinit var binding: FragmentAddReminderBinding

    private val viewModel: AddReminderViewModel by navGraphViewModels(R.id.addReminder_nav_graph){
        defaultViewModelProviderFactory
    }

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

        viewModel.nameMarker.observe(viewLifecycleOwner) {
            binding.titleLocationTextView.text = it
        }


        binding.addLocationTextView.setOnClickListener {
            findNavController().navigate(R.id.action_addReminderFragment_to_selectLocationFragment)
        }

        binding.saveReminderFab.setOnClickListener {
            val title = binding.titleReminderEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            viewModel.saveReminder(title, description)
            findNavController().popBackStack()
        }
    }

    /*private fun observeViewModels() {
        with(viewModel) {
            nameMarker.observe(viewLifecycleOwner) { binding.titleLocationTextView.text = it }
            nameMarker.observe(viewLifecycleOwner) { binding.titleLocationTextView.text = it }
            nameMarker.observe(viewLifecycleOwner) { binding.titleLocationTextView.text = it }
            nameMarker.observe(viewLifecycleOwner) { binding.titleLocationTextView.text = it }
        }
    }*/

    /*private fun setReminder() {


        viewModel
    }*/

}