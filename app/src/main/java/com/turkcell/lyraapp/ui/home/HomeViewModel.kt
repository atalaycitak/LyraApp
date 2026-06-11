package com.turkcell.lyraapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect: Flow<HomeEffect> = _effect.receiveAsFlow()

    init {
        onIntent(HomeIntent.LoadData)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadData -> loadHomeData()
            is HomeIntent.CategoryClicked -> handleCategoryClick(intent.categoryId)
            is HomeIntent.PlaylistClicked -> handlePlaylistClick(intent.playlistId)
        }
    }

    private fun loadHomeData() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false, errorMessage = null) }

            // Üç farklı ağ isteğini paralel çalıştırıyoruz
            val categoriesDeferred = async { homeRepository.getCategories() }
            val recentDeferred = async { homeRepository.getRecentPlaylists() }
            val forYouDeferred = async { homeRepository.getForYouPlaylists() }

            val categoriesResult = categoriesDeferred.await()
            val recentResult = recentDeferred.await()
            val forYouResult = forYouDeferred.await()

            if (categoriesResult.isSuccess && recentResult.isSuccess && forYouResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = categoriesResult.getOrNull() ?: emptyList(),
                        recentPlaylists = recentResult.getOrNull() ?: emptyList(),
                        forYouPlaylists = forYouResult.getOrNull() ?: emptyList()
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = "Veriler yüklenirken bir hata oluştu."
                    )
                }
                _effect.send(HomeEffect.ShowError("Veriler yüklenirken bir hata oluştu."))
            }
        }
    }

    private fun handleCategoryClick(categoryId: String) {
        viewModelScope.launch {
            _effect.send(HomeEffect.NavigateToCategory(categoryId))
        }
    }

    private fun handlePlaylistClick(playlistId: String) {
        viewModelScope.launch {
            _effect.send(HomeEffect.NavigateToPlaylist(playlistId))
        }
    }
}
