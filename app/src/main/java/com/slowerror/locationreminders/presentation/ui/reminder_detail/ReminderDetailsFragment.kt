package com.slowerror.locationreminders.presentation.ui.reminder_detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.databinding.FragmentReminderDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

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
        setupMenu()

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

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.removeItem -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.removeReminder()
                            Timber.i("viewModel.removeReminder() was completed")
                            findNavController().popBackStack()
                        }

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}