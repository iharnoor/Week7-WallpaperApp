package com.example.wallpaperempty.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaperempty.R
import com.example.wallpaperempty.domain.entities.PicsumWallpaper


class WallpaperAdapter(
    private val wallpaperList: List<PicsumWallpaper>, private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.wallpaper_item, parent, false)
        return WallpaperViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val currentItem = wallpaperList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return wallpaperList.size
    }

    inner class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: AppCompatImageView = itemView.findViewById(R.id.wallpaper_image_view)

        fun bind(currentItem: PicsumWallpaper) {
            Glide.with(imageView.context).load(currentItem.url).into(imageView)
            imageView.setOnClickListener {
                onItemClick(currentItem.url)
            }
        }
    }
}
