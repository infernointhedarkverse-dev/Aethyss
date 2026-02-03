package com.aethyss.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.aethyss.ui.ChatScreen
import com.aethyss.ui.HomeScreen

private const val ROUTE_HOME = "home"
private const val ROUTE_CHAT = "chat"

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME,
        modifier = modifier
    ) {
        composable(ROUTE_HOME) {
            HomeScreen(onOpenChat = {
                navController.navigate(ROUTE_CHAT)
            })
        }
        composable(ROUTE_CHAT) {
            ChatScreen()
        }
    }
}
