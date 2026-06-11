package com.turkcell.lyraapp.ui.auth.login

data class LoginUiState(
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false
)

sealed interface LoginIntent {
    data class PhoneNumberChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object TogglePasswordVisibility : LoginIntent
    data object Submit : LoginIntent
    data object RegisterClicked : LoginIntent
    data object ForgotPasswordClicked : LoginIntent
}

sealed interface LoginEffect {
    data class ShowError(val message: String) : LoginEffect
    data object NavigateToHome : LoginEffect
    data object NavigateToRegister : LoginEffect
    data object NavigateToForgotPassword : LoginEffect
}
