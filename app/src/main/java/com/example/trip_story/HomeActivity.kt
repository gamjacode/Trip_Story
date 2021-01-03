package com.example.trip_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    var test : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_layout, HomeFragment()).commit()

        bottom_navigation_bar.selectedItemId = R.id.bottom_menu_home
        bottom_navigation_bar.setOnNavigationItemSelectedListener {
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
                R.id.bottom_menu_todo -> {
                    if(!test.equals("todo")) {
                        transaction.replace(
                            R.id.fragment_layout,
                            TodoFragment()
                        ).commit()
                        test = "todo"
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
                else->{
                    false
                }
            }
        }
    }
}
