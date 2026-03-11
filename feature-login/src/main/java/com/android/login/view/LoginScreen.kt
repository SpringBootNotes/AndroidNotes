package com.android.login.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.ui.R


@Composable
public fun LoginScreenRoute() {
    LoginScreen()
}

@Composable
private fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LoginScreenContent(state = state.value)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvents.NavigateToNotes -> {
                    // Navigate to notes screen
                }

                is LoginEvents.NavigateToSignUp -> {
                    // Navigate to sign up screen
                }
            }
        }
    }
}

@Composable
private fun LoginScreenContent(state: LoginState) {

}

@Preview
@Composable
private fun LoginScreenContentPreview() {
    LoginScreenContent(state = LoginState())
}
