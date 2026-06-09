package com.turkcell.lyraapp.ui.auth.login

data class UiState(
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface Intent {
    data class OnPhoneNumberChange(val phoneNumber: String) : Intent
    data class OnPasswordChange(val password: String) : Intent
    data object OnTogglePasswordVisibility : Intent
    data object OnLoginClick : Intent
    data object OnRegisterClick : Intent
    data object OnForgotPasswordClick : Intent
}

sealed interface Effect {
    data class ShowError(val message: String) : Effect
    data object NavigateToHome : Effect
    data object NavigateToRegister : Effect
    data object NavigateToForgotPassword : Effect
}
