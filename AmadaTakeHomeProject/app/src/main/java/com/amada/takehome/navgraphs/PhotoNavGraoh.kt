package com.amada.takehome.navgraphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amada.takehome.models.Photo
import com.amada.takehome.ui.PopulatePhotoDetail
import com.amada.takehome.ui.PopulatePhotoScreen

@Composable
fun PhotoNavGraph(
) {
    val navHostController = rememberNavController()
    val photoState = remember { mutableStateOf<Photo?>(null) }

    NavHost(
        navController = navHostController,
        startDestination = PhotoScreen.PhotosScreen.route
    ) {
        composable(route = PhotoScreen.PhotosScreen.route) {
            PopulatePhotoScreen(
                onPhotoClicked = { photo ->
                    photoState.value = photo
                    navHostController.navigate(PhotoScreen.PhotoDetailScreen.route)
                }
            )
        }
        composable(route = PhotoScreen.PhotoDetailScreen.route) {
            PopulatePhotoDetail(photoState.value)
        }
    }
}

sealed class PhotoScreen(val route: String) {
    object PhotosScreen : PhotoScreen(route = "photos_screen")
    object PhotoDetailScreen : PhotoScreen(route = "photo_detail_screen")
}