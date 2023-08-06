package com.amada.takehome.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.amada.takehome.R
import com.amada.takehome.models.Photo
import com.amada.takehome.ui.common.ProgressIndicator
import com.amada.takehome.ui.common.ShowSnackbar
import com.amada.takehome.utils.FlickrSize
import com.amada.takehome.utils.createFlickrImageUrl
import com.amada.takehome.utils.map
import com.amada.takehome.utils.mapFailure
import com.amada.takehome.utils.mapLoading
import com.amada.takehome.viewmodels.PhotoViewModel

/**
 * This is the compose preview for the photo screen
 */
// TODO
@Preview
@Composable
fun PhotoScreenPreview() {
}

/**
 * This composable interacts with the View Model and other data sources and populates the
 * PhotoScreen, and handles its hoisted data.
 */
@Composable
fun PopulatePhotoScreen() {
    val isLoading = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }
    var errorMessage: String? = null
    var photos: List<Photo>? = null

    val photoViewModel = viewModel<PhotoViewModel>()
        remember { PhotoViewModel() }
    val photoEvent by photoViewModel.photosFlow.collectAsStateWithLifecycle()
    photoEvent
        .mapLoading {
            isLoading.value = true
            errorMessage = null
            photos = null
        }
        .map {
            isLoading.value = false
            errorMessage = null
            photos = it
        }
        .mapFailure {
            isLoading.value = false
            errorMessage = stringResource(id = it)
            photos = null
        }

    PhotosScreen(
        searchTextLabel = stringResource(R.string.search),
        enterSearchTextLabel = stringResource(R.string.enter_search_text),
        flickrDescriptionLabel = stringResource(R.string.flickr_image_description),
        isLoading = isLoading.value,
        errorMessage = errorMessage,
        photos = photos,
        buttonPressed = {
            if (searchText.value.isEmpty()) {
                photoViewModel.fetchRecentPhotos()
            } else {
                photoViewModel.fetchSearchPhotos(searchText = searchText.value)
            }
        },
        searchText = searchText.value,
        updateSearchText = { searchText.value = it },
        paginate = { photoViewModel.fetchNextPage() }
    )
}

/**
 * This is the stateless composable for the PhotoScreen.
 */
@Composable
fun PhotosScreen(
    searchTextLabel: String,
    enterSearchTextLabel: String,
    flickrDescriptionLabel: String,
    isLoading: Boolean,
    errorMessage: String?,
    photos: List<Photo>?,
    searchText: String,
    updateSearchText: (searchText: String) -> Unit,
    buttonPressed: () -> Unit,
    paginate: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {},
    ) { contentPadding ->
        if (isLoading) {
            ProgressIndicator()
        }

        errorMessage?.let { message ->
            ShowSnackbar(
                message = message,
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        }

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            TextField(
                value = searchText,
                singleLine = true,
                onValueChange = { updateSearchText(it) },
                placeholder = { Text(text = enterSearchTextLabel) },
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
            )
            Button(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
                onClick = { buttonPressed() }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = searchTextLabel
                )
            }
            photos?.let { photos ->
                val lazyGridListState = rememberLazyGridState()

                val shouldStartPaginate: State<Boolean> = remember {
                    derivedStateOf {
                        (lazyGridListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >= (lazyGridListState.layoutInfo.totalItemsCount - 6)
                    }
                }
                if (shouldStartPaginate.value) {
                    paginate()
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    state = lazyGridListState,
                ) {
                    photos.forEach { photo ->
                        item {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                model = createFlickrImageUrl(
                                    serverId = photo.server,
                                    photoId = photo.id,
                                    secret = photo.secret,
                                    size = FlickrSize.THUMBNAIL_150
                                ),
                                contentDescription = flickrDescriptionLabel
                            )
                        }
                    }
                }
            }
        }
    }
}

