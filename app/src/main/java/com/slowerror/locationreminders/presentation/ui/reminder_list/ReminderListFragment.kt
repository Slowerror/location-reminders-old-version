package com.slowerror.locationreminders.presentation.ui.reminder_list

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentReminderListBinding
import com.slowerror.locationreminders.presentation.MainActivity
import com.slowerror.locationreminders.presentation.ui.reminder_list.adapter.ReminderAdapter
import com.slowerror.locationreminders.presentation.utils.RegisterRequestPermissions
import timber.log.Timber

class ReminderListFragment : Fragment() {

    private lateinit var binding: FragmentReminderListBinding

    private val viewModel: ReminderListViewModel by viewModels()

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private val registerRequestPermissions: RegisterRequestPermissions by lazy {
        RegisterRequestPermissions(requireContext(),  locationPermissionRequest, requireView())
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

        val adapter = ReminderAdapter()
        val linearLayout = LinearLayoutManager(requireContext())

        binding.reminderRw.layoutManager = linearLayout
        binding.reminderRw.adapter = adapter

        binding.reminderRw.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.reminders.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("ReminderListFragment onViewCreated")
        registerRequestPermissions.checkPermissions()
        setupMenu()

        binding.addReminderFab.setOnClickListener {
            findNavController().navigate(R.id.action_remindersFragment_to_addReminderFragment)
        }

    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.logoutItem -> {
                        AuthUI.getInstance().signOut(requireContext()).addOnSuccessListener {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            requireActivity().finish()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}