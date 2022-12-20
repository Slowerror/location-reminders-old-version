package com.slowerror.locationreminders.presentation.ui.reminder_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentReminderListBinding
import com.slowerror.locationreminders.presentation.ui.reminder_list.adapter.ReminderAdapter
import timber.log.Timber

class ReminderListFragment : Fragment() {

    private lateinit var binding: FragmentReminderListBinding

    private val viewModel: ReminderListViewModel by viewModels()

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

        binding.addReminderFab.setOnClickListener {
            findNavController().navigate(R.id.action_remindersFragment_to_addReminderFragment)
        }
    }
}