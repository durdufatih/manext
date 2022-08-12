package com.fatihdurdu.manext.viewmodel.state

import com.fatihdurdu.manext.data.ImageResponseList


sealed class ListState {
    object Empty : ListState()
    object Refresh : ListState()
    object Loading : ListState()
    class Success(val data: ImageResponseList) : ListState()
    class Error(val message: String) : ListState()
}
