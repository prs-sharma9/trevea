package com.learn.android.trevea.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.learn.android.trevea.components.CateogryList
import com.learn.android.trevea.components.TopAppBar
import com.learn.android.trevea.network.repository.OtdbRepository
import com.learn.android.trevea.network.retrofit.RetrofitInstance
import com.learn.android.trevea.viewmodel.TreveaViewModel
import com.learn.android.trevea.viewmodel.TreveaViewModelFactory

@Preview
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: TreveaViewModel = viewModel(
        factory = TreveaViewModelFactory(repository = OtdbRepository(api = RetrofitInstance.otdbApi))
    )

    val uiState = viewModel.categoryUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when(uiState.value) {
                is TreveaViewModel.CategoryUiState.Error -> {
                    Text(text = "Error!")
                }
                TreveaViewModel.CategoryUiState.Loading -> {
                    Text(text = "Loading...")
                }
                is TreveaViewModel.CategoryUiState.Success -> {
                    CateogryList(
                        categories = (uiState.value as TreveaViewModel.CategoryUiState.Success)
                            .categories
                    )
                }
            }
        }
    }
}