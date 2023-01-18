package com.yayanurc.photogallery.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val description: String?,
    val likes: Int,
    val color: String,
    val urls: PhotoUrl,
    val user: PhotoUser
) : Parcelable {

    @Parcelize
    data class PhotoUrl(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

    @Parcelize
    data class PhotoUser(
        val name: String,
        val username: String
    ) : Parcelable {
        // Line bellow is because unsplash.com requires to add analytics metadata
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=PhotoFromApi&utm_medium=referral"
    }
}