package com.example.trip_story

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.navigation.UserFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    var test : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //사진 경로를 가져올 수 있는 권한을 요청
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_layout, HomeFragment()).commit()

        bottom_navigation.selectedItemId = R.id.bottom_menu_home
        bottom_navigation.setOnNavigationItemSelectedListener {
            val transaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.bottom_menu_home -> {
                    if(!test.equals("home")) {
                        transaction.replace(
                            R.id.fragment_layout,
                            HomeFragment()
                        ).commit()
                        test = "home"
                    }
                    false
                }
                R.id.bottom_menu_user -> {
                    if (!test.equals("user")) {
                        var userFragment = UserFragment()
                        var bundle = Bundle()
                        var uid = FirebaseAuth.getInstance().currentUser?.uid
                        bundle.putString("destinationUid", uid)
                        userFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_layout, userFragment).commit()
                    }
                    false
                }


                R.id.bottom_menu_photo -> {
                    //외부 스토리지 경로를 가져올 수 있는 권한지 있는지 체크
                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        startActivity(Intent(this,AddPhotoActivity::class.java))
                    }
                    false
                }


                R.id.bottom_menu_map -> {
                    if(!test.equals("map")) {
                        transaction.replace(
                            R.id.fragment_layout,
                            MapFragment()
                        ).commit()
                        test = "map"
                    }
                    false
                }

                R.id.bottom_menu_message -> {
                    if(!test.equals("message")) {
                        transaction.replace(
                            R.id.fragment_layout,
                            MessageFragment()
                        ).commit()
                        test = "message"
                    }
                    false
                }
/*
                R.id.bottom_menu_setting -> {
                    if(!test.equals("setting")) {
                        transaction.replace(    //fragment가 해당 layout으로 대체됨
                            R.id.fragment_layout,
                            SettingFragment()
                        ).commit()
                        test = "setting"
                    }
                    false
                }
                */
                else->{
                    false
                }
            }
        }
    }
}
