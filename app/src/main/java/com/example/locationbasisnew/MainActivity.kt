package com.example.locationbasisnew

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.locationbasisnew.RetrofitClient.API_KEY
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var tvOutput : TextView
    private val locationPermissionCode =2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button : Button =findViewById(R.id.btnLocation)
        button.setOnClickListener(){
            getLocation()
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)

    }

    override fun onLocationChanged(location: Location) {
        tvOutput= findViewById(R.id.lblOutput)
        tvOutput.text= "Latitude: \n" + location.latitude + ",Longitude: " + location.longitude
        fetchAddress()
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
        val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()

        RetrofitClient.locationInterface.getAddress(
            API_KEY, timeStamp, latitude, longitude
        ).enqueue(object : Callback<AddressResponse> {
            override fun onResponse(call: Call<AddressResponse>, response: Response<AddressResponse>) {
                if (response.isSuccessful) {
                    val address = response.body()?.formattedAddress
                    tvOutput.text = "Address: $address"
                } else {
                    // Handle API call error
                    Toast.makeText(this@MainActivity, "Error getting address", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}