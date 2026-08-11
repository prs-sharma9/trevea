package com.learn.android.trevea.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.learn.android.trevea.data.local.database.TreveaDatabase
import com.learn.android.trevea.data.local.preferences.userDataStore
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.data.local.repository.UserRepository
import com.learn.android.trevea.ui.components.CategoryList
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import com.learn.android.trevea.data.remote.retrofit.RetrofitInstance
import com.learn.android.trevea.ui.components.IconActionButton
import com.learn.android.trevea.ui.components.TextActionButton
import com.learn.android.trevea.viewmodel.trevea.TreveaViewModel
import com.learn.android.trevea.viewmodel.trevea.TreveaViewModelFactory
import com.learn.android.trevea.viewmodel.user.UserViewModel
import com.learn.android.trevea.viewmodel.user.UserViewModelFactory


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
//    val viewModel: TreveaViewModel = viewModel(
//        factory = TreveaViewModelFactory(repository = OtdbRepository(api = RetrofitInstance.otdbApi))
//    )

//    val uiState = viewModel.categoryUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            repository = UserRepository(
                userCategoryDao = TreveaDatabase.getInstance(context).userCategoryDao()),
            prefRepository = UserPreferenceRepository(
                dataStore = context.userDataStore
            )
        )
    )

    Scaffold(
        topBar = { TopAppBar(
            enableNavigationIcon = false,
            navController = navController,
            enableActions = true,
            actions = {
                IconActionButton(
                    onAction = {
                        navController.navigate("register")
                    }
                )
            }
        ) }
    ) { innerPadding ->
        Surface (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextActionButton (label = "Start Quiz")
            }

        }
    }
}