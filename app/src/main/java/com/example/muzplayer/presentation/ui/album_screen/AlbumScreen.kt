import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.presentation.components.album_item.AlbumItem
import com.example.muzplayer.presentation.ui.album_screen.AlbumScreenViewModel

@Composable
fun AlbumScreen(
    viewModel: AlbumScreenViewModel = hiltViewModel(),
    listState: LazyListState
) {
    val albums = viewModel.albumItems.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        AlbumsContent(data = albums.value, viewModel = viewModel, listState = listState)
    }
}

@Composable
fun AlbumsContent(
    data: List<Album>,
    viewModel: AlbumScreenViewModel,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(data) { musicItem ->
            AlbumItem(album = musicItem)
        }
    }
}