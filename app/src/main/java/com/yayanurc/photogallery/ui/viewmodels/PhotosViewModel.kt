package com.yayanurc.photogallery.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yayanurc.photogallery.data.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: PhotosRepository,
    state: SavedStateHandle, // allows save pieces of data through process death amd then restore it afterward
) : ViewModel() {

    val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "android"
        private const val CURRENT_QUERY = "current_query"
    }
}