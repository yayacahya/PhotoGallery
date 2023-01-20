package com.yayanurc.photogallery.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

fun <T : RecyclerView> T.removeItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}

inline fun <reified T : Fragment> FragmentActivity.fragmentByTagOrNew(
    tag: String, noinline factory: () -> T
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        supportFragmentManager.findFragmentByTag(tag) as? T ?: factory()
    }
}

inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T {
    return this.apply {
        arguments = Bundle().apply(argsBuilder)
    }
}

inline fun <reified T : Any?> Fragment.argument(key: String): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) {
        arguments?.let { bundle ->
            when {
                SDK_INT >= 33 -> {
                    when {
                        T::class.java.isAssignableFrom(String::class.java) -> bundle.getString(key) // as T
                        T::class.java.isAssignableFrom(Parcelable::class.java) -> {
                            bundle.getParcelable(key, T::class.java) // as T
                        }
                        else -> null
                    }
                } else -> @Suppress("DEPRECATION") bundle.get(key)
            }
        } as T
    }