package com.fatihdurdu.manext.network

import com.fatihdurdu.manext.data.ImageResponseList
import com.fatihdurdu.manext.util.Const
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {

    @GET(Const.LIST_URL)
    suspend fun getAllImages(@Query(value = "client_id") clientId: String = Const.API_KEY): ImageResponseList

}