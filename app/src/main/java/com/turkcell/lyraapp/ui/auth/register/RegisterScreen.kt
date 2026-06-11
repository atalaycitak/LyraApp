package com.turkcell.lyraapp.ui.auth.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.ui.theme.LyraAppTheme

@Composable
fun RegisterRoute(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RegisterEffect.NavigateToLogin -> onNavigateToLogin()
                is RegisterEffect.NavigateToHome -> onNavigateToHome()
                is RegisterEffect.NavigateBack -> onNavigateBack()
                is RegisterEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    RegisterScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onIntent(RegisterIntent.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Başlık
            Text(
                text = "Hesap oluştur",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Alt Başlık
            Text(
                text = "Birkaç adımda Lyra'ya katıl ve çalma\nlisteni oluştur.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Ad ve Soyad
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { onIntent(RegisterIntent.NameChanged(it)) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Ad") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = uiState.surname,
                    onValueChange = { onIntent(RegisterIntent.SurnameChanged(it)) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Soyad") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Telefon Numarası
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = { onIntent(RegisterIntent.PhoneNumberChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Telefon numarası") },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Smartphone, contentDescription = "Telefon")
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

            // Şifre
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onIntent(RegisterIntent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Şifre") },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Şifre")
                },
                trailingIcon = {
                    IconButton(onClick = { onIntent(RegisterIntent.TogglePasswordVisibility) }) {
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

            Spacer(modifier = Modifier.height(8.dp))

            // Şifre Gücü Çubukları ve Uyarı
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val hasMinLength = uiState.password.length >= 8
                val hasDigit = uiState.password.any { it.isDigit() }
                
                // Basit bir güç hesaplaması
                val strengthLevel = when {
                    uiState.password.isEmpty() -> 0
                    hasMinLength && hasDigit -> 3
                    hasMinLength || hasDigit -> 2
                    else -> 1
                }

                val activeColor = MaterialTheme.colorScheme.primary
                val inactiveColor = MaterialTheme.colorScheme.surfaceVariant

                Box(modifier = Modifier.weight(1f).height(4.dp).background(if (strengthLevel >= 1) activeColor else inactiveColor, RoundedCornerShape(2.dp)))
                Box(modifier = Modifier.weight(1f).height(4.dp).background(if (strengthLevel >= 2) activeColor else inactiveColor, RoundedCornerShape(2.dp)))
                Box(modifier = Modifier.weight(1f).height(4.dp).background(if (strengthLevel >= 3) activeColor else inactiveColor, RoundedCornerShape(2.dp)))
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "En az 8 karakter, bir rakam içermeli.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Kullanım Koşulları
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.isTermsAccepted,
                    onCheckedChange = { onIntent(RegisterIntent.ToggleTerms) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                        append("Kullanım Koşulları")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append(" ve ")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                        append("Gizlilik\nPolitikası")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append("'nı okudum, kabul ediyorum.")
                    }
                }
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { onIntent(RegisterIntent.ToggleTerms) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Kayıt Ol Butonu
            Button(
                onClick = { onIntent(RegisterIntent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = uiState.isRegisterEnabled && !uiState.isLoading,
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
                            text = "Kayıt ol",
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

            Spacer(modifier = Modifier.height(24.dp))

            // Giriş Yap Alanı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Zaten hesabın var mı?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { onIntent(RegisterIntent.LoginClicked) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Giriş yap",
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
fun RegisterScreenDarkPreview() {
    LyraAppTheme {
        RegisterScreen(
            uiState = RegisterUiState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode")
@Composable
fun RegisterScreenLightPreview() {
    LyraAppTheme {
        RegisterScreen(
            uiState = RegisterUiState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
