package com.example.muzplayer.presentation.ui.library_screen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.common.MusicScreen
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.SinglePermission
import com.example.muzplayer.presentation.components.music_item.MusicItem
import com.example.muzplayer.presentation.components.search_textfield.TextFieldWithoutPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val songs = viewModel.songItems.collectAsState().value
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentScreen = MusicScreen.Library
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            var expanded by remember { mutableStateOf(false) }
            var text by rememberSaveable { mutableStateOf("") }

            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Text(
                            text = currentScreen.name,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold,
                            softWrap = false,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleLarge
                        )

                        AnimatedVisibility(visible = expanded) {
                            TextFieldWithoutPadding(
                                modifier = Modifier.padding(start = 24.dp),
                                placeholder = "Search",
                                value = text,
                                onImeSearch = {
                                    val newIndex = viewModel.searchSong(songs, text)
                                    coroutineScope.launch {
                                        listState.scrollToItem(newIndex)
                                    }

                                },
                                onValueChange = {
                                    text = it
                                }
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
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        3.dp
                    ),
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer

                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
        ) {
            SinglePermission()
            HomeContent(
                data = songs,
                viewModel = viewModel,
                listState = listState,
            )
        }
    }
}

@Composable
fun HomeContent(
    data: List<Song>,
    viewModel: LibraryViewModel? = null,
    listState: LazyListState? = null,
) {
    if (listState != null) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(data) { musicItem ->
                MusicItem(music = musicItem, viewModel = viewModel)
            }
        }
    }
}