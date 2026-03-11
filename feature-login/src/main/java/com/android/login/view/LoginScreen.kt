package com.android.login.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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

    LoginScreenContent(state = state.value, onEmailChanged = viewModel::onEmailChanged)

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
private fun LoginScreenContent(state: LoginState, onEmailChanged: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            EmailTextField(
                email = state.email,
                onEmailChanged = onEmailChanged,
                isError = state.showInvalidEmailError
            )
            Spacer(modifier = Modifier.height(16.dp))
            //PasswordTextField()
            Spacer(modifier = Modifier.height(16.dp))
            //RememberMeToggle()
            Spacer(modifier = Modifier.height(32.dp))
            //LoginButton()
            Spacer(modifier = Modifier.height(32.dp))
            //SignUpLink()
        }
    }
}

@Composable
private fun EmailTextField(
    email: String,
    onEmailChanged: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Column {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChanged,
            label = { EmailTextFieldLabel() },
            placeholder = { EmailTextFieldPlaceholder() },
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorTextColor = MaterialTheme.colorScheme.onSurface,
                errorCursorColor = MaterialTheme.colorScheme.error
            )
        )
        if (isError) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(id = R.string.invalid_email_error),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun EmailTextFieldLabel() {
    Text(text = stringResource(id = R.string.email_field_label))
}

@Composable
private fun EmailTextFieldPlaceholder() {
    Text(text = stringResource(id = R.string.email_field_placeholder), color = Color.Gray)
}


@Preview
@Composable
private fun LoginScreenContentPreview() {
    LoginScreenContent(state = LoginState(), onEmailChanged = {})
}
