
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.presentation.components.album_item.AlbumItem
import com.example.muzplayer.presentation.ui.album_screen.AlbumScreenViewModel

@Composable
fun AlbumScreen(
    viewModel: AlbumScreenViewModel = hiltViewModel(),
) {
    val songs = viewModel.albumItems.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        AlbumScreenContent(data = songs.value,)
    }
}

@Composable
fun AlbumScreenContent(
    data: List<Album>,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        columns = GridCells.Adaptive(minSize = 200.dp)
    ) {
        items(data) { musicItem ->
            AlbumItem(album = musicItem)
        }
    }
}