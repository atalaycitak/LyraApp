package com.turkcell.lyraapp.ui.home

import com.turkcell.lyraapp.data.home.Category
import com.turkcell.lyraapp.data.home.Playlist

data class HomeUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val recentPlaylists: List<Playlist> = emptyList(),
    val forYouPlaylists: List<Playlist> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String? = null
)

sealed interface HomeIntent {
    object LoadData : HomeIntent
    data class CategoryClicked(val categoryId: String) : HomeIntent
    data class PlaylistClicked(val playlistId: String) : HomeIntent
}

sealed interface HomeEffect {
    data class ShowError(val message: String) : HomeEffect
    data class NavigateToPlaylist(val playlistId: String) : HomeEffect
    data class NavigateToCategory(val categoryId: String) : HomeEffect
}
