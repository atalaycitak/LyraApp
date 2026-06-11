package com.turkcell.lyraapp.data.home

interface HomeRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getRecentPlaylists(): Result<List<Playlist>>
    suspend fun getForYouPlaylists(): Result<List<Playlist>>
}
