package com.fatihdurdu.manext.repository

import com.fatihdurdu.manext.data.ImageResponseList
import com.fatihdurdu.manext.network.UnsplashApiService
import javax.inject.Inject

class ListAllImageRepository @Inject constructor(private val unsplashApiService: UnsplashApiService) {

    suspend fun getAllImages(): ImageResponseList = unsplashApiService.getAllImages()
}