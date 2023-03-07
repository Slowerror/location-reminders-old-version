package com.slowerror.locationreminders.presentation.ui.reminder_list

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.common.Resource
import com.slowerror.locationreminders.databinding.FragmentReminderListBinding
import com.slowerror.locationreminders.presentation.MainActivity
import com.slowerror.locationreminders.presentation.ui.reminder_list.adapter.ReminderAdapter
import com.slowerror.locationreminders.presentation.ui.reminder_list.adapter.ReminderClickListener
import com.slowerror.locationreminders.presentation.utils.RegisterRequestPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class ReminderListFragment : Fragment() {

    private lateinit var binding: FragmentReminderListBinding

    private val viewModel: ReminderListViewModel by viewModels()
    private lateinit var reminderAdapter: ReminderAdapter

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private val registerRequestPermissions: RegisterRequestPermissions by lazy {
        RegisterRequestPermissions(requireContext(), locationPermissionRequest, requireView())
    }

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
        binding = FragmentReminderListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("ReminderListFragment onViewCreated")
        disableBackButton()
        initAdapter()
        registerRequestPermissions.checkPermissions()

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { result ->
                Timber.i("UiState was called")
                when (result) {
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Упс, не удалось загрузить данные ${result.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.noDataTextView.visibility =
                            if (result.data.isNullOrEmpty())
                                View.VISIBLE
                            else
                                View.GONE
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Timber.i("Resource.Success: ${result.data}")
                        binding.noDataTextView.visibility =
                            if (result.data.isNullOrEmpty())
                                View.VISIBLE
                            else
                                View.GONE
                        reminderAdapter.submitList(result.data)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        setupMenu()

        binding.addReminderFab.setOnClickListener {
            findNavController().navigate(R.id.action_remindersFragment_to_addReminderFragment)
        }

    }

    private fun disableBackButton() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.logoutItem -> {
                        logout()
                        true
                    }
                    R.id.removeReminders -> {
                        viewModel.removeReminders()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initAdapter() {
        reminderAdapter = ReminderAdapter(ReminderClickListener { id ->
            findNavController().navigate(
                ReminderListFragmentDirections.actionRemindersFragmentToReminderDetailsFragment(
                    id
                )
            )
        })

        reminderAdapter.apply {
            binding.reminderRw.adapter = reminderAdapter
            binding.reminderRw.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.reminderRw.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun logout() {
        AuthUI.getInstance().signOut(requireContext())
            .addOnCompleteListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                requireActivity().finish()
            }
    }

}