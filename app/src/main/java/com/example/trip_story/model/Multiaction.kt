package com.example.trip_story.model

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.trip_story.R
import kotlinx.android.synthetic.main.activity_multiaction.*

class Multiaction : AppCompatActivity() {
    var pick_from_Multi_album = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiaction)

        Multi_Album_Button.setOnClickListener{
            doTakeMultiAlbumAction()
        }
    }

    fun doTakeMultiAlbumAction() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,pick_from_Multi_album)

    }
}