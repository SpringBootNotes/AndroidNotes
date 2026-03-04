package com.android.login.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
public fun LoginScreenRoute() {
    LoginScreen()
}

@Composable
private fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    Button(
        onClick = { viewModel.login("test2@test.com", "Test123456!") },
        modifier = Modifier.padding(20.dp)
    ) {
        Text("Login")
    }

}