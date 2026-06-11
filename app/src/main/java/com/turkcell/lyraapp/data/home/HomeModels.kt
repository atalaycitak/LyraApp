package com.turkcell.lyraapp.data.home

data class Category(
    val id: String,
    val title: String,
    val colorValue: Long // Örneğin: 0xFF5D52A0
)

data class Playlist(
    val id: String,
    val title: String,
    val subtitle: String,
    val colorValue: Long
)
