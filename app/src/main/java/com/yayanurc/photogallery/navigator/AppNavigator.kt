package com.yayanurc.photogallery.navigator

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
    fun navigateTo(screen: Screens, anything: Any? = null)
}