package com.elkusnandi.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elkusnandi.core.common.addZeroPrefix
import com.elkusnandi.core.common.getDuration
import com.elkusnandi.core.design.R
import com.elkusnandi.core.design.theme.UntilThenTheme
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

@Composable
fun CountDownItem(
    title: String,
    duration: HashMap<TimeUnit, Number>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clickable {
                onClick()
            }, shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
            ) {
                SingleTimeItem(
                    duration[TimeUnit.DAYS].toString(),
                    stringResource(id = R.string.days)
                )
                TimeDivider()
                SingleTimeItem(
                    duration[TimeUnit.HOURS].toString(),
                    stringResource(id = R.string.hours)
                )
                TimeDivider()
                SingleTimeItem(
                    duration[TimeUnit.MINUTES].toString(),
                    stringResource(id = R.string.minutes)
                )
                TimeDivider()
                SingleTimeItem(
                    duration[TimeUnit.SECONDS].toString(),
                    stringResource(id = R.string.seconds)
                )
            }
        }
    }
}

@Composable
private fun TimeDivider() {
    Text(
        text = ":",
        style = MaterialTheme.typography.displaySmall,
    )
}

@Composable
private fun SingleTimeItem(time: String, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = time.addZeroPrefix(),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Text(text = title, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleTimeItemPreview() {
    SingleTimeItem("10", "Days")
}

@Preview
@Composable
private fun CountDownItemPreview() {
    val title = "Birthday"
    val time = ZonedDateTime.now().plusSeconds(36500L).getDuration()
    UntilThenTheme {
        CountDownItem(title, time) {}
    }
}