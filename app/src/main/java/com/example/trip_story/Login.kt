package com.example.trip_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.sign

class Login : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener{
        loginEmail()
        }

        sign_image.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        login_text3.setOnClickListener{
            startActivity(Intent(this,FindPw::class.java))
        }
    }

    fun loginEmail(){
        auth?.signInWithEmailAndPassword(login_edit1.text.toString(),login_edit2.text.toString())
            ?.addOnCompleteListener {
                if(it.isSuccessful){
                    startActivity(Intent(this, HomeActivity::class.java))
                }else{
                    Toast.makeText(this,"아이디 또는 비밀번호를 확인하세요", Toast.LENGTH_LONG).show()
                }
            }
    }
}





