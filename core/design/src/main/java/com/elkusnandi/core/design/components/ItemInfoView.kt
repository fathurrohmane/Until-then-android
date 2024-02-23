package com.elkusnandi.core.design.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun ItemInfoView(
    leftSlot: @Composable () -> Unit,
    middleSlot: @Composable () -> Unit,
    rightSlot: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leftSlot()
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1F)) {
            middleSlot()
        }
        Spacer(modifier = Modifier.width(8.dp))
        rightSlot()
    }

}

@Composable
fun BasicItemInfoView(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Info,
    action: @Composable () -> Unit
) {
    ItemInfoView(leftSlot = {
        Image(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)

        )
    }, middleSlot = {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }, rightSlot = {
        action()

    }, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
private fun BasicItemInfoViewPreview() {
    MaterialTheme {
        BasicItemInfoView(
            text = "Something went wrong",
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            TextButton(onClick = { }) {
                Text(text = "Retry")
            }
        }
    }
}