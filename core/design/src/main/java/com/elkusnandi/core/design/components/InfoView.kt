package com.elkusnandi.core.design.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun InfoView(
    topSlot: @Composable () -> Unit,
    middleSlot: @Composable () -> Unit,
    bottomSlot: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        topSlot()
        Spacer(modifier = Modifier.height(16.dp))
        middleSlot()
        Spacer(modifier = Modifier.height(16.dp))
        bottomSlot()
    }

}

@Composable
fun BasicInfoView(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Info,
    action: @Composable () -> Unit
) {
    InfoView(topSlot = {
        Image(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier.size(56.dp)
        )
    }, middleSlot = {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }, bottomSlot = {
        action()

    }, modifier = modifier)
}

@Preview(showSystemUi = true)
@Composable
private fun BasicInfoViewPreview() {
    MaterialTheme {
        BasicInfoView(text = "Something went wrong") {
            Button(onClick = { }) {
                Text(text = "Retry")
            }
        }
    }
}