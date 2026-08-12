package com.learn.android.trevea.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.learn.android.trevea.R


@Composable
fun StatsItem (
    title: String = "Test",
    value: String = "4"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium
        )

    }
}

@Composable
fun UserStats(
    longestStreak: String,
    totalQuestions: String,
    totalCorrectAns: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .border(width = Dp.Hairline, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp)),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatsItem(
            title = stringResource(R.string.longest_string),
            value = longestStreak
        )

        StatsItem(
            title = stringResource(R.string.total_questions),
            value = totalQuestions
        )

        StatsItem(
            title = stringResource(R.string.total_correct_ans),
            value = totalCorrectAns
        )
    }
}