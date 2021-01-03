package com.example.trip_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.sign

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.isEnabled = false
        login_edit1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input_id = login_edit1.text.toString()
                val regex = Regex(pattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+\$")
                val matched = regex.containsMatchIn(input_id)

                if(matched==true){
                    login_text1.setText("올바른 형식 입니다.")
                    login_text1.setTextColor(resources.getColor(R.color.colorBlack))

                    login_edit2.addTextChangedListener(object:TextWatcher{
                        override fun afterTextChanged(s: Editable?) {}
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            val input_pw = login_edit2.text.toString()
                            val regex = Regex(pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,15}.$")
                            val matched = regex.containsMatchIn(input_pw)

                            if(matched==true){
                                login_text2.setText("올바른 형식 입니다.")
                                login_text2.setTextColor(resources.getColor(R.color.colorBlack))
                                login_button.isEnabled = true
                            }
                            else{
                                login_text2.setText("올바른 비밀번호를 입력하세요.")
                                login_text2.setTextColor(resources.getColor(R.color.colorincorrect))
                            }
                            if(input_pw.equals("")||input_pw==null){
                                login_text2.setText("")
                            }
                        }

                    })
                }
                else{
                    login_text1.setText("올바르지 않습니다.")
                    login_text1.setTextColor(resources.getColor(R.color.colorincorrect))
                }
                if(input_id.equals("")||input_id==null){
                    login_text1.setText("")
                }
            }
        })

        login_button.setOnClickListener{
            startActivity(Intent(this,HomeActivity::class.java))
        }
        sign_image.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
        login_text3.setOnClickListener{
            startActivity(Intent(this,FindPw::class.java))
        }
    }
}