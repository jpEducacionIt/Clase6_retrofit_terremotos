package com.example.clase6_retrofit_terremoto

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TerremotoAdapter: ListAdapter<Terremoto, TerremotoAdapter.ViewHolder>(DiffCallBack) {

    lateinit var onItemClickListener: (Terremoto) -> Unit

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val magnitudText : TextView = view.findViewById(R.id.item_list_magnitud)
        private val lugarText: TextView = view.findViewById(R.id.item_list_lugar)
        private val context: Context = view.context

        fun bind (terremoto: Terremoto) {
            magnitudText.text = context.getString(R.string.magnitude_format, terremoto.magnitud)
            lugarText.text = terremoto.lugar

            view.setOnClickListener {
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener(terremoto)
                } else {
                    Log.i("TerremotoAdapter", "Implementar listener Adapter")
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerremotoAdapter.ViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TerremotoAdapter.ViewHolder, position: Int) {
        val terremoto = getItem(position)
        holder.bind(terremoto)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Terremoto>() {
        override fun areItemsTheSame(oldItem: Terremoto, newItem: Terremoto): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Terremoto, newItem: Terremoto): Boolean {
            return oldItem == newItem
        }
    }
}
