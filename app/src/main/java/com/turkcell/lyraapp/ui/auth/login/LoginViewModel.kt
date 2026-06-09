package com.turkcell.lyraapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.OnPhoneNumberChange -> {
                _uiState.update { it.copy(phoneNumber = intent.phoneNumber) }
            }
            is Intent.OnPasswordChange -> {
                _uiState.update { it.copy(password = intent.password) }
            }
            Intent.OnTogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            Intent.OnLoginClick -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    
                    val result = authRepository.login(
                        phoneNumber = _uiState.value.phoneNumber,
                        password = _uiState.value.password
                    )
                    
                    _uiState.update { it.copy(isLoading = false) }
                    
                    result.onSuccess {
                        _effect.send(Effect.NavigateToHome)
                    }.onFailure { error ->
                        _effect.send(Effect.ShowError(error.message ?: "Bilinmeyen bir hata oluştu"))
                    }
                }
            }
            Intent.OnRegisterClick -> {
                viewModelScope.launch {
                    _effect.send(Effect.NavigateToRegister)
                }
            }
            Intent.OnForgotPasswordClick -> {
                viewModelScope.launch {
                    _effect.send(Effect.NavigateToForgotPassword)
                }
            }
        }
    }
}
