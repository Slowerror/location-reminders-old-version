package com.slowerror.locationreminders.presentation.ui.add_reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentAddReminderBinding

class AddReminderFragment : Fragment() {

    private lateinit var binding: FragmentAddReminderBinding

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

        binding.saveReminderFab.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
            findNavController().popBackStack(R.id.loginFragment, false)
        }
    }
}