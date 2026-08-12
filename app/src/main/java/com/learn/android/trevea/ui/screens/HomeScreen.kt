package com.learn.android.trevea.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.learn.android.trevea.R
import com.learn.android.trevea.data.local.database.TreveaDatabase
import com.learn.android.trevea.data.local.preferences.userDataStore
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.data.local.repository.UserRepository
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.ui.components.IconActionButton
import com.learn.android.trevea.ui.components.TextActionButton
import com.learn.android.trevea.viewmodel.profile.ProfileViewModel
import com.learn.android.trevea.viewmodel.profile.ProfileViewModelFactory


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
//    val viewModel: QuizViewModel = viewModel(
//        factory = QuizViewModelFactory(repository = OtdbRepository(api = RetrofitInstance.otdbApi))
//    )

//    val uiState = viewModel.quizUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            repository = UserRepository(
                userCategoryDao = TreveaDatabase.getInstance(context).userCategoryDao()),
            prefRepository = UserPreferenceRepository(
                dataStore = context.userDataStore
            )
        )
    )

    val profileUiState = profileViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(
            enableBackNavigation = false,
            navController = navController,
            enableActions = true,
            actions = {
                IconActionButton(
                    icon = Icons.Default.AccountCircle,
                    description = stringResource(R.string.account_icon_description)
                ) {
                    navController.navigate("register")
                }
            }
        ) }
    ) { innerPadding ->
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
                        navController.navigate("quiz")
                    }
                )
            }
    }
}