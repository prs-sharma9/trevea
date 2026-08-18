package com.learn.android.trevea.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.learn.android.trevea.R

@Composable
fun IconActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    description: String,
    onAction: () -> Unit = {}
) {
    val tag = "Trevea: IconActionButton"
    IconButton(

        onClick = {
            onAction()
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.account_icon_description),
            tint = colorResource(R.color.secondary),
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        )
    }
}

@Composable
fun TextActionButton(
    modifier: Modifier = Modifier,
    label: String,
    onAction: () -> Unit
) {
    val tag = "Trevea: TextActionButton"
    var scaleVal by remember { mutableStateOf(1f) }
    Row (
        modifier = modifier
            .fillMaxWidth()
            .scale(scaleVal)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
        modifier = modifier,
            onClick = {
                onAction()
            },
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 15.dp,
                pressedElevation = 2.dp
            ),
            colors = ButtonDefaults.textButtonColors(
                containerColor = colorResource(R.color.secondary),
                contentColor = colorResource(R.color.accent)
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(20.dp),
                text = label,
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}