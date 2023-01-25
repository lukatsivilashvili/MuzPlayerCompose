
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.common.MusicScreen
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.presentation.components.album_item.AlbumItem
import com.example.muzplayer.presentation.ui.album_screen.AlbumScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    viewModel: AlbumScreenViewModel = hiltViewModel(),
) {
    val songs = viewModel.albumItems.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val currentScreen = MusicScreen.Albums
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
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
            AlbumScreenContent(
                data = songs.value,
            )
        }
    }
}

@Composable
fun AlbumScreenContent(
    data: List<Album>,
    viewModel: AlbumScreenViewModel = hiltViewModel()
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        columns = GridCells.Adaptive(minSize = 200.dp)
    ) {
        items(data) { musicItem ->
            AlbumItem(album = musicItem, viewModel = viewModel)
        }
    }
}