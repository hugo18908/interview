package com.example.test

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fish.day13_localstoragesharedpreferences.sharedPreference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.style_lv.view.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    val hs = HashSet<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide(); //隱藏標題
        setContentView(R.layout.activity_main)
        val from = arrayOf("locationName", "weather")
        val to = intArrayOf(R.id.tvLocal, R.id.tvweather)

        val URL =
            "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-094341B1-766B-4A54-BFBD-0E5890F5C984"
        val que = Volley.newRequestQueue(this@MainActivity)
        val req = StringRequest(
            Request.Method.GET, URL,
            Response.Listener { response ->
                val json = response.toString()
                val jsonObj = JSONObject(json)

                var locationName = ArrayList<String>()
                val items: MutableList<Map<String, Any>> =
                    ArrayList()

                val records = jsonObj.getJSONObject("records")


                val location = records.getJSONArray("location")


                for (i in 0 until location.length()) {
                    val Obj = location.getJSONObject(i)
                    locationName.add(Obj.getString("locationName"))
                    var wxVar = ArrayList<String>()
                    var popVar = ArrayList<String>()
                    var MinTVar = ArrayList<String>()
                    var ClVar = ArrayList<String>()
                    var MaxTVar = ArrayList<String>()
                    var TimeVar = ArrayList<String>()
                    val weatherElement = Obj.getJSONArray("weatherElement")
                    for (j in 0 until weatherElement.length()) {
                        val elementNameObj = weatherElement.getJSONObject(j)
                        val elementName = elementNameObj.getString("elementName")
                        if (elementName == "Wx") {
                            val time = elementNameObj.getJSONArray("time")
                            for (k in 0 until time.length()) {
                                val weathername = time.getJSONObject(k)
                                var time = weathername.getString("startTime")
                                var parameter = weathername.getJSONObject("parameter")
                                var parameterNameVar = parameter.getString("parameterName")
                                wxVar.add(parameterNameVar)
                                TimeVar.add(time)
                            }
                        } else if (elementName == "PoP") {
                            val time = elementNameObj.getJSONArray("time")
                            for (k in 0 until time.length()) {
                                val weathername = time.getJSONObject(k)
                                var parameter = weathername.getJSONObject("parameter")
                                var parameterNameVar = parameter.getString("parameterName")
                                popVar.add(parameterNameVar)
                            }
                        } else if (elementName == "MinT") {
                            val time = elementNameObj.getJSONArray("time")
                            for (k in 0 until time.length()) {
                                val weathername = time.getJSONObject(k)
                                var parameter = weathername.getJSONObject("parameter")
                                var parameterNameVar = parameter.getString("parameterName")
                                MinTVar.add(parameterNameVar)
                            }
                        } else if (elementName == "CI") {
                            val time = elementNameObj.getJSONArray("time")
                            for (k in 0 until time.length()) {
                                val weathername = time.getJSONObject(k)
                                var parameter = weathername.getJSONObject("parameter")
                                var parameterNameVar = parameter.getString("parameterName")
                                ClVar.add(parameterNameVar)
                            }
                        } else if (elementName == "MaxT") {
                            val time = elementNameObj.getJSONArray("time")
                            for (k in 0 until time.length()) {
                                val weathername = time.getJSONObject(k)
                                var parameter = weathername.getJSONObject("parameter")
                                var parameterNameVar = parameter.getString("parameterName")
                                MaxTVar.add(parameterNameVar)
                            }
                        }
                    }
                        val item: MutableMap<String, Any> =
                            HashMap()
                        item.put(
                            "weather", "天氣:：" + wxVar[0] + "\n"
                                    + "濕度：" + popVar[0] + "%" + "\n"
                                    + "舒適度：" + ClVar[0] + "\n"
                                    + "最高溫：" + MaxTVar[0] + "℃" + "\n"
                                    + "最低溫：" + MinTVar[0] + "℃"
                        )
                        item.put("locationName", Obj.getString("locationName"))
                        items.add(item);

                    lv.onItemClickListener =
                        OnItemClickListener { parent, view, position, id ->
                            var intent =  Intent(this, SecondActivity::class.java)
                            intent.putExtra("bundle", view.tvLocal.text.toString());
                            this.startActivity(intent);
                        }

                    val pref = sharedPreference(this)

                    lv.onItemLongClickListener =
                        OnItemLongClickListener { parent, view, position, id ->
                            hs.add(view.tvLocal.text.toString())
                            pref.save(hs)
                            val context = applicationContext
                            val text = "已加入我的最愛"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, text, duration)
                            toast.show()
                            true
                        }

                    button.setOnClickListener {
                        val intent = Intent(this, showActivity::class.java)
                        startActivity(intent)
                    }

                }
                val adapter = SimpleAdapter(
                    this,
                    items,
                    R.layout.style_lv,
                    from,
                    to
                )
                lv.setAdapter(adapter)
                tv_title.text = "本日天氣"

                jump.setOnClickListener {
                    var intent = Intent(this ,local::class.java)
                    this.startActivity(intent);

                }
            }, Response.ErrorListener {
            })
        que.add(req)


    }




}
