package com.example.appblocker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appblocker.ui.PlaylistScreen
import com.example.appblocker.ui.PlaylistSelectorScreen
import com.example.appblocker.ui.HomeScreen
import com.example.appblocker.ui.ScheduleScreen

@Composable
fun Navigation(context: Context, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("HomeScreen") { HomeScreen(navController = navController) }
        composable("PlaylistScreen") { PlaylistScreen(context, navController = navController) }
        composable("PlaylistSelectorScreen") {
            PlaylistSelectorScreen(
                context,
                navController = navController
            )
        }
        composable("ScheduleScreen") { ScheduleScreen(context, navController = navController) }
    }
}
