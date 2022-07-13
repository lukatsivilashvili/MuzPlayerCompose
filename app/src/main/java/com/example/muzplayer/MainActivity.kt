package com.example.muzplayer

import android.os.Bundle
import android.service.autofill.UserData
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.muzplayer.components.RallyTabRow
import com.example.muzplayer.ui.home_screen.HomeBody
import com.example.muzplayer.ui.library_screen.LibraryBody
import com.example.muzplayer.ui.playlist_screen.PlaylistBody
import com.example.muzplayer.ui.theme.MuzPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzPlayerTheme {
                val allScreens = MusicScreen.values().toList()
                val navController = rememberNavController()
                val backstackEntry = navController.currentBackStackEntryAsState()
                val currentScreen = MusicScreen.fromRoute(backstackEntry.value?.destination?.route)

                Scaffold(
                    topBar = {
                        RallyTabRow(
                            allScreens = allScreens,
                            onTabSelected = { screen ->
                                navController.navigate(screen.name)
                            },
                            currentScreen = currentScreen
                        )
                    }
                ) { innerPadding ->
                    RallyNavHost(navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun RallyNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
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
