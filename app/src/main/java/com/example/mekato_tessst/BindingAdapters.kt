package com.example.mekato_tessst

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache images for better performance
                .placeholder(R.drawable.cart_blue) // Optional placeholder
                .error(R.drawable.error64)
                .into(view)
        } else {
            view.setImageResource(R.drawable.add) // Fallback if URL is null
        }
    }
}

/*


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache images for better performance
            .placeholder(R.drawable.placeholder) // Optional placeholder
            .into(view)
    } else {
        view.setImageResource(R.drawable.placeholder) // Fallback if URL is null
    }
}

 */