package com.example.locationbasisnew

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.locationbasisnew.RetrofitClient.API_KEY
import java.io.IOException
import java.sql.Timestamp
import java.util.Locale

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
        tvOutput = findViewById(R.id.lblOutput)
        tvOutput.text = "Latitude: \n${location.latitude}, Longitude: ${location.longitude}"

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
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val addressText = "${address.getAddressLine(0)}, ${address.locality}, ${address.adminArea}, ${address.countryName}"

                // Now you can display the addressText in your TextView or wherever you want.
                tvOutput.text = "Address: \n$addressText"
            } else {
                // Handle the case where no addresses were found
                tvOutput.text = "No address found"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the exception, e.g., show an error message to the user
        }
    }
}