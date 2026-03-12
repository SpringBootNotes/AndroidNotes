package com.android.login.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.ui.R
import com.android.ui.components.LoadingSpinnerOverlay
import com.android.ui.components.dialogs.GenericErrorDialog
import com.android.ui.components.dialogs.NetworkErrorDialog


@Composable
public fun LoginScreenRoute() {
    LoginScreen()
}

@Composable
private fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()


    LoginScreenContent(
        state = state.value,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onRememberMeChecked = viewModel::onRememberMeClicked,
        onLoginButtonClicked = viewModel::onLoginButtonClicked
    )

    LoginDialogs(
        showInvalidCredentialsErrorDialog = state.value.showInvalidCredentialsErrorDialog,
        showNetworkErrorDialog = state.value.showNetworkErrorDialog,
        showGenericErrorDialog = state.value.showGenericErrorDialog,
        dismissInvalidCredentialsDialog = viewModel::dismissInvalidCredentialsErrorDialog,
        dismissNetworkErrorDialog = viewModel::dismissNetworkErrorDialog,
        dismissGenericErrorDialog = viewModel::dismissGenericErrorDialog
    )

    if (state.value.isLoading) {
        LoadingSpinnerOverlay()
    }

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
private fun LoginScreenContent(
    state: LoginState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRememberMeChecked: (Boolean) -> Unit,
    onLoginButtonClicked: () -> Unit
) {
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
            PasswordTextField(password = state.password, onPasswordChanged = onPasswordChanged)
            Spacer(modifier = Modifier.height(16.dp))
            RememberMeToggle(
                isRememberMeChecked = state.rememberMeIsChecked,
                onCheckedChange = onRememberMeChecked,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            LoginButton(
                enabled = state.loginButtonEnabled,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onLoginButtonClicked
            )
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
            trailingIcon = {
                if (email.isNotEmpty()) {
                    ClearFieldIconButton(onClick = { onEmailChanged("") })
                }
            },
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
    Text(
        text = stringResource(id = R.string.email_field_placeholder),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )
}

@Composable
private fun PasswordTextField(
    password: String,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        label = { PasswordTextFieldLabel() },
        placeholder = { PasswordTextFieldPlaceholder() },
        trailingIcon = {
            PasswordVisibilityIconButton(
                passwordVisible = passwordVisible,
                onClick = { passwordVisible = !passwordVisible }
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        visualTransformation = if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )
}

@Composable
private fun PasswordTextFieldLabel() {
    Text(text = stringResource(id = R.string.password_field_label))
}

@Composable
private fun PasswordTextFieldPlaceholder() {
    Text(
        text = stringResource(id = R.string.password_field_placeholder),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )
}

@Composable
private fun ClearFieldIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(id = R.string.clear_email_content_description)
        )
    }
}

@Composable
private fun PasswordVisibilityIconButton(
    passwordVisible: Boolean,
    onClick: () -> Unit
) {
    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val description = if (passwordVisible) {
        stringResource(id = R.string.hide_password_content_description)
    } else {
        stringResource(id = R.string.show_password_content_description)
    }

    IconButton(onClick = onClick) {
        Icon(imageVector = image, contentDescription = description)
    }
}

@Composable
private fun RememberMeToggle(
    isRememberMeChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable { onCheckedChange(!isRememberMeChecked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isRememberMeChecked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = R.string.remember_me_toggle_label))
    }
}

@Composable
private fun LoginButton(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(4.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Text(
            text = stringResource(id = R.string.login_button_text),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun LoginDialogs(
    showInvalidCredentialsErrorDialog: Boolean,
    showNetworkErrorDialog: Boolean,
    showGenericErrorDialog: Boolean,
    dismissInvalidCredentialsDialog: () -> Unit,
    dismissNetworkErrorDialog: () -> Unit,
    dismissGenericErrorDialog: () -> Unit
) {
    if(showInvalidCredentialsErrorDialog) {
        InvalidCredentialsErrorDialog(onDismissRequest = dismissInvalidCredentialsDialog)
    }
    if(showNetworkErrorDialog) {
        NetworkErrorDialog(onDismissRequest = dismissNetworkErrorDialog)
    }
    if(showGenericErrorDialog) {
        GenericErrorDialog(onDismissRequest = dismissGenericErrorDialog)
    }
}

@Preview
@Composable
private fun LoginScreenContentPreview() {
    LoginScreenContent(
        state = LoginState(),
        onEmailChanged = {},
        onPasswordChanged = {},
        onRememberMeChecked = {},
        onLoginButtonClicked = {}
    )
}
