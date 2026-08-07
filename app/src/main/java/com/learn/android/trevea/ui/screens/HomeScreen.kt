package com.learn.android.trevea.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.learn.android.trevea.ui.components.CategoryList
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import com.learn.android.trevea.data.remote.retrofit.RetrofitInstance
import com.learn.android.trevea.ui.components.TopBarActionIcon
import com.learn.android.trevea.viewmodel.trevea.TreveaViewModel
import com.learn.android.trevea.viewmodel.trevea.TreveaViewModelFactory


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: TreveaViewModel = viewModel(
        factory = TreveaViewModelFactory(repository = OtdbRepository(api = RetrofitInstance.otdbApi))
    )

    val uiState = viewModel.categoryUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(
            enableNavigationIcon = false,
            navController = navController,
            enableActions = true,
            actions = {
                TopBarActionIcon(
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
            when(uiState.value) {
                is TreveaViewModel.CategoryUiState.Error -> {
                    Text(text = "Error!")
                }
                TreveaViewModel.CategoryUiState.Loading -> {
                    Text(text = "Loading...")
                }
                is TreveaViewModel.CategoryUiState.Success -> {
                    CategoryList(
                        allCategories = (uiState.value as TreveaViewModel.CategoryUiState.Success)
                            .categories.sortedBy { it.categoryId },
                        userCategories = emptyList()
                    )
                }
            }
        }
    }
}