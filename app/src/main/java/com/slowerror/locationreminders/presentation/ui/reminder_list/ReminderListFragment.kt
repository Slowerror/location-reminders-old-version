package com.slowerror.locationreminders.presentation.ui.reminder_list

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentReminderListBinding
import com.slowerror.locationreminders.presentation.MainActivity
import com.slowerror.locationreminders.presentation.ui.reminder_list.adapter.ReminderAdapter
import com.slowerror.locationreminders.presentation.utils.hasLocationPermission
import timber.log.Timber

class ReminderListFragment : Fragment() {

    private lateinit var binding: FragmentReminderListBinding

    private val viewModel: ReminderListViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                onGotPermissionsResultForLocation(permissions)
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {

                requireActivity().requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 101
                )

                /*val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Диалог")
                    .setMessage("Для приложения необходимо знать ваше точное местоположение")
                    .setNegativeButton(DialogInterface.BUTTON_NEGATIVE) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setOnDismissListener {
                        it.dismiss()
                    }
                    *//*.setPositiveButton(DialogInterface.BUTTON_POSITIVE) {_, _ ->
                        requireActivity().requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ), 101
                        )
                    }*//*
                    .create()

                builder.show()*/

            }
            else -> {
                Snackbar.make(requireView(), "Пермишены запрещены!", Snackbar.LENGTH_SHORT).show()
            }
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("ReminderListFragment onViewCreated")
        checkPermissions()
        setupMenu()

        binding.addReminderFab.setOnClickListener {
            findNavController().navigate(R.id.action_remindersFragment_to_addReminderFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("ReminderListFragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("ReminderListFragment onDestroy")
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkPermissions() {
        if (!requireContext().hasLocationPermission()) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private fun onGotPermissionsResultForLocation(grantResult: Map<String, Boolean>) {
        if (grantResult.entries.all { it.value }) {
            Toast.makeText(requireContext(), "Пермишены вызваны", Toast.LENGTH_SHORT).show()
        }
    }

}