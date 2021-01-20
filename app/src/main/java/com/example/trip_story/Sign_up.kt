package com.example.trip_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.sign_image
import kotlinx.android.synthetic.main.activity_sign_up.*

class Sign_up : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    lateinit var email: String
    lateinit var pw1: String
    lateinit var pw2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()   //auth : 회원가입에 관련된 정보들을 쓸 수 있게해줌
        signup_button.setOnClickListener() {
            email = signup_edit1.text.toString()
            pw1 = signup_edit2.text.toString()
            pw2 = signup_edit3.text.toString()

            if (pw1.equals(pw2)) {  //equals
                createEmail()
            } else if (email.isEmpty() || pw1.isEmpty()) {
                Toast.makeText(this, "이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createEmail() {
        auth?.createUserWithEmailAndPassword(
            signup_edit1.text.toString(),
            signup_edit2.text.toString()
        )
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                } else if (it.exception.toString().isNotEmpty()) {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            ?.addOnFailureListener {
                Toast.makeText(this,"통신 불량",Toast.LENGTH_SHORT).show()
                // 통신 불량에 대한 예외처리
            }
    }
}




        /*
        signup_button.setOnClickListener{
          Signup()
        }
    }

    fun Signup() {//회원가입 코드를 넣어줌
        auth?.createUserWithEmailAndPassword(   //createUserWithEmailAndPassword : 회원가입을 시켜줌
            signup_edit1.text.toString(),
            signup_edit2.text.toString()
        )
            ?.addOnCompleteListener { task -> //task?..
                if (task.isSuccessful) {    //회원가입이 에러없이 되었다.
                    moveMainPage(task.result?.user)
                } else if (!task.exception?.message.isNullOrEmpty()) { //NullOrEmpty가 아니라면 이기때문에 !를 앞에 붙임
                    //show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    fun moveMainPage(user: FirebaseUser?) {    //회원가입이 성공할 시 다음페이지로 넘어가는 메소드
        if (user != null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}

         */
