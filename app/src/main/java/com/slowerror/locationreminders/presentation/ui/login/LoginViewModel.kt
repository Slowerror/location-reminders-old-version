package com.slowerror.locationreminders.presentation.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.slowerror.locationreminders.domain.usecase.login.LoginUseCase

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    /*private val loginUseCase = LoginUseCase()*/

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun onLogin(launcher: ActivityResultLauncher<Intent>) {
        loginUseCase.invoke(launcher)
    }
}


class LoginViewModelFactory(private val loginUseCase: LoginUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginUseCase) as T
        } else {
            throw IllegalArgumentException("Неизвестный ViewModel class")
        }

    }
}