package com.turkcell.lyraapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Routes.HOME, "Ana sayfa", Icons.Filled.Home)
    object Search : BottomNavItem(Routes.SEARCH, "Ara", Icons.Filled.Search)
    object Library : BottomNavItem(Routes.LIBRARY, "Kütüphane", Icons.Filled.LibraryMusic)
    object Favorites : BottomNavItem(Routes.FAVORITES, "Favoriler", Icons.Filled.Favorite)
    object Profile : BottomNavItem(Routes.PROFILE, "Profil", Icons.Filled.Person)
}
