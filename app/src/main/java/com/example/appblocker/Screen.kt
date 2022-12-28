package com.example.appblocker

sealed class Screen (val route: String) {
    object HomeScreen : Screen("home_screen")
    object PlaylistScreen : Screen("playlist_screen")
    object PlaylistSelectorScreen : Screen("playlist_selector_screen")
}