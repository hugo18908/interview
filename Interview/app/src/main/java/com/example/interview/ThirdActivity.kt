package com.example.interview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_third.*
import java.net.HttpURLConnection
import java.net.URL

class ThirdActivity : AppCompatActivity() {

    var id = toString()
    var title = toString()
    var url = toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        get()


    }

    fun get() {
        var string = intent.getStringExtra("key")
        var delimiter = "\n"

        val data = string.split(delimiter)

        id = data[0]
        title = data[1]
        url = data[2]


        textView.text = id + "\n" + title

    }


}
