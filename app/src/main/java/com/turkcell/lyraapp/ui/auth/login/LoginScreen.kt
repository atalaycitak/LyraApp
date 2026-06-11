package com.turkcell.lyraapp.ui.auth.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.ui.theme.LyraAppTheme

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> onNavigateToHome()
                is LoginEffect.NavigateToRegister -> onNavigateToRegister()
                is LoginEffect.NavigateToForgotPassword -> onNavigateToForgotPassword()
                is LoginEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onIntent: (LoginIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Logo Box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Hoşgeldin Başlığı
            Text(
                text = "Tekrar hoş geldin",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Alt Başlık
            Text(
                text = "Hesabına giriş yap, kaldığın yerden\ndinlemeye devam et.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Telefon Numarası Alanı
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = { onIntent(LoginIntent.PhoneNumberChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Telefon numarası") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Smartphone,
                        contentDescription = "Telefon"
                    )
                },
                prefix = {
                    Text(
                        text = "+90 ",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                placeholder = { Text("5XX XXX XX XX") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Şifre Alanı
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Şifre") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Şifre"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onIntent(LoginIntent.TogglePasswordVisibility) }) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (uiState.isPasswordVisible) "Şifreyi Gizle" else "Şifreyi Göster"
                        )
                    }
                },
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Şifremi Unuttum Butonu
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                TextButton(onClick = { onIntent(LoginIntent.ForgotPasswordClicked) }) {
                    Text(
                        text = "Şifremi unuttum",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Giriş Yap Butonu
            Button(
                onClick = { onIntent(LoginIntent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = uiState.isLoginEnabled && !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Giriş yap",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Kayıt Ol Alanı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hesabın yok mu?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { onIntent(LoginIntent.RegisterClicked) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Kayıt ol",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun LoginScreenDarkPreview() {
    LyraAppTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode")
@Composable
fun LoginScreenLightPreview() {
    LyraAppTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
