package com.example.appblocker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appblocker.ui.BlocklistScreen
import com.example.appblocker.ui.BlocklistSelectorScreen
import com.example.appblocker.ui.HomeScreen
import com.example.appblocker.ui.ScheduleScreen

@Composable
fun Navigation(context: Context, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("HomeScreen") { HomeScreen(context = context, navController = navController) }
        composable("BlocklistScreen") { BlocklistScreen(context, navController = navController) }
        composable("BlocklistSelectorScreen") {
            BlocklistSelectorScreen(
                context,
                navController = navController
            )
        }
        composable("ScheduleScreen") { ScheduleScreen(context, navController = navController) }
    }
}
