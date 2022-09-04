package com.example.muzplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.muzplayer.components.BottomNavigationBar
import com.example.muzplayer.components.HomeBottomBar
import com.example.muzplayer.ui.home_screen.HomeBody
import com.example.muzplayer.ui.library_screen.LibraryBody
import com.example.muzplayer.ui.playlist_screen.PlaylistBody
import com.example.muzplayer.ui.theme.MuzPlayerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzPlayerTheme {
                setStatusColor()
                MainActivityScreen()
            }
        }
    }
}

@Composable
fun MainActivityScreen() {
    val allScreens = MusicScreen.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = MusicScreen.fromRoute(backstackEntry.value?.destination?.route)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    allScreens = allScreens,
                    onItemSelected = { screen ->
                        navController.navigate(screen.name)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
            ) {
                MusicNavHost(navController, modifier = Modifier.padding(bottom = 64.dp))
                HomeBottomBar(modifier = Modifier.align(Alignment.BottomCenter),)
            }
        }
    }
}


@Composable
fun MusicNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = MusicScreen.Home.name,
        modifier = modifier
    ) {
        composable(MusicScreen.Home.name) {
            HomeBody()
        }
        composable(MusicScreen.Library.name) {
            LibraryBody()
        }
        composable(MusicScreen.Playlists.name) {
            PlaylistBody()
        }
    }
}

@Composable
private fun setStatusColor() {
    val systemUiController = rememberSystemUiController()
    val primaryColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
    val darkTheme = isSystemInDarkTheme()

    systemUiController.setStatusBarColor(
        color = primaryColor,
        darkIcons = !darkTheme
    )
}
