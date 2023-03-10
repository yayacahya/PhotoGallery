package com.yayanurc.photogallery.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yayanurc.photogallery.R
import com.yayanurc.photogallery.models.Photo
import com.yayanurc.photogallery.databinding.FragmentPhotoDetailBinding
import com.yayanurc.photogallery.di.GlideApp
import com.yayanurc.photogallery.ui.MainActivity
import com.yayanurc.photogallery.utils.argument
import dagger.hilt.android.AndroidEntryPoint


/**
 * Fragment that displays photo detail.
 * feature : ZoomIn-ZoomOut by Pinching Finger and Double Tap
 */
@AndroidEntryPoint
class PhotoDetailFragment : Fragment(R.layout.fragment_photo_detail) {

    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!

    private val photo by argument<Parcelable>(PHOTO_TEXT)

    companion object {
        const val PHOTO_TEXT = "photo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = getString(R.string.label_photo_detail)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        _binding = FragmentPhotoDetailBinding.bind(view)

        binding.apply {

            GlideApp.with(this@PhotoDetailFragment)
                .load((photo as Photo).urls.regular)
                .error(R.drawable.ic_error)
                .centerInside()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }
                })
                .into(ivPhotoDetail)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}