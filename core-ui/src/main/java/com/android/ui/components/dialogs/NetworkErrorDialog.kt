package com.android.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.ui.R

@Composable
fun NetworkErrorDialog(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.network_error_button_text))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.network_error_title))
        },
        text = {
            Text(text = stringResource(id = R.string.network_error_message))
        }
    )
}

@Preview
@Composable
private fun NetworkErrorDialogPreview() {
    NetworkErrorDialog(onDismissRequest = {})
}