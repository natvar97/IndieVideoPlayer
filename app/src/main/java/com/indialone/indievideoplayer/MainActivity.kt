package com.indialone.indievideoplayer

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.indialone.indievideoplayer.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File

class MainActivity : AppCompatActivity(), OnSelectListener {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var videoFiles: ArrayList<File>
    private lateinit var videoRvAdapter: VideoRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        askPermission()

    }

    private fun askPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    displayVideoFiles()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@MainActivity,
                        "Permission is Required to Proceed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }

            }).check()
    }

    private fun findVideoFiles(file: File): ArrayList<File> {
        val myVideos = ArrayList<File>()

        val files = file.listFiles()

        if (files != null) {

            for (videoFile in files) {
                if (videoFile.isDirectory && !videoFile.isHidden) {
                    myVideos.addAll(findVideoFiles(videoFile))
                } else {
                    if (videoFile.name.endsWith(".mp4")
                        || videoFile.name.endsWith(".mkv")
                        || videoFile.name.endsWith(".mp3")
                        || videoFile.name.endsWith(".wav")
                    ) {
                        myVideos.add(videoFile)
                    }
                }
            }
        }
        return myVideos
    }

    private fun displayVideoFiles() {
        videoFiles = ArrayList()
        videoFiles.addAll(findVideoFiles(Environment.getExternalStorageDirectory()))
        videoRvAdapter = VideoRvAdapter(this, videoFiles, this)
        videoRvAdapter.setHasStableIds(true)
        mBinding.rvVideos.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            setHasFixedSize(true)
            adapter = videoRvAdapter
        }
    }

    override fun onFileClicked(file: File) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("video", file.absolutePath)
        startActivity(intent)
    }

}