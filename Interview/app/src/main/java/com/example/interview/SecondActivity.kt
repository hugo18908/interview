package com.example.interview

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL


class SecondActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val url = "https://jsonplaceholder.typicode.com/photos"
        asyncTask().execute(url)


    }

    inner class asyncTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg url: String?): String {

            var text: String
            val connect = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connect.connect()
                text = connect.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connect.disconnect()
            }
            return text

        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val listdata = ArrayList<String>()
            var id = ArrayList<String>()
            var title = ArrayList<String>()
            var url = ArrayList<String>()
            var thumbnailUrl = ArrayList<String>()
            var albumId = ArrayList<String>()
            val jsArray = JSONArray(result)
            for (i in 0 until jsArray.length()) {
                val jsObj = jsArray.getJSONObject(i)
                var albumIdName = jsObj.getString("albumId")
                var idName = jsObj.getString("id")
                var titleName = jsObj.getString("title")
                var urlName = jsObj.getString("url")
                var thumbnailUrlName = jsObj.getString("thumbnailUrl")
                id.add(idName)
                title.add(titleName)
                url.add(urlName)
                thumbnailUrl.add(thumbnailUrlName)
                albumId.add(albumIdName)

            }


            for (a in 0 until id.size) {
                listdata.add(
                    id[a] + "\n" +
                            title[a] + "\n"
                            + thumbnailUrl[a]
                )
            }

            val adapter = mAdapter(listdata)
            val lv = findViewById<View>(R.id.list_view) as RecyclerView
            val layoutManager = GridLayoutManager(this@SecondActivity, 4)
            lv.layoutManager = layoutManager
            lv.adapter = adapter
        }


    }

}

class mAdapter(private val mData: List<String>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.dataView.context
        val intent = Intent(context,ThirdActivity::class.java)
        intent.putExtra("key",mData[position])
        Log.d("TAG",mData[position])



        holder.dataView.text = mData[position]
        holder.dataView.setOnClickListener {
            context.startActivity(intent)

        }




    }




}

 class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    val dataView: TextView = v.findViewById(R.id.info_text)




}








