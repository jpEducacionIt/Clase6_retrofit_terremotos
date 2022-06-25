package com.example.clase6_retrofit_terremoto

import retrofit2.Response
import retrofit2.http.GET


interface TerremotoAPIService {
    @GET (value = "all_week.geojson")
    suspend fun getListaTerremosSemana(): Response<TerremotoJsonResponse>

    @GET (value = "all_day.geojson")
    suspend fun getListaTerremosDia(): TerremotoJsonResponse
}
