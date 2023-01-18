package com.yayanurc.photogallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.yayanurc.photogallery.api.PhotosApi
import com.yayanurc.photogallery.data.remote.PhotosPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepository @Inject constructor(private val photosApi: PhotosApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotosPagingSource(photosApi, query) }
        ).liveData
}