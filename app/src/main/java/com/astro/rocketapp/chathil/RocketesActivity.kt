package com.astro.rocketapp.chathil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.astro.rocketapp.rocket.detailScreen
import com.astro.rocketapp.rocket.navigateToDetail
import com.astro.rocketapp.rockets.ListRoute
import com.astro.rocketapp.rockets.listScreen
import com.astro.rocketapp.shared.theme.RocketesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            RocketesTheme {
                NavHost(
                    navController = navController,
                    startDestination = ListRoute,
                    modifier = Modifier,
                ) {
                    listScreen { id ->
                        navController.navigateToDetail(id)
                    }
                    detailScreen {
                        navController.navigateUp()
                    }
                }
            }
        }
    }
}
