package com.example.test

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.local.*
import org.json.JSONObject

class local : AppCompatActivity() {
    var city = " "


    private val NeedPermissions =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    private val PERMISSION_REQUEST_CODE = 487

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide(); //隱藏標題
        setContentView(R.layout.local)

    }

    override fun onResume() {
        super.onResume()
        setup()

    }


    @SuppressLint
    fun setup() {
        if (!requestAllNeedPermissions())
            return;
        else
            requestLocation()

    }


    @SuppressLint("MissingPermission")
    fun requestLocation() {
        Log.d("setup", "enter requestLocation")
        val locationRequest = LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//         設定更新速度
        locationRequest.setInterval(10)
//        設定要更新幾次座標
        locationRequest.setNumUpdates(1)
        val mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationresult: LocationResult?) {
                    Log.d("setup", "enter onLocationResult")
                    locationresult ?: return
//                val locationText =
//                    "經度:${locationresult.lastLocation.longitude} "+"\n"+"緯度${locationresult.lastLocation.latitude}"
                    Log.d(
                        "locationresult",
                        "${locationresult.lastLocation.longitude}" + "${locationresult.lastLocation.latitude}"
                    )


                    var str =
                        "https://maps.googleapis.com/maps/api/geocode/json?latlng=${locationresult.lastLocation.latitude},${locationresult.lastLocation.longitude}&key=AIzaSyAkgyXAEs63XbE0xZZ7Sg5gXC2VqzH3T4g&language=zh-TW"
                    Log.d("str", str)
                    val que = Volley.newRequestQueue(this@local)
                    val req = StringRequest(
                        Request.Method.GET, str,
                        Response.Listener { response ->
                            val json = response.toString()
                            val jsonObj = JSONObject(json)
                            val results = jsonObj.getJSONArray("results")
                            for (i in 0 until results.length()) {
                                val resultsObj = results.getJSONObject(i)
                                val address_components =
                                    resultsObj.getJSONArray("address_components")
                                for (j in 0 until address_components.length()) {
                                    val address_componentsObj = address_components.getJSONObject(j)
                                    val types = address_componentsObj.getJSONArray("types")
                                    if (types[0].toString() == "administrative_area_level_1") {
                                        city = address_componentsObj.getString("long_name")
                                        city = city.replace('台', '臺')
                                        view()
                                        break
                                        Log.e("setup", "in callback" + city)
                                    }
                                    break
                                }
                            }
                        }, Response.ErrorListener {
                        })
                    que.add(req)
                }
            },
            null
        )
        Log.d("setup", "city = " + city)
    }


    //    回傳是否已經有權限。
    fun requestAllNeedPermissions(): Boolean {
        Log.d("setup", "in requestAllNeedPermissions")
        val permissionsList = ArrayList<String>()
        for (permission in NeedPermissions)
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(permission)
        if (permissionsList.size < 1)
            return true

        ActivityCompat.requestPermissions(
            this,
            permissionsList.toTypedArray(),
            PERMISSION_REQUEST_CODE
        )
        return false
    }

    //查詢是否調用到權限
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("setup", "in onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!requestAllNeedPermissions())
                finish()

        }


    }

    fun view() {
        val from = arrayOf("locationName", "weather")
        val to = intArrayOf(R.id.tvLocal, R.id.tvweather)
        Log.d("setup", "in view function city = " + city)
        val URL =
            "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-094341B1-766B-4A54-BFBD-0E5890F5C984"
        val que = Volley.newRequestQueue(this@local)
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
                    val cityName = Obj.getString("locationName")

                    if (cityName == city) {

                        for (j in 0 until weatherElement.length()) {
                            val elementNameObj = weatherElement.getJSONObject(j)
                            val elementName = elementNameObj.getString("elementName")
                            Log.e("city2", elementName)
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
                    }


                }
                val adapter = SimpleAdapter(
                    this,
                    items,
                    R.layout.style_lv,
                    from,
                    to
                )
                tv_local.text = "本地天氣"
                lv_local.setAdapter(adapter)


            }, Response.ErrorListener {
            })
        que.add(req)

    }
}