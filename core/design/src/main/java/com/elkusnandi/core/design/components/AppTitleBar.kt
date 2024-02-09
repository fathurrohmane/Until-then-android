package com.elkusnandi.core.design.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.elkusnandi.core.design.theme.UntilThenTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTitleBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = "Until then") },
        colors = topAppBarColors(containerColor = Color.Transparent)
    )
}

@Preview(showSystemUi = true)
@Composable
private fun AppTitleBarPreview() {
    UntilThenTheme {
        AppTitleBar()
    }
}