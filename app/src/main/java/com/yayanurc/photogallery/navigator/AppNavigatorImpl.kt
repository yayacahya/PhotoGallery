package com.yayanurc.photogallery.navigator

import androidx.fragment.app.FragmentActivity
import com.yayanurc.photogallery.R
import com.yayanurc.photogallery.models.Photo
import com.yayanurc.photogallery.ui.fragments.PhotoDetailFragment
import com.yayanurc.photogallery.ui.fragments.PhotosFragment
import com.yayanurc.photogallery.utils.fragmentByTagOrNew
import com.yayanurc.photogallery.utils.withArgs
import javax.inject.Inject

/**
 * Navigator implementation.
 */
class AppNavigatorImpl @Inject constructor(private val activity: FragmentActivity) : AppNavigator {

    override fun navigateTo(screen: Screens, anything: Any?) {
        val photoDetailFragment by activity.fragmentByTagOrNew(PhotoDetailFragment::class.java.canonicalName!!) {
            PhotoDetailFragment().withArgs {
                putParcelable(PhotoDetailFragment.PHOTO_TEXT, anything as Photo)
            }
        }

        val fragment = when (screen) {
            Screens.PHOTOS -> PhotosFragment()
            Screens.PHOTO_DETAIL -> photoDetailFragment
        }

        if (!fragment.isAdded) {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null) //fragment::class.java.canonicalName)
                .commit()
        }
    }
}