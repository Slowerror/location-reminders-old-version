package com.slowerror.locationreminders.presentation.ui.reminder_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.databinding.FragmentReminderDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ReminderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentReminderDetailsBinding
    private val viewModel: ReminderDetailsViewModel by viewModels()

    private val reminderId: Long by lazy {
        ReminderDetailsFragmentArgs.fromBundle(requireArguments()).idReminder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReminderById(reminderId)

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        binding.pbReminder.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Упс, не удалось загрузить данные ${result.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Loading -> {
                        binding.pbReminder.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        with(binding) {
                            pbReminder.visibility = View.GONE
                            contentTitleReminder.text = result.data?.title ?: ""
                            contentLocationReminder.text = result.data?.namePoi ?: ""
                            contentDescription.text = result.data?.description ?: ""
                        }


                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }
}