package com.indialone.indievideoplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Clock
import com.indialone.indievideoplayer.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityVideoPlayerBinding
    private var videoPath = ""
    private lateinit var mVideoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mBinding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra("video")) {
            videoPath = intent.getStringExtra("video")!!
        }

        val videoUri = Uri.parse(videoPath)

        mVideoPlayer = SimpleExoPlayer.Builder(this).build()
        mBinding.videoPlayer.player = mVideoPlayer
        mVideoPlayer.apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
            clock
            textComponent
            addMediaItem(MediaItem.fromUri(videoUri))
        }
        mVideoPlayer.prepare()

    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoPlayer.pause()
    }

    override fun onPause() {
        super.onPause()
        if (videoPath.endsWith(".mp4") || videoPath.endsWith(".mkv")) {
            mVideoPlayer.pause()
        }
    }

}