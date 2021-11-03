package com.example.qrcodemarket.ui.admin.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodemarket.data.model.MarketData
import com.example.qrcodemarket.databinding.RecycleMarketItemBinding


class MarketManageAdapter():RecyclerView.Adapter<MarketManageAdapter.MyViewHolder>() {

    var markets = ArrayList<MarketData>()

    fun setDataList(data:ArrayList<MarketData>){
        this.markets = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketManageAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecycleMarketItemBinding.inflate(layoutInflater)
        return MarketManageAdapter.MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarketManageAdapter.MyViewHolder, position: Int) {
        holder.bind(markets[position])
    }

    override fun getItemCount(): Int = markets.size

    class MyViewHolder(val binding: RecycleMarketItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: MarketData){
            binding.marketbinding = data
            binding.executePendingBindings()
        }
    }
}