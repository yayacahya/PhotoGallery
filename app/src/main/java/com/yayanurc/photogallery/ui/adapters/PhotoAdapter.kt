package com.yayanurc.photogallery.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yayanurc.photogallery.R
import com.yayanurc.photogallery.models.Photo
import com.yayanurc.photogallery.databinding.ListViewItemBinding


class PhotoAdapter(val onPhotoSelected: (photo: Photo, position: Int) -> Unit) :
    PagingDataAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PhotoComparator) { // PagingDataAdapter -> knows how to handle paging data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(
            ListViewItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PhotoViewHolder(private val binding: ListViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            binding.apply {
                // Glide - to load images
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .error(R.drawable.ic_error)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPhoto)

                textViewUsername.text = photo.user.name
                if (photo.description == null) {
                    val noDescription = "No description"
                    textViewDescription.text = noDescription
                } else {
                    textViewDescription.text = photo.description
                }

                root.setOnClickListener {
                    onPhotoSelected(photo, absoluteAdapterPosition)
                }
            }
        }
    }

    object PhotoComparator : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem == newItem
    }
}