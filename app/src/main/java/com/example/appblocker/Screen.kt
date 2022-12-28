package com.example.appblocker

sealed class Screen (val route: String) {
    object MainScreen : Screen("main_screen")
    object PlaylistScreen : Screen("playlist_screen")
    object PlaylistSelectorScreen : Screen("playlist_selector_screen")
}