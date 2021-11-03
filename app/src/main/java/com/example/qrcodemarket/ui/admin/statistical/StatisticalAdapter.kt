package com.example.qrcodemarket.ui.admin.statistical

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodemarket.data.model.StatisticalData
import com.example.qrcodemarket.databinding.RecyclerStatisticalBinding

class StatisticalAdapter() : RecyclerView.Adapter<StatisticalAdapter.MyViewHolder>() {

    var access = ArrayList<StatisticalData>()

    fun setDataList(data:ArrayList<StatisticalData>){
        this.access = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticalAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerStatisticalBinding.inflate(layoutInflater)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticalAdapter.MyViewHolder, position: Int) {
        holder.bind(access[position])
    }

    override fun getItemCount(): Int = access.size

    class MyViewHolder(val binding: RecyclerStatisticalBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: StatisticalData){
            binding.recyclerStatistical = data
            binding.executePendingBindings()
        }
    }

}
