package com.example.locationbasisnew

import java.sql.Time

data class Location(
    val Longitude: Double,
    val Latitude: Double,
    val TimeStamp: String,
    val Address: String
)
data class Address(
    val address_line1: String,
    val address_line2 : String
)