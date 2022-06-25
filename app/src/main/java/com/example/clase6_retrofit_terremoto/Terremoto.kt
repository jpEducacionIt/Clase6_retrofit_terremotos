package com.example.clase6_retrofit_terremoto

data class Terremoto(
    val id: String,
    val lugar: String,
    val magnitud: Double,
    val duracion: Long,
    val latitud: Double,
    val longitud: Double
)