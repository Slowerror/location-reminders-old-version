package com.slowerror.locationreminders.presentation.ui.add_reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
}