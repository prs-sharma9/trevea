package com.learn.android.trevea.ui.screens

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.learn.android.trevea.R
import com.learn.android.trevea.ui.components.TopAppBar
import com.learn.android.trevea.viewmodel.profile.ProfileViewModel
import com.learn.android.trevea.viewmodel.profile.ProfileViewModelFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.learn.android.trevea.data.local.preferences.userDataStore
import com.learn.android.trevea.data.local.repository.UserPreferenceRepository
import com.learn.android.trevea.ui.components.IconActionButton
import com.learn.android.trevea.ui.components.StringTitle
import com.learn.android.trevea.ui.components.UserStats
import java.io.File

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val tag = "Trevea: ProfileScreen"

    val context = LocalContext.current

    // Create ViewModel instance
    val uViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            prefRepository = UserPreferenceRepository(context.userDataStore)
        )
    )

    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current


    val profileUiStateValues = uViewModel.uiState.collectAsStateWithLifecycle().value

    val userName = profileUiStateValues.name
    val profilePhoto = profileUiStateValues.photoUrl
    Log.d(tag, "photoPath: $profilePhoto")

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            uViewModel.saveUserProfile(context = context, path = uri)
        }
    }

    val configuration = LocalConfiguration.current

    Log.d(tag, "Orientation: ${configuration.orientation}, Layout: ${configuration.screenLayout}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    StringTitle(stringResource(R.string.reg_user_screen_title))
                },
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
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ProfilePicture(
                        profilePhoto = profilePhoto,
                        choosePhotoHandler = {
                            photoPicker.launch(
                                PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    UserNameInput(
                        value = userName,
                        onValueChange = {
                            uViewModel.updateUserName(it)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardManager?.hide()
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    UserStats(
                        longestStreak = profileUiStateValues.longestStreak.toString(),
                        totalQuestions = profileUiStateValues.totalQuestions.toString(),
                        totalCorrectAns = profileUiStateValues.totalCorrectAns.toString()
                    )
                }
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.5f)
                            .padding(start = 10.dp)
                    ) {
                        ProfilePicture(
                            profilePhoto = profilePhoto,
                            choosePhotoHandler = {
                                photoPicker.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        UserNameInput(
                            value = userName,
                            onValueChange = {
                                uViewModel.updateUserName(it)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardManager?.hide()
                                    focusManager.clearFocus()
                                }
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    UserStats(
                        modifier = Modifier.weight(0.5f),
                        longestStreak = profileUiStateValues.longestStreak.toString(),
                        totalQuestions = profileUiStateValues.totalQuestions.toString(),
                        totalCorrectAns = profileUiStateValues.totalCorrectAns.toString()
                    )
                }
            }
        }

    }
}

@Composable
fun ProfilePicture(
    profilePhoto: String?,
    choosePhotoHandler: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if(profilePhoto != null) {
            AsyncImage(
                model = File(profilePhoto),
                contentDescription = stringResource(R.string.profile_picture_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
                    .border(2.dp, colorResource(R.color.secondary), shape = CircleShape),
                clipToBounds = true
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = stringResource(R.string.profile_picture_description),
                tint = colorResource(R.color.secondary),
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .padding(10.dp)
            )
        }

        TextButton(
            modifier = Modifier,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.secondary)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation = 1.dp),
            border = BorderStroke(
                width = Dp.Hairline,
                color = colorResource(R.color.secondary)
            ),
            onClick = {
                choosePhotoHandler()
            }
        ) {
            Text(
                text = stringResource(R.string.choose_profile_picture),
                style = MaterialTheme.typography.displayMedium,
                color = colorResource(R.color.accent)
            )
        }
    }
}

@Composable
fun UserNameInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(0.8f),
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.displayMedium,
        label = {
            Text(
                text = stringResource(R.string.username_label),
                style = MaterialTheme.typography.displaySmall,
                color = colorResource(R.color.accent)
            )
        },
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = colorResource(R.color.accent),
            unfocusedTextColor = colorResource(R.color.accent),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = colorResource(R.color.accent),

        )
    )
}