package com.example.clase6_retrofit_terremoto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TerremotoAdapter
    private var terremotoLista = mutableListOf<Terremoto>()
    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_list, container, false)
        recyclerView = myView.findViewById(R.id.listrecyclerview)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter = TerremotoAdapter()
        recyclerView.adapter = adapter

        getTerremotos(view)

        adapter.onItemClickListener = {
            Toast.makeText(activity, it.lugar, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
        Log.i("FRA", job.toString())
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun getTerremotos(view: View) {
        CoroutineScope(Dispatchers.IO + job).launch {
            val call = getRetrofit().create(TerremotoAPIService::class.java).getListaTerremosSemana()
            val response: TerremotoJsonResponse? = call.body()

            activity?.runOnUiThread {
                terremotoLista.clear()
                if (call.isSuccessful) {
                    terremotoLista = ((response?.features?.let { parseCallTerremotos(it) } ?: emptyList()) as MutableList<Terremoto>)
                    adapter.submitList(terremotoLista)
                    handleEmptyView(terremotoLista, view)
                } else {
                    val error = call.errorBody().toString()
                    Snackbar
                        .make(view, error, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parseCallTerremotos(features: MutableList<Feature>): MutableList<Terremoto> {
        val lista = mutableListOf<Terremoto>()

        for (feature in features) {
            val id = feature.id

            val properties = feature.properties
            val magnitud = properties.mag
            val lugar = properties.place
            val duracion = properties.time

            val geometry = feature.geometry
            val longitud = geometry.longitude
            val latitud = geometry.latitude

            val terremoto = Terremoto(id, lugar, magnitud, duracion, latitud, longitud)
            lista.add(terremoto)
        }
        return lista
    }

    private fun handleEmptyView(terremotoLista: MutableList<Terremoto>, view: View) {
        if (terremotoLista.isEmpty()) {
            Snackbar
                .make(view, "No hay terremotos en esta franja de tiempo", Snackbar.LENGTH_LONG)
                .show()
        }
    }
}
