package com.indialone.indievideoplayer

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.indialone.indievideoplayer.databinding.ListItemLayoutBinding
import java.io.File

class VideoRvAdapter(
    private val context: Context,
    private val videoFiles: ArrayList<File>,
    private val listener: OnSelectListener
) : RecyclerView.Adapter<VideoRvAdapter.VideoRvViewHolder>() {
    class VideoRvViewHolder(itemView: ListItemLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val tvTitle = itemView.tvTitle
        val ivThumbnail = itemView.ivThumbnail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoRvViewHolder {
        val view = ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoRvViewHolder, position: Int) {
        holder.tvTitle.text = videoFiles[position].name
        holder.tvTitle.isSelected = true

        val thumbnail: Bitmap? = ThumbnailUtils.createVideoThumbnail(
            videoFiles[position].absolutePath,
            MediaStore.Images.Thumbnails.MINI_KIND
        )
        if (videoFiles[position].name.endsWith(".mp3") || videoFiles[position].name.endsWith(".wav")) {
            holder.ivThumbnail.setImageResource(R.drawable.icon_music_file)
            holder.ivThumbnail.scaleType = ImageView.ScaleType.FIT_CENTER
        } else {
            holder.ivThumbnail.setImageBitmap(thumbnail)
        }
        holder.itemView.setOnClickListener {
            listener.onFileClicked(videoFiles[position])
        }
    }

    override fun getItemCount(): Int {
        return videoFiles.size
    }
}