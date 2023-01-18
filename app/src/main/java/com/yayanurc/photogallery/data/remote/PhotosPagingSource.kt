package com.yayanurc.photogallery.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yayanurc.photogallery.api.PhotosApi
import com.yayanurc.photogallery.models.Photo
import retrofit2.HttpException
import java.io.IOException

class PhotosPagingSource(
    private val photosApi: PhotosApi,
    private val query: String
) : PagingSource<Int, Photo>() {

    companion object {
        private const val PHOTOS_STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: PHOTOS_STARTING_PAGE_INDEX

        return try {
            val response = photosApi.getPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == PHOTOS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}