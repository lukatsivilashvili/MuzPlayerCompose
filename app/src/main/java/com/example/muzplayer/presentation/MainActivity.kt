package com.example.muzplayer.presentation

import AlbumScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.muzplayer.common.MusicScreen
import com.example.muzplayer.presentation.components.BottomNavigationBar
import com.example.muzplayer.presentation.ui.bottom_bar.HomeBottomBar
import com.example.muzplayer.presentation.ui.library_screen.LibraryScreen
import com.example.muzplayer.presentation.ui.player_screen.PlayerScreen
import com.example.muzplayer.presentation.ui.playlist_screen.PlaylistScreen
import com.example.muzplayer.presentation.ui.theme.MuzPlayerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzPlayerTheme {
                SetStatusColor()
                MainActivityScreen()
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(
) {
    val allScreens = MusicScreen.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = MusicScreen.fromRoute(backstackEntry.value?.destination?.route)

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
    ) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                PlayerScreen(
                    bottomSheetScaffoldState = bottomSheetScaffoldState
                )
            },
            sheetPeekHeight = 0.dp
        ) {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                bottomBar = {
                    BottomNavigationBar(
                        allScreens = allScreens,
                        onItemSelected = { screen ->
                            navController.navigate(route = screen.name){
                                popUpTo(currentScreen.name){
                                    inclusive = true
                                }
                            }
                        },
                        currentScreen = currentScreen
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    MusicNavHost(
                        navController,
                        modifier = Modifier.padding(bottom = 64.dp)
                    )
                    HomeBottomBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onBottomBarClick = {
                            showBottomSheet(
                                bottomSheetScaffoldState = bottomSheetScaffoldState,
                                coroutineScope = coroutineScope
                            )
                        })
                }
            }
        }
    }
}


@Composable
fun MusicNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MusicScreen.Library.name,
        modifier = modifier
    ) {
        composable(MusicScreen.Library.name) {
            LibraryScreen()
        }
        composable(MusicScreen.Albums.name) {
            AlbumScreen()
        }
        composable(MusicScreen.Playlists.name) {
            PlaylistScreen()
        }
    }
}

@Composable
private fun SetStatusColor() {
    val systemUiController = rememberSystemUiController()
    val primaryColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer
    val darkTheme = isSystemInDarkTheme()

    systemUiController.setStatusBarColor(
        color = primaryColor,
        darkIcons = !darkTheme
    )
    systemUiController.setNavigationBarColor(
        color = androidx.compose.material3.MaterialTheme.colorScheme.surface
    )
}

@OptIn(ExperimentalMaterialApi::class)
fun showBottomSheet(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {

        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
            bottomSheetScaffoldState.bottomSheetState.expand()
        } else {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }
}
