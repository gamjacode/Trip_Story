package com.example.trip_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_button1.setOnClickListener{
        startActivity(Intent(this,Login::class.java))
        }

        main_button2.setOnClickListener{
            startActivity(Intent(this,Sign_up::class.java))
        }

    }
}
