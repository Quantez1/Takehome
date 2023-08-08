package com.amada.takehome.utils

import androidx.compose.foundation.lazy.grid.LazyGridState

fun LazyGridState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1