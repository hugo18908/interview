package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fish.day13_localstoragesharedpreferences.sharedPreference
import kotlinx.android.synthetic.main.activity_show.*
import org.json.JSONObject.NULL

class showActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide(); //隱藏標題
        setContentView(R.layout.activity_show)

        val pref = sharedPreference(this)
         textView.setText(pref.get().toString())


    }
}
