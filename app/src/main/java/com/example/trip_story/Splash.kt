package com.example.trip_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {
    val SPLASH_VIEW_TIME:Long = 2000    //2초후 넘어감
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({         //2초이후 어디로 갈지 정해줌
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },SPLASH_VIEW_TIME)
    }
}