package com.rotate.application

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DataAdapter(val context: Context, var dataList: List<BitcoinTracker>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var price:TextView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(dataList.size>0)
        (holder as DataViewHolder).bind(dataList[position])
    }

    override fun getItemCount(): Int {
       return dataList.size
    }
    inner class DataViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(item: BitcoinTracker){
           itemView.findViewById<TextView>(R.id.hash).text=item.x.hash;
            itemView.findViewById<TextView>(R.id.time).text=item.x.time.toString();
            price=itemView.findViewById<TextView>(R.id.amount);
            itemView.findViewById<TextView>(R.id.amount).text="$"+item.x.out.get(0).value
            val format: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"))


            var formatted: String = format.format(item.x.time*1000L)
            println(formatted)
            itemView.findViewById<TextView>(R.id.time).text="Time: "+formatted
        }
    }

}