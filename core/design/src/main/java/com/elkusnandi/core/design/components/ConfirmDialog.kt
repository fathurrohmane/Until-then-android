package com.elkusnandi.core.design.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.elkusnandi.core.design.R

@Composable
fun ConfirmDialog(
    dialogTitle: String,
    dialogText: String,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    MaterialTheme {
        ConfirmDialog(
            dialogText = "This is example confirm dialog",
            dialogTitle = "Example Dialog",
            onConfirmation = {},
            onDismissRequest = {})
    }
}

@Preview
@Composable
private fun ConfirmDialogLongTextPreview() {
    MaterialTheme {
        ConfirmDialog(
            dialogText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            dialogTitle = "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet",
            onConfirmation = {},
            onDismissRequest = {})
    }
}