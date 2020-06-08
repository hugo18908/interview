package com.example.interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        jump()

    }

    fun jump(){
        jump_btn.setOnClickListener{
            var intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

    }
}
