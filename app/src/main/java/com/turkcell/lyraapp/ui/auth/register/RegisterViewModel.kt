package com.turkcell.lyraapp.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _effect = Channel<RegisterEffect>(Channel.BUFFERED)
    val effect: Flow<RegisterEffect> = _effect.receiveAsFlow()

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.NameChanged -> updateForm { it.copy(name = intent.value) }
            is RegisterIntent.SurnameChanged -> updateForm { it.copy(surname = intent.value) }
            is RegisterIntent.PhoneNumberChanged -> updateForm { it.copy(phoneNumber = intent.value) }
            is RegisterIntent.PasswordChanged -> updateForm { it.copy(password = intent.value) }
            is RegisterIntent.TogglePasswordVisibility -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is RegisterIntent.ToggleTerms -> updateForm { it.copy(isTermsAccepted = !it.isTermsAccepted) }
            is RegisterIntent.Submit -> submit()
            is RegisterIntent.BackClicked -> viewModelScope.launch { _effect.send(RegisterEffect.NavigateBack) }
            is RegisterIntent.LoginClicked -> viewModelScope.launch { _effect.send(RegisterEffect.NavigateToLogin) }
        }
    }

    private fun updateForm(transform: (RegisterUiState) -> RegisterUiState) {
        _uiState.update { current ->
            val updated = transform(current)
            updated.copy(isRegisterEnabled = updated.isFormValid())
        }
    }

    private fun RegisterUiState.isFormValid(): Boolean {
        val hasMinLength = password.length >= 8
        val hasDigit = password.any { it.isDigit() }
        return name.isNotBlank() && 
               surname.isNotBlank() && 
               phoneNumber.isNotBlank() && 
               hasMinLength && hasDigit && 
               isTermsAccepted
    }

    private fun submit() {
        val state = _uiState.value
        if (!state.isRegisterEnabled || state.isLoading) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.login(state.phoneNumber, state.password)
            _uiState.update { it.copy(isLoading = false) }
            
            result.onSuccess {
                _effect.send(RegisterEffect.NavigateToHome)
            }.onFailure { error ->
                _effect.send(RegisterEffect.ShowError(error.message ?: "Kayıt sırasında hata oluştu"))
            }
        }
    }
}
