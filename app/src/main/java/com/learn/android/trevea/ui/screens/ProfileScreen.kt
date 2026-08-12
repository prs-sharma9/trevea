package com.learn.android.trevea.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.learn.android.trevea.R
import com.learn.android.trevea.data.local.database.TreveaDatabase
import com.learn.android.trevea.data.local.repository.UserRepository
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.viewmodel.profile.ProfileViewModel
import com.learn.android.trevea.viewmodel.profile.ProfileViewModelFactory
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.learn.android.trevea.data.local.preferences.userDataStore
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.data.remote.repository.OtdbRepository
import com.learn.android.trevea.data.remote.retrofit.RetrofitInstance
import com.learn.android.trevea.ui.components.IconActionButton
import com.learn.android.trevea.ui.components.UserStats
import com.learn.android.trevea.viewmodel.quiz.QuizViewModel
import com.learn.android.trevea.viewmodel.quiz.QuizViewModelFactory

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val tag = "REGISTRATION_SCREEN"

    val context = LocalContext.current

    // Create ViewModel instance
    val uViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            repository = UserRepository(
                userCategoryDao = TreveaDatabase.getInstance(
                    context = context
                ).userCategoryDao()
            ),
            prefRepository = UserPreferenceRepository(context.userDataStore)
        )
    )

    val tViewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(
            repository = OtdbRepository(
                api = RetrofitInstance.otdbApi
            )
        )
    )

    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current


    val profileUiStateValues = uViewModel.uiState.collectAsStateWithLifecycle().value
    val allCategoryList = tViewModel.quizUiState.collectAsStateWithLifecycle().value

    val userName = profileUiStateValues.name
//    val userCategoryList = regUiStateValues.userCategories

//    Log.d(tag, "User Category Count: ${userCategoryList.size}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.reg_user_screen_title),
                enableBackNavigation = true,
                navController = navController,
                enableActions = true,
                actions = {
                    IconActionButton (
                        icon = Icons.Default.Save,
                        description = stringResource(R.string.save_btn_descp)
                    ) {
                        uViewModel.saveUserProfile()
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.profile_picture_description),
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                        .padding(20.dp)
                )
                TextButton(
                    modifier = Modifier,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation = 1.dp),
                    border = BorderStroke(
                        width = Dp.Hairline,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    onClick = {}
                ) {
                    Text(
                        text = stringResource(R.string.choose_profile_picture),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                value = userName,
                onValueChange = { uViewModel.updateUserName(it)},
                textStyle = MaterialTheme.typography.labelMedium,
                label = {
                    Text(text = stringResource(R.string.username_label))
                },
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardManager?.hide() }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.user_stats),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

//            if ( allCategoryList is QuizViewModel.QuizUiState.CategoryList) {
//                CategoryList (
//                    allCategories = allCategoryList.categories,
//                    userCategories = userCategoryList,
//                    selectionMode = true,
//                    onSelectionChanged = { category ->
//                        uViewModel.toggleUserCategory(category)
//                    }
//                )
//            } else {
//                Text(text="loading")
//            }

            UserStats(
                longestStreak = profileUiStateValues.longestStreak.toString(),
                totalQuestions = profileUiStateValues.totalQuestions.toString(),
                totalCorrectAns = profileUiStateValues.totalCorrectAns.toString()
            )
        }
    }
}