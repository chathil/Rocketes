package com.chathil.rocketes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chathil.rocketes.rocket.detailScreen
import com.chathil.rocketes.rocket.navigateToDetail
import com.chathil.rocketes.rockets.ListRoute
import com.chathil.rocketes.rockets.listScreen
import com.chathil.rocketes.shared.theme.RocketesTheme
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
