package com.amada.takehome.models


/**
 * Transport for adding photos
 * @param photos The photos themselves
 * @param clearPhotos indicates whether to clear the existing photos or retain them.
 */
data class PhotosModel(
    val photos: List<Photo>,
    val clearPhotos: Boolean
)