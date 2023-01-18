package com.yayanurc.photogallery.api

import com.yayanurc.photogallery.BuildConfig
import com.yayanurc.photogallery.data.remote.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Check documentation:
 * https://unsplash.com/documentation#location
 */
interface PhotosApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"

        // This will be automatically generated and contain API key which has been added in gradle.properties
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    // https://unsplash.com/documentation#list-photos
    @Headers(
        "Accept-Version: v1",
        "Authorization: Client-ID $CLIENT_ID"
    ) // v1: metadata this API expects, Client-ID: access key
    @GET("search/photos")
    suspend fun getPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PhotoResponse
}