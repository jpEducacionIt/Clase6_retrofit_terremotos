package com.example.clase6_retrofit_terremoto

data class Geometry(val coordinates: List<Double>) {
    val longitude: Double
        get() = coordinates[0]
    val latitude: Double
        get() = coordinates[1]
}
