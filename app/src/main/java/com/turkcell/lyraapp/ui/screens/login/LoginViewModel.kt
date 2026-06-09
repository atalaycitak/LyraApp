package com.turkcell.lyraapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnPhoneNumberChange -> {
                _state.update { it.copy(phoneNumber = event.phoneNumber) }
            }
            is LoginEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.password) }
            }
            LoginEvent.OnTogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            LoginEvent.OnLoginClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    // Burada ileride Repository üzerinden ağ/doğrulama işlemleri yapılacaktır.
                    // Şimdilik sadece başarılı durumu simüle ediyoruz.
                    _effect.send(LoginEffect.NavigateToHome)
                    _state.update { it.copy(isLoading = false) }
                }
            }
            LoginEvent.OnRegisterClick -> {
                viewModelScope.launch {
                    _effect.send(LoginEffect.NavigateToRegister)
                }
            }
            LoginEvent.OnForgotPasswordClick -> {
                viewModelScope.launch {
                    _effect.send(LoginEffect.NavigateToForgotPassword)
                }
            }
        }
    }
}
