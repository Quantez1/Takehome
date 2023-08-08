package com.amada.takehome.utils

import com.amada.takehome.utils.ResponseState.Failed
import com.amada.takehome.utils.ResponseState.InitialState
import com.amada.takehome.utils.ResponseState.Loading
import com.amada.takehome.utils.ResponseState.Success


/**
 * The state of the API call provided by a ViewModel for for consumption by the UI.
 *
 * @param PayloadType the type of the data being returned for Success
 */
sealed class ResponseState<out PayloadType> {
    /**
     * This is for state flows, which require an initial state.
     */
    data class InitialState(val nothing: Nothing? = null) : ResponseState<Nothing>()

    /**
     * The requested data has begun and hasn't yet completed loading.
     */
    data class Loading(val nothing: Nothing? = null) : ResponseState<Nothing>()

    /**
     * The requested data has successfully completed loading.
     *
     * @property payload – The data you wanted to load
     */
    data class Success<PayloadType>(
        val payload: PayloadType,
    ) : ResponseState<PayloadType>()

    /**
     * The attempt to load data was unsuccessful
     */
    data class Failed<PayloadType>(
        val errorMessageResId: Int = -1,
    ) : ResponseState<PayloadType>()
}

/**
 * The map{ } for a successful fetch, the single parameter returns a PayloadType type.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, U> ResponseState<T>.map(f: (T) -> U): ResponseState<U> = when (this) {
    is Loading -> this
    is Success<T> -> Success(f(payload))
    is Failed<*> -> (this as ResponseState<U>)
    is InitialState -> this
}

/**
 * The mapFailure{ } for an unsuccessful fetch, the single parameter returns a string resource ID
 * for the error message to display.
 */
inline fun <T, U> ResponseState<T>.mapFailure(f: (errorMessage: Int) -> U): ResponseState<T> = when (this) {
    is Loading -> this
    is Success<T> -> this
    is Failed<*> -> {
        f(errorMessageResId); this
    }
    is InitialState -> this
}

inline fun <T> ResponseState<T>.mapLoading(f: (Unit) -> Unit): ResponseState<T> = when (this) {
    is Loading -> {
        f(Unit); this
    }
    is Success -> this
    is Failed<*> -> this
    is InitialState -> this
}

enum class LastEvent {
    NONE, MAP, MAP_LOADING, MAP_FAILURE
}
