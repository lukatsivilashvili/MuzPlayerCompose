package com.example.muzplayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.muzplayer.common.MusicScreen
import com.example.muzplayer.presentation.components.BottomNavigationBar
import com.example.muzplayer.presentation.ui.bottom_bar.HomeBottomBar
import com.example.muzplayer.presentation.ui.home_screen.HomeBody
import com.example.muzplayer.presentation.ui.library_screen.LibraryBody
import com.example.muzplayer.presentation.ui.player_screen.PlayerScreen
import com.example.muzplayer.presentation.ui.playlist_screen.PlaylistBody
import com.example.muzplayer.presentation.ui.theme.MuzPlayerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzPlayerTheme {
                setStatusColor()
                MainActivityScreen(backPressedDispatcher = onBackPressedDispatcher)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(
    backPressedDispatcher: OnBackPressedDispatcher
) {
    val allScreens = MusicScreen.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = MusicScreen.fromRoute(backstackEntry.value?.destination?.route)

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    fun showBottomSheet() {
        coroutineScope.launch {

            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            } else {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
    ) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                PlayerScreen(
                    backPressedDispatcher = backPressedDispatcher,
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
                            navController.navigate(screen.name)
                        },
                        currentScreen = currentScreen
                    )
                },
                topBar = {
                    var expanded by remember { mutableStateOf(false) }
                    var text by rememberSaveable { mutableStateOf("") }

                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(end = 24.dp),
                                    text = currentScreen.name,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    softWrap = false,
                                    maxLines = 1,
                                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                                )

                                AnimatedVisibility(visible = expanded) {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp),
                                        singleLine = true,
                                        value = text,
                                        onValueChange = { text = it },
                                        textStyle = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                        },
                        actions = {
                            AnimatedVisibility(visible = currentScreen.name == "Library") {
                                IconButton(
                                    modifier = Modifier.padding(end = 16.dp, start = 24.dp),
                                    onClick = {
                                        expanded = !expanded
                                    }) {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Search"
                                    )
                                }
                            }
                        },
                        windowInsets = TopAppBarDefaults.windowInsets,
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceColorAtElevation(
                                3.dp
                            ),
                            navigationIconContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                            titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                            actionIconContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer

                        ),
                        scrollBehavior = scrollBehavior
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    MusicNavHost(navController, modifier = Modifier.padding(bottom = 64.dp))
                    HomeBottomBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onBottomBarClick = { showBottomSheet() })
                }
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
