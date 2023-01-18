package com.yayanurc.photogallery.navigator

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.yayanurc.photogallery.R
import com.yayanurc.photogallery.ui.fragments.PhotoDetailFragment
import com.yayanurc.photogallery.ui.fragments.PhotosFragment
import javax.inject.Inject

/**
 * Navigator implementation.
 */
class AppNavigatorImpl @Inject constructor(private val activity: FragmentActivity) : AppNavigator {

    override fun navigateTo(screen: Screens, title: String, bundle: Bundle?) {
        val fragment = when (screen) {
            Screens.PHOTOS -> PhotosFragment(title)
            Screens.PHOTO_DETAIL -> PhotoDetailFragment(title)
        }
        if (bundle != null) {
            fragment.arguments = bundle
        }
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null) //fragment::class.java.canonicalName)
            .commit()
    }
}