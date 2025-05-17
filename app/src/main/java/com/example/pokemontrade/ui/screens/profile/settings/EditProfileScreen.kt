package com.example.pokemontrade.ui.screens.profile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pokemontrade.data.models.users.UserProfileRequest
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.screens.profile.UsersViewModel
import com.example.pokemontrade.ui.screens.profile.UsersViewModelFactory
import com.example.pokemontrade.ui.theme.DisabledRed
import com.example.pokemontrade.ui.theme.RedPrimary
import com.example.pokemontrade.ui.theme.SettingsRed
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val usersViewModel: UsersViewModel = viewModel(factory = UsersViewModelFactory(tokenManager))

    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var originalName by remember { mutableStateOf("") }
    var originalLocation by remember { mutableStateOf("") }
    var originalEmail by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val profile = usersViewModel.getUserProfile()
        profile?.let {
            originalName = it.name
            originalLocation = it.location
            originalEmail = it.email

            name = it.name
            location = it.location
            email = it.email
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edita tu perfil", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(SettingsRed),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Cambiar foto",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            Text("Información básica", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField(label = "Nombre", value = name, onValueChange = { name = it })

            Spacer(modifier = Modifier.height(12.dp))

            ProfileTextField(label = "Email", value = email, onValueChange = { email = it })

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { navController.navigate("select_location_map/$name") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ubicación", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }

            Divider(color = RedPrimary.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    scope.launch {
                        val updateRequest = UserProfileRequest(
                            name = if (name != originalName) name else null,
                            email = if (email != originalEmail) email else null,
                            location = if (location != originalLocation) location else null
                        )

                        val updatedProfile = usersViewModel.updateUserProfile(updateRequest)
                        if (updatedProfile != null) {
                            tokenManager.saveUserProfile(updatedProfile)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .padding(vertical = 52.dp, horizontal = 32.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SettingsRed,
                    contentColor = Color.White,
                )
            ) {
                Text("Guardar cambios")
            }
        }
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = RedPrimary.copy(alpha = 0.5f),
            modifier = Modifier.padding(bottom = 0.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = RedPrimary,
                unfocusedIndicatorColor = RedPrimary.copy(alpha = 0.5f),
                cursorColor = RedPrimary,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewEditProfileScreen() {
    val navController = rememberNavController()
    EditProfileScreen(navController = navController)
}
