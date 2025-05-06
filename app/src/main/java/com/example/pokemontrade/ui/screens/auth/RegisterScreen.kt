package com.example.pokemontrade.ui.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.DisabledBlue
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onRegisterClick: (name: String, username: String, email: String, password: String) -> Unit = { _, _, _, _ -> }
) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(RetrofitInstance.api))
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isFormValid =
        name.isNotBlank() && username.isNotBlank() && email.isNotBlank() && password.length >= 8

    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 30.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                modifier = Modifier.size(36.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Únete a Poketrade",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre", color = BluePrimary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DisabledBlue,
                    unfocusedBorderColor = DisabledBlue,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario", color = BluePrimary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DisabledBlue,
                    unfocusedBorderColor = DisabledBlue,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                label = { Text("Email", color = BluePrimary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DisabledBlue,
                    unfocusedBorderColor = DisabledBlue,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = BluePrimary) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    val description =
                        if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = description,
                            tint = DisabledBlue
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DisabledBlue,
                    unfocusedBorderColor = DisabledBlue,
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (password.isNotEmpty() && password.length < 8) {
                Text(
                    text = "La contraseña debe tener al menos 8 caracteres.",
                    fontSize = 12.sp,
                    color = BluePrimary
                )
            }

            Spacer(modifier = Modifier.height(270.dp))

            Button(
                onClick = {
                    scope.launch {
                        viewModel.register(
                            name = name,
                            username = username,
                            email = email,
                            password = password,
                            onSuccess = {
                                scope.launch {
                                    tokenManager.saveToken(it.access_token)
                                    context.getSharedPreferences(
                                        "UserSession",
                                        Context.MODE_PRIVATE
                                    ).edit {
                                        putBoolean("isLoggedIn", true)
                                        putString("name", it.user.name)
                                    }
                                    Toast.makeText(context, "¡Cuenta creada!", Toast.LENGTH_SHORT)
                                        .show()
                                    onRegisterClick(it.user.name, username, email, password)
                                }
                            },
                            onError = {
                                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(40.dp)),
                shape = CircleShape,
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary,
                    contentColor = Color.White,
                    disabledContainerColor = DisabledBlue,
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    com.example.pokemontrade.ui.theme.PokemonTradeTheme {
        RegisterScreen()
    }
}
