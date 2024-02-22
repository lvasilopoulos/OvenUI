package com.ovenui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ovenui.kitchen.Burner

class CookTopAdapter(val listener : (Burner) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Burner> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookTopViewHolder {
        return CookTopViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_burner_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CookTopViewHolder -> {
                val item = items[position]
                holder.bind(State.getInstance().burners.get(position))
                holder.itemView.setOnClickListener{ listener(item) }
            }
        }
    }

    fun submitList(burnerList : List<Burner>){
        items = burnerList
        notifyDataSetChanged()
    }

    class CookTopViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val burnerName : TextView = itemView.findViewById(R.id.burner_name)
        val burnerState : ImageView = itemView.findViewById(R.id.burner_state)

        fun bind(burner: Burner){

            burnerName.text = burner.name
            when(burner.mealState.colorState()){
                1 -> burnerState.setColorFilter(Color.argb(255, 255, 0, 0))
                2 -> burnerState.setColorFilter(Color.argb(255, 32, 234, 0))
                else -> {
                    burnerState.setColorFilter(Color.argb(255, 255, 255, 255))
                }
            }

        }

    }

}