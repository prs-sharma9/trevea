package com.learn.android.trevea.ui.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.learn.android.trevea.R
import com.learn.android.trevea.data.local.preferences.userDataStore
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.ui.components.IconActionButton
import com.learn.android.trevea.ui.components.StringTitle
import com.learn.android.trevea.ui.components.TextActionButton
import com.learn.android.trevea.viewmodel.profile.ProfileViewModel
import com.learn.android.trevea.viewmodel.profile.ProfileViewModelFactory
import java.io.File


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val tag = "HomeScreen"

    val context = LocalContext.current

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            prefRepository = UserPreferenceRepository(
                dataStore = context.userDataStore
            )
        )
    )

    val profileUiState = profileViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(
            title = {
                StringTitle(stringResource(R.string.app_name))
            },
            enableBackNavigation = false,
            navController = navController,
            enableActions = true,
            actions = {
                if (profileUiState.value.photoUrl != null) {
                    AsyncImage(
                        model = File(profileUiState.value.photoUrl),
                        contentDescription = stringResource(R.string.account_icon_description),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClick = {
                                    navController.navigate("register")
                                }
                            )
                            .border(1.dp, MaterialTheme.colorScheme.primary)
                    )
                } else {
                    IconActionButton(
                        icon = Icons.Default.AccountCircle,
                        description = stringResource(R.string.account_icon_description)
                    ) {
                        navController.navigate("register")
                    }
                }

            }
        ) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Log.d(tag, "HomeScreen")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
//                        .border(1.dp, color = Color.Black)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = profileUiState.value.name,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            TextActionButton (
                label = stringResource(R.string.start_quiz),
                onAction = {
                    navController.navigate("quiz_config")
                }
            )
        }
    }
}