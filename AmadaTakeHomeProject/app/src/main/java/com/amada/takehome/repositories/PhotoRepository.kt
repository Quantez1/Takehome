package com.amada.takehome.repositories

import com.amada.takehome.api.RetrofitInstance
import com.amada.takehome.models.PhotoResultsModel
import com.amada.takehome.utils.ApiResponse
import com.amada.takehome.utils.ApiResponse.Failed
import com.amada.takehome.utils.ApiResponse.Success

class PhotoRepository {
    private val photoApi = RetrofitInstance.createPhotoApi

    suspend fun getRecentPhotos(page: Int): ApiResponse<PhotoResultsModel> {
        return try {
            val flickrPhotos = photoApi.getPhotos(
                method = "flickr.photos.getRecent",
                apiKey = API_KEY,
                page = page,
                format = "json",
                noJsonCallback = "1",
                perPage = 30
            )
            Success(flickrPhotos)
        } catch (e: Exception) {
            Failed(e.message ?: "No error message available")
        }
    }

    suspend fun searchPhotos(
        searchText: String,
        page: Int
    ): ApiResponse<PhotoResultsModel> {
        return try {
            val flickrPhotos = photoApi.getPhotos(
                method = "flickr.photos.search",
                apiKey = API_KEY,
                page = page,
                searchText = searchText,
                format = "json",
                noJsonCallback = "1",
                perPage = 100
            )
            Success(flickrPhotos)
        } catch (e: Exception) {
            Failed(e.message ?: "No error message available")
        }
    }

    companion object {
        const val API_KEY = "a0222db495999c951dc33702500fdc4d"
    }
}


