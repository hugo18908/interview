package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleAdapter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_second.*
import org.json.JSONObject

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide(); //隱藏標題
        setContentView(R.layout.activity_second)


        val URL =
            "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-091?Authorization=CWB-094341B1-766B-4A54-BFBD-0E5890F5C984&format=JSON&elementName=UVI,T,Wx "
        var key = intent.getStringExtra("bundle")

        val que = Volley.newRequestQueue(this@SecondActivity)

        val req = StringRequest(
            Request.Method.GET, URL,
            Response.Listener { response ->
                val json = response.toString()
                val jsonObj = JSONObject(json)
                val from = arrayOf("weather")
                val to = intArrayOf(R.id.tvweather)
                val items: MutableList<Map<String, Any>> =
                    ArrayList()

                val records = jsonObj.getJSONObject("records")
                val locations = records.getJSONArray("locations")
                val TVar = ArrayList<String>()
                val WxVar = ArrayList<String>()
                val TimeVar = ArrayList<String>()
                val DayVar = arrayOf("今天","明天","後天")


                for (i in 0 until locations.length()) {
                    val locationsObj = locations.getJSONObject(i)
                    val location = locationsObj.getJSONArray("location")
                    for (j in 0 until location.length()) {
                        val locationObj = location.getJSONObject(j)
                        val weatherElement = locationObj.getJSONArray("weatherElement")
                        val locationName = locationObj.getString("locationName")
                        if (locationName == key) {
                            for (k in 0 until weatherElement.length()) {
                                val weatherElementObj = weatherElement.getJSONObject(k)
                                val elementName = weatherElementObj.getString("elementName")
                                if (elementName == "T") {
                                    val time = weatherElementObj.getJSONArray("time")
                                    for (l in 0 until time.length() step 2) {
                                        val timeObj = time.getJSONObject(l)
                                        val elementValue = timeObj.getJSONArray("elementValue")
                                        val time = timeObj.getString("endTime")
                                        val Daystr = time.substring(0,10)
                                        for (m in 0 until elementValue.length() ) {
                                            val elementValueObj = elementValue.getJSONObject(m)
                                            var parameterNameVar =
                                                elementValueObj.getString("value")
                                                TimeVar.add(Daystr)
                                                TVar.add(parameterNameVar)


                                        }
                                    }
                                } else if (elementName == "Wx") {
                                    val time = weatherElementObj.getJSONArray("time")
                                    for (l in 0 until time.length() step 2) {
                                        val timeObj = time.getJSONObject(l)
                                        val elementValue = timeObj.getJSONArray("elementValue")
                                        for (m in 0 until elementValue.length() step 2) {
                                            val elementValueObj = elementValue.getJSONObject(m)
                                            var parameterNameVar =
                                                elementValueObj.getString("value")
                                            WxVar.add(parameterNameVar)
                                        }
                                    }
                                }

                            }
                            for (a in 0 until WxVar.size ) {

                                if(a <= 2) {

                                    val item: MutableMap<String, Any> =
                                        HashMap()
                                    item.put(
                                        "weather",
                                         DayVar[a] + "\n"
                                                + "天氣預報:：" + WxVar[a] + "\n"
                                                + "平均溫度:：" + TVar[a] + "℃" + "\n"

                                    )
                                    items.add(item);

                                }
                                else {

                                    val item: MutableMap<String, Any> =
                                        HashMap()
                                    item.put(
                                        "weather",
                                         TimeVar[a] + "\n"
                                                + "天氣預報:：" + WxVar[a] + "\n"
                                                + "平均溫度:：" + TVar[a] + "℃" + "\n"

                                    )
                                    items.add(item);

                                }


                            }
                        }
                    }
                }
                val adapter = SimpleAdapter(
                    this,
                    items,
                    R.layout.sec_style_lv,
                    from,
                    to
                )
                lv_sec.setAdapter(adapter)
                tv_location.text = key
            },
            Response.ErrorListener {
            })
        que.add(req)
    }
}
