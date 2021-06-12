package com.rotate.application

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(val context: Context,var dataList:List<BitcoinTracker> ) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(dataList.size>0)
        (holder as DataViewHolder).bind(dataList[position])
    }

    override fun getItemCount(): Int {
       return dataList.size
    }
    inner class DataViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(item:BitcoinTracker){
           itemView.findViewById<TextView>(R.id.hash).text=item.x.hash;
            itemView.findViewById<TextView>(R.id.time).text=item.x.time.toString();
          //  itemView.findViewById<TextView>(R.id.amount).text=item.data.price.get(0);
        }
    }

}