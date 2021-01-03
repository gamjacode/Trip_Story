package com.example.trip_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_find_pw.*
import kotlinx.android.synthetic.main.activity_login.*

class FindPw : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_pw)

        findpw_button.isEnabled=false
        findpw_image.setOnClickListener{
            startActivity(Intent(this,Login::class.java))
        }

        findpw_edit1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input_id = findpw_edit1.text.toString()
                val regex = Regex(pattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+\$")
                val matched = regex.containsMatchIn(input_id)

                if(matched==true){
                    findpw_text1.setText("올바른 형식 입니다.")
                    findpw_text1.setTextColor(resources.getColor(R.color.colorBlack))
                    findpw_button.isEnabled=true
                }
                else{
                    findpw_text1.setText("올바르지 않습니다.")
                    findpw_text1.setTextColor(resources.getColor(R.color.colorincorrect))
                }
                if(input_id.equals("")||input_id==null){
                    findpw_text1.setText("")
                }
            }
        })
    }
}