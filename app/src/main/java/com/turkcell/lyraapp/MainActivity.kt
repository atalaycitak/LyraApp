package com.turkcell.lyraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.turkcell.lyraapp.ui.theme.LyraAppTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LyraAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                val bottomBarRoutes = listOf(
                    com.turkcell.lyraapp.ui.navigation.Routes.HOME,
                    com.turkcell.lyraapp.ui.navigation.Routes.SEARCH,
                    com.turkcell.lyraapp.ui.navigation.Routes.LIBRARY,
                    com.turkcell.lyraapp.ui.navigation.Routes.FAVORITES,
                    com.turkcell.lyraapp.ui.navigation.Routes.PROFILE
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute in bottomBarRoutes) {
                            com.turkcell.lyraapp.ui.components.LyraBottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    com.turkcell.lyraapp.ui.navigation.LyraAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LyraAppTheme {
        Greeting("Android")
    }
}

