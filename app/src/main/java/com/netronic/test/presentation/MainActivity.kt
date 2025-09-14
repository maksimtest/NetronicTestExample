package com.netronic.test.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.netronic.test.presentation.details.UserDetailsScreen
import com.netronic.test.presentation.users.UsersScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppRoot() }
    }
}

@Composable
private fun AppRoot() {
    val nav = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = nav, startDestination = "users") {
            composable("users") {
                UsersScreen(onUserClick = { id -> nav.navigate("details/${id}") })
            }
            composable(
                route = "details/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { UserDetailsScreen() }
        }
    }
}
