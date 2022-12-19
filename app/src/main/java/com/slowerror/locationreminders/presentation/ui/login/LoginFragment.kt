package com.slowerror.locationreminders.presentation.ui.login

import android.app.Activity.RESULT_OK
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat.Builder
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.slowerror.locationreminders.R
import com.slowerror.locationreminders.databinding.FragmentLoginBinding
import timber.log.Timber

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
//        this.onSignInResult(res)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onObserveAuthState()
    }

    private fun onObserveAuthState() {
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            Timber.i("Текущий стате: $state")
            when (state) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {

                    findNavController().navigate(R.id.remindersFragment)
                }
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    Timber.i("Пользователь не авторизован!")
                    binding.loginButton.setOnClickListener {
                        launchSignInFlow()
                    }
                }
                else -> {
                    Timber.i("Произошла ошибка!")

                }
            }
        }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            /*val user = FirebaseAuth.getInstance().currentUser
            Timber.i("Авторизация успешна! Пользователь: ${user?.displayName} и его id ${user?.uid}")
            findNavController().navigate(R.id.remindersFragment)*/
        } else {
            Timber.i("Не удалось авторизироваться. Ошибка: ${response?.error?.errorCode}")
        }
    }
}