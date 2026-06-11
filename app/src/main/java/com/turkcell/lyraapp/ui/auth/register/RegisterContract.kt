package com.turkcell.lyraapp.ui.auth.register

data class RegisterUiState(
    val name: String = "",
    val surname: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val isRegisterEnabled: Boolean = false
)

sealed interface RegisterIntent {
    data class NameChanged(val value: String) : RegisterIntent
    data class SurnameChanged(val value: String) : RegisterIntent
    data class PhoneNumberChanged(val value: String) : RegisterIntent
    data class PasswordChanged(val value: String) : RegisterIntent
    data object TogglePasswordVisibility : RegisterIntent
    data object ToggleTerms : RegisterIntent
    data object Submit : RegisterIntent
    data object BackClicked : RegisterIntent
    data object LoginClicked : RegisterIntent
}

sealed interface RegisterEffect {
    data class ShowError(val message: String) : RegisterEffect
    data object NavigateToHome : RegisterEffect
    data object NavigateBack : RegisterEffect
    data object NavigateToLogin : RegisterEffect
}
