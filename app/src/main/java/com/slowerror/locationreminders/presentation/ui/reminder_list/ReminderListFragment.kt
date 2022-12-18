package com.slowerror.locationreminders.presentation.ui.reminder_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.slowerror.locationreminders.databinding.FragmentReminderListBinding
import timber.log.Timber


class ReminderListFragment : Fragment() {

    private lateinit var binding: FragmentReminderListBinding

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

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        Timber.i("Авторизация успешна! Пользователь: ${user?.displayName} и его id ${user?.uid}")
        binding.signOutBtn.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
            findNavController().popBackStack()
            Timber.i("Выход успешен!")
        }
    }
}