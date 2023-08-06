package com.amada.takehome.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * This object is specifically for the Flickr URL for the Takehome project.
 *
 */
object  RetrofitInstance {
    private const val BASE_URL = "https://www.flickr.com/services/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val createPhotoApi: PhotoApi = retrofit.create(PhotoApi::class.java)
}
