package com.yayanurc.photogallery.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
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
import com.yayanurc.photogallery.utils.parcelable
import dagger.hilt.android.AndroidEntryPoint


/**
 * Fragment that displays photo detail.
 * feature : ZoomIn-ZoomOut by Pinching Finger and Double Tap
 */
@AndroidEntryPoint
class PhotoDetailFragment(private val titleName: String) : Fragment(R.layout.fragment_photo_detail) {

    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = titleName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        _binding = FragmentPhotoDetailBinding.bind(view)

        binding.apply {
            val photo = arguments?.parcelable<Photo>("photo")

            GlideApp.with(this@PhotoDetailFragment)
                .load(photo?.urls?.regular)
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