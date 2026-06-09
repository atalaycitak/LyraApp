package com.turkcell.lyraapp.ui.screens.login

data class LoginState(
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface LoginEvent {
    data class OnPhoneNumberChange(val phoneNumber: String) : LoginEvent
    data class OnPasswordChange(val password: String) : LoginEvent
    data object OnTogglePasswordVisibility : LoginEvent
    data object OnLoginClick : LoginEvent
    data object OnRegisterClick : LoginEvent
    data object OnForgotPasswordClick : LoginEvent
}

sealed interface LoginEffect {
    data class ShowError(val message: String) : LoginEffect
    data object NavigateToHome : LoginEffect
    data object NavigateToRegister : LoginEffect
    data object NavigateToForgotPassword : LoginEffect
}
