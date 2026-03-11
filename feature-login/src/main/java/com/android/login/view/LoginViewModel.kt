package com.android.login.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dispatchers.IoDispatcher
import com.android.login.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvents>()
    val events = _events.receiveAsFlow()

    internal fun onLoginButtonClicked(email: String, password: String) = viewModelScope.launch(dispatcher) {
        _state.update { it.copy(isLoading = true) }

        try {
            val result = loginUseCase(email, password)

            when (result) {
                is LoginUseCase.LoginResult.Success -> _events.send(LoginEvents.NavigateToNotes)

                is LoginUseCase.LoginResult.InvalidCredentialsError -> showInvalidCredentialsErrorDialog()

                is LoginUseCase.LoginResult.NetworkError -> showNetworkErrorDialog()

                is LoginUseCase.LoginResult.GenericError -> showGenericErrorDialog()
            }
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

    internal fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email) }
    }

    internal fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password) }
    }

    internal fun onRememberMeClicked() {
        _state.update { it.copy(rememberMeIsChecked = !it.rememberMeIsChecked) }
    }

    internal fun onSignUpClicked() {
        viewModelScope.launch {
            _events.send(LoginEvents.NavigateToSignUp)
        }
    }

    private fun showInvalidCredentialsErrorDialog() {
        _state.update { it.copy(showInvalidCredentialsErrorDialog = true) }
    }

    internal fun dismissInvalidCredentialsErrorDialog() {
        _state.update { it.copy(showInvalidCredentialsErrorDialog = false) }
    }

    private fun showNetworkErrorDialog() {
        _state.update { it.copy(showNetworkErrorDialog = true) }
    }

    internal fun dismissNetworkErrorDialog() {
        _state.update { it.copy(showNetworkErrorDialog = false) }

    }

    private fun showGenericErrorDialog() {
        _state.update { it.copy(showGenericErrorDialog = true) }
    }

    internal fun dismissGenericErrorDialog() {
        _state.update {
            it.copy(showGenericErrorDialog = false)
        }
    }
}

internal data class LoginState(
    val isLoading: Boolean = false,
    val email: String? = null,
    val password: String? = null,
    val rememberMeIsChecked: Boolean = false,
    val showInvalidCredentialsErrorDialog: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
    val showGenericErrorDialog: Boolean = false
) {
    private val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    val emailIsValid: Boolean = email?.let { emailRegex.matches(email) } ?: false
    val loginButtonEnabled: Boolean =
        !email.isNullOrBlank() && !password.isNullOrBlank() && emailIsValid
}

internal sealed class LoginEvents {
    data object NavigateToNotes : LoginEvents()
    data object NavigateToSignUp : LoginEvents()
}