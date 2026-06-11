package com.turkcell.lyraapp.data.home

import kotlinx.coroutines.delay
import javax.inject.Inject

class MockHomeRepository @Inject constructor() : HomeRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        delay(500) // Ağ gecikmesi simülasyonu
        val categories = listOf(
            Category("1", "Gece Sürüşü", 0xFF6D649A),
            Category("2", "Sabah Kah...", 0xFF6B72D3),
            Category("3", "Neon Soka...", 0xFFC98442),
            Category("4", "Odaklan", 0xFF3DA39B),
            Category("5", "Derin Mavi", 0xFF74AD5C),
            Category("6", "Yaz Anıları", 0xFF408D9C)
        )
        return Result.success(categories)
    }

    override suspend fun getRecentPlaylists(): Result<List<Playlist>> {
        delay(500)
        val playlists = listOf(
            Playlist("1", "Neon Sokaklar", "Şehir Işıkları", 0xFFB67B46),
            Playlist("2", "Derin Mavi", "Okyanus", 0xFF7BA65B),
            Playlist("3", "Yıldız Tozu", "Polaris", 0xFF4EA19C)
        )
        return Result.success(playlists)
    }

    override suspend fun getForYouPlaylists(): Result<List<Playlist>> {
        delay(500)
        val playlists = listOf(
            Playlist("101", "", "", 0xFF8376AD),
            Playlist("102", "", "", 0xFF625BBA),
            Playlist("103", "", "", 0xFF358C84)
        )
        return Result.success(playlists)
    }
}
