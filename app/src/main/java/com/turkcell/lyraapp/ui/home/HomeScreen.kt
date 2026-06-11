package com.turkcell.lyraapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.data.home.Category
import com.turkcell.lyraapp.data.home.Playlist

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlaylist: (String) -> Unit = {},
    onNavigateToCategory: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is HomeEffect.NavigateToPlaylist -> onNavigateToPlaylist(effect.playlistId)
                is HomeEffect.NavigateToCategory -> onNavigateToCategory(effect.categoryId)
            }
        }
    }

    HomeScreen(
        state = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp) // Bottom Navigation Bar boşluğu
            ) {
                HomeHeader()

                Spacer(modifier = Modifier.height(16.dp))

                // Kategoriler Grid
                CategoryGridSection(
                    categories = state.categories,
                    onCategoryClick = { onIntent(HomeIntent.CategoryClicked(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Son Çalınanlar
                RecentPlaylistsSection(
                    playlists = state.recentPlaylists,
                    onPlaylistClick = { onIntent(HomeIntent.PlaylistClicked(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Senin İçin Çalma Listeleri
                ForYouPlaylistsSection(
                    playlists = state.forYouPlaylists,
                    onPlaylistClick = { onIntent(HomeIntent.PlaylistClicked(it)) }
                )
            }
        }
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "İyi akşamlar",
                color = Color.LightGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ne dinlemek istersin?",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.WbSunny,
                contentDescription = "Theme Settings",
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD49C9C)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ZK",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun CategoryGridSection(
    categories: List<Category>,
    onCategoryClick: (String) -> Unit
) {
    // LazyVerticalGrid scrollable Column içinde kullanılamayacağı için yüksekliği sabitliyoruz
    // 6 eleman, 2 kolon -> 3 satır
    val gridHeight = if (categories.isEmpty()) 0.dp else ((categories.size + 1) / 2 * 68).dp // 60dp yükseklik + 8dp boşluk
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false,
        modifier = Modifier.height(gridHeight)
    ) {
        items(categories) { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2A2A2A))
                    .clickable { onCategoryClick(category.id) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sol taraftaki renkli kare/ikon alanı
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(category.colorValue))
                ) {
                    // İçine hafif saydam desen eklenebilir, görselde öyle duruyor.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun RecentPlaylistsSection(
    playlists: List<Playlist>,
    onPlaylistClick: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Son çalınanlar",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tümü",
                color = Color(0xFFD49C9C),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* Tümü tıklandı */ }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(playlists) { playlist ->
                PlaylistCard(playlist = playlist, onClick = { onPlaylistClick(playlist.id) })
            }
        }
    }
}

@Composable
fun ForYouPlaylistsSection(
    playlists: List<Playlist>,
    onPlaylistClick: (String) -> Unit
) {
    Column {
        Text(
            text = "Senin için çalma listeleri",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(playlists) { playlist ->
                PlaylistCard(playlist = playlist, onClick = { onPlaylistClick(playlist.id) })
            }
        }
    }
}

@Composable
fun PlaylistCard(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(playlist.colorValue))
        )
        if (playlist.title.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = playlist.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (playlist.subtitle.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = playlist.subtitle,
                color = Color.Gray,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
