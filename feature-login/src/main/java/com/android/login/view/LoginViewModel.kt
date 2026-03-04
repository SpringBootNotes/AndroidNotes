package com.android.login.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dispatchers.IoDispatcher
import com.android.login.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    internal fun login(email: String, password: String) = viewModelScope.launch(dispatcher) {
       val result = loginUseCase(email, password)
        Log.d("testing123", result.toString())
    }
}
