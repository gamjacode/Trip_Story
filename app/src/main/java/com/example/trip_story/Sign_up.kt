package com.example.trip_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.sign_image
import kotlinx.android.synthetic.main.activity_sign_up.*

class Sign_up : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup_button.isEnabled = false
        signup_edit1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input_id = signup_edit1.text.toString()
                val regex = Regex(pattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+\$")
                val matched = regex.containsMatchIn(input_id)

                if (matched == true) {
                    signup_text1.setText("올바른 형식 입니다.")
                    signup_text1.setTextColor(resources.getColor(R.color.colorBlack))

                    signup_edit2.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {}
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            val input_pw = signup_edit2.text.toString()
                            val regex = Regex(pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,15}.$")
                            val matched = regex.containsMatchIn(input_pw)

                            if (matched == true) {
                                signup_text2.setText("올바른 형식 입니다.")
                                signup_text2.setTextColor(resources.getColor(R.color.colorBlack))

                                signup_edit3.addTextChangedListener(object : TextWatcher {
                                    override fun afterTextChanged(s: Editable?) {}
                                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int,count: Int) {
                                        val input_pw1 = signup_edit2.text.toString()
                                        val input_pw2 = signup_edit3.text.toString()
                                        if(input_pw2.equals(input_pw1)){
                                            signup_text3.setText("비밀번호가 일치합니다.")
                                            signup_text3.setTextColor(resources.getColor(R.color.colorBlack))
                                            signup_button.isEnabled = true
                                        }
                                        else{
                                            signup_text3.setText("비밀번호가 일치하지 않습니다.")
                                            signup_text3.setTextColor(resources.getColor(R.color.colorincorrect))
                                        }
                                        if (input_pw2.equals("") || input_pw2 == null) {
                                            signup_text3.setText("")
                                        }
                                    }

                                })
                            } else {
                                signup_text2.setText("올바른 비밀번호를 입력하세요.")
                                signup_text2.setTextColor(resources.getColor(R.color.colorincorrect))
                            }
                            if (input_pw.equals("") || input_pw == null) {
                                signup_text2.setText("")
                            }
                        }

                    })
                } else {
                    signup_text1.setText("올바르지 않습니다.")
                    signup_text1.setTextColor(resources.getColor(R.color.colorincorrect))
                }
                if (input_id.equals("") || input_id == null) {
                    signup_text1.setText("")
                }
            }
        })

        signup_button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        sign_image.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}