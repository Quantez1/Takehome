package com.amada.takehome.utils

/**
 * The state of the API call provided by a repository for for consumption by the ViewModel.
 *
 * @param PayloadType the type of the data being returned for Success
 */
sealed class ApiResponse<out PayloadType> {
    data class Success<PayloadType>(
        val payload: PayloadType,
    ) : ApiResponse<PayloadType>()

    data class Failed<PayloadType>(
        val error: String,
    ) : ApiResponse<PayloadType>()
}
