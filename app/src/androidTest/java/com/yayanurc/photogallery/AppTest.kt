package com.yayanurc.photogallery

import android.view.KeyEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yayanurc.photogallery.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AppTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun runAppTest() {
        ActivityScenario.launch(MainActivity::class.java)

        // Check Switch View Menu is displayed
        onView(withId(R.id.menu_switch_view)).check(matches(isDisplayed()))

        // Check Search Menu is displayed
        onView(withId(R.id.menu_search)).check(matches(isDisplayed()))

        // Tap Switch Menu
        onView(withId(R.id.menu_switch_view)).perform(click())

        // Check Data List is still displayed
        onView(withId(R.id.rv_photos)).check(matches(isDisplayed()))

        // Tap Switch Menu again
        onView(withId(R.id.menu_switch_view)).perform(click())

        // Check Data List is still displayed
        onView(withId(R.id.rv_photos)).check(matches(isDisplayed()))

        // Open The SearchView
        onView(withId(R.id.menu_search)).perform(click())

        // Clear text on SearchVew
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(clearText())

        // Type text on SearchVew
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText("mango"))

        // Hit Enter Key
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        // Check Data List is still displayed
        onView(withId(R.id.rv_photos)).check(matches(isDisplayed()))

        // Navigate to Photo Detail screen by tapping on first Item from Data List
        onView(withId(R.id.rv_photos))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Check Photo on Photo Detail Fragment is displayed
        onView(withId(R.id.iv_photo_detail)).check(matches(isDisplayed()))

        // Do Double Tap on Photo to Zoom
        onView(withId(R.id.iv_photo_detail)).perform(doubleClick())

        // Check Photo on Photo Detail Fragment is still displayed
        onView(withId(R.id.iv_photo_detail)).check(matches(isDisplayed()))
    }
}