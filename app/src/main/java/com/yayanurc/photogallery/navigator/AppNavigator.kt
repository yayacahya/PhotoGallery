package com.yayanurc.photogallery.navigator

import android.os.Bundle

/**
 * Available screens.
 */
enum class Screens {
    PHOTOS,
    PHOTO_DETAIL
}

/**
 * Interfaces that defines an app navigator.
 */
interface AppNavigator {
    // Navigate to a given screen.
    fun navigateTo(screen: Screens, title: String, bundle: Bundle? = null)
}