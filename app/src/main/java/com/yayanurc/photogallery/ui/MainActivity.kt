package com.yayanurc.photogallery.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import com.yayanurc.photogallery.R
import com.yayanurc.photogallery.navigator.AppNavigator
import com.yayanurc.photogallery.navigator.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@BuildCompat.PrereleaseSdkCheck /**
 * Main activity of the application.
 *
 * Container for the Photos & Photo Detail fragments.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            navigator.navigateTo(Screens.PHOTOS, getString(R.string.app_name))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }
}