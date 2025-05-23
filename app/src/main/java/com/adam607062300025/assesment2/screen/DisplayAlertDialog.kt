package com.adam607062300025.assesment2.screen

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.adam607062300025.assesment2.R
import com.adam607062300025.assesment2.ui.theme.Assesment2Theme

@Composable
fun DisplayAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        text = {
            Text(
                text = stringResource(R.string.pesan_hapus)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {onConfirmation()}
            ) {
                Text(
                    text = stringResource(R.string.tombol_hapus)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {onDismissRequest()}
            ) {
                Text(
                    text = stringResource(R.string.tombol_batal)
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DialogPreview() {
    Assesment2Theme {
        DisplayAlertDialog(
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}