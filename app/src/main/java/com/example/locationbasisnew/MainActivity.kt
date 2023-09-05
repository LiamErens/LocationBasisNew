package com.example.locationbasisnew

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private lateinit var recyclerView: RecyclerView
    private lateinit var locationList: MutableList<LocationData>
    private lateinit var locationAdapter: LocationAdapter

    private lateinit var timeStamp: String
    private lateinit var addressText: String

    private var lastLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        locationList = mutableListOf()

        locationAdapter = LocationAdapter(locationList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = locationAdapter

        val button: Button = findViewById(R.id.btnLocation)
        button.setOnClickListener {
            getLocation()
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        } else {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5f, this)
        }
    }


    override fun onLocationChanged(location: Location) {

        // Call fetchAddress to convert latitude and longitude to an address

        fetchAddress(location.latitude, location.longitude)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
            }
        }
    }
    private fun fetchAddress(latitude: Double, longitude: Double) {
        val apiKey = "de6bc85871014773abf95026579ae632"
        val url =
            "https://api.geoapify.com/v1/geocode/reverse?lat=$latitude&lon=$longitude&apiKey=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (!response.isSuccessful || responseBody == null) {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to fetch address",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return
                }

                try {
                    val jsonObject = JSONObject(responseBody)  // Parse the JSON response
                    val featuresArray = jsonObject.getJSONArray("features")  // Get the "features" array

                    if (featuresArray.length() > 0) {
                        val firstFeature = featuresArray.getJSONObject(0)  // Get the first feature object
                        val properties = firstFeature.getJSONObject("properties")  // Get the "properties" object
                        val addressText = properties.getString("formatted")  // Get the "formatted" address

                        // Create a LocationData instance with the retrieved data
                        val newLocationData = LocationData(latitude, longitude, addressText)

                        // Add the newLocationData to the locationList
                        locationList.add(newLocationData)

                        // Notify the adapter of the data change
                        runOnUiThread {
                            locationAdapter.notifyDataSetChanged()
                        }
                    } else {
                        // Handle the case where no address was found
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "No address found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: JSONException) {
                    // Handle JSON parsing exception
                    Log.e(TAG, "JSON parsing error: ${e.message}")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error parsing JSON", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }





}