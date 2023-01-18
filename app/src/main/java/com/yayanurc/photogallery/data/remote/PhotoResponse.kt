package com.yayanurc.photogallery.data.remote

import com.yayanurc.photogallery.models.Photo

data class PhotoResponse(
    val results: List<Photo>
)