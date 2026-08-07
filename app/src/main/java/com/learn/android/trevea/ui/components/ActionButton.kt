package com.learn.android.trevea.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.learn.android.trevea.R

@Composable
fun TopBarActionIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.AccountCircle,
    onAction: () -> Unit = {}
) {
    IconButton(

        onClick = {
            onAction()
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.account_icon_description),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        )
    }
}