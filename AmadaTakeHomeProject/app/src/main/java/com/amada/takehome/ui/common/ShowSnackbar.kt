package com.amada.takehome.ui.common

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Displays a message in a snackbar and auto dismisses.  Call within the context of a Scaffold.
 */
@Composable
fun ShowSnackbar(
    message: String,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    LaunchedEffect(null) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(message = message)
        }
    }
}
