package com.amada.takehome.api

import com.amada.takehome.models.PhotoResultsModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Flickr API endpoints involving photos
 *
 *  Documentation:
 *
 *  https://www.flickr.com/services/api/flickr.photos.getRecent.html
 */
interface PhotoApi {
    @GET("rest")
    suspend fun getPhotos(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("text") searchText: String? = null,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: String,
        @Query("per_page") perPage: Int,
   ) : PhotoResultsModel
}