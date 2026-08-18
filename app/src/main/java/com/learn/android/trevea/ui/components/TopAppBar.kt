package com.learn.android.trevea.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.navigation.NavController
import com.learn.android.trevea.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit),
    enableBackNavigation: Boolean = false,
    navController: NavController,
    enableActions: Boolean = false,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = title,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            if (enableBackNavigation) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.back_icon_description),
                        tint = colorResource(R.color.secondary)
                    )
                }
            }
        },
        actions = {
            if (enableActions) {
                actions()
            }
        }
    )
}


@Composable
fun StringTitle(
    titleTxt: String
) {
    Text(
        text = titleTxt.capitalize(locale = Locale.current),
        style = MaterialTheme.typography.titleLarge,
        color = colorResource(R.color.secondary)
    )
}