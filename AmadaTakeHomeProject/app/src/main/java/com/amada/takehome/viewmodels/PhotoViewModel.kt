package com.amada.takehome.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amada.takehome.R
import com.amada.takehome.models.Photo
import com.amada.takehome.repositories.PhotoRepository
import com.amada.takehome.utils.ApiResponse
import com.amada.takehome.utils.ResponseState
import com.amada.takehome.utils.ResponseState.InitialState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {

    // TODO Extract Paginator into it's own class

    enum class FlickrFunction {
        FLICKR_FUNCTION_NONE,
        FLICKR_FUNCTION_RECENT,
        FLICKR_FUNCTION_SEARCH,
    }

    init {
        resetPagination()
    }

    private var currentPage = 1
    private var totalPages = 0
    private var lastFlickrFunction = FlickrFunction.FLICKR_FUNCTION_NONE
    private var photoList: MutableList<Photo> = mutableListOf()
    private var lastSearchText = ""

    private fun canPaginate(): Boolean {
        return (lastFlickrFunction != FlickrFunction.FLICKR_FUNCTION_NONE)
                && currentPage < totalPages
    }

    private fun resetPagination() {
        currentPage = 1
        totalPages = 0
        lastFlickrFunction = FlickrFunction.FLICKR_FUNCTION_NONE
        lastSearchText = ""
        photoList = mutableListOf()
    }

    private fun updatePagination(
        photos: List<Photo>,
        total: Int,
        flickrFunction: FlickrFunction,
        searchText: String? = null
    ) {
        if (currentPage == 1) {
            photoList = photos.toMutableList()
        } else {
            photoList += photos
        }
        totalPages = total
        currentPage += 1
        lastFlickrFunction = flickrFunction
        lastSearchText = searchText ?: lastSearchText
    }

    private val photoRepository = PhotoRepository()

    private val mutablePhotosFlow =
        MutableStateFlow<ResponseState<List<Photo>>>(InitialState())
    val photosFlow: StateFlow<ResponseState<List<Photo>>>
        get() = mutablePhotosFlow.asStateFlow()

    fun fetchRecentPhotos(page: Int = 1) {
        if (page == 1) {
            resetPagination()
        }

        viewModelScope.launch {
            mutablePhotosFlow.value = ResponseState.Loading()
            when (val response = photoRepository.getRecentPhotos(page)) {
                is ApiResponse.Failed -> {
                    mutablePhotosFlow.value = ResponseState.Failed(R.string.flickr_err)
                    Log.e("Flickr", "Unable to retrieve Flickr images")
                }

                is ApiResponse.Success -> {
                    if (response.payload.stat == "ok") {
                        with(response.payload.photos) {
                            updatePagination(photo, pages, FlickrFunction.FLICKR_FUNCTION_RECENT)
                        }
                        mutablePhotosFlow.value = ResponseState.Success(photoList)
                    } else {
                        mutablePhotosFlow.value = ResponseState.Failed(R.string.flickr_err)
                        with(response.payload) {
                            Log.e(
                                "Flickr",
                                "Unable to retrieve Flickr images, code = $code, message = $message"
                            )
                        }
                    }
                }
            }
        }
    }

    fun fetchSearchPhotos(
        searchText: String,
        page: Int = 1
    ) {
        viewModelScope.launch {
            if (page == 1) {
                resetPagination()
            }
            resetPagination()
            mutablePhotosFlow.value = ResponseState.Loading()
            when (val response = photoRepository.searchPhotos(searchText, page)) {
                is ApiResponse.Failed -> {
                    mutablePhotosFlow.value = ResponseState.Failed(R.string.flickr_err)
                }
                is ApiResponse.Success -> {
                    if (response.payload.stat == "ok") {
                        with(response.payload.photos) {
                            updatePagination(
                                photos = photo,
                                total = pages,
                                flickrFunction = FlickrFunction.FLICKR_FUNCTION_RECENT,
                                searchText = searchText
                            )
                        }
                        mutablePhotosFlow.value = ResponseState.Success(photoList)
                    } else {
                        mutablePhotosFlow.value = ResponseState.Failed(R.string.flickr_err)
                    }
                }
            }
        }
    }

    fun fetchNextPage() {
        if (!canPaginate()) return

        when (lastFlickrFunction) {
            FlickrFunction.FLICKR_FUNCTION_RECENT -> {
                fetchRecentPhotos(currentPage)
            }

            FlickrFunction.FLICKR_FUNCTION_SEARCH -> {
                fetchSearchPhotos(lastSearchText, currentPage)
            }

            else -> {}
        }
    }

}