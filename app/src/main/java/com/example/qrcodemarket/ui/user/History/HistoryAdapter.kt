package com.example.qrcodemarket.ui.user.History

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodemarket.data.model_new.HistoryData
import com.example.qrcodemarket.databinding.RecyclerHistoryBinding

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyViewHolder>(),Filterable {

    var access = ArrayList<HistoryData>()
    var tempListAccess = ArrayList<HistoryData>()

    fun setDataList(data:ArrayList<HistoryData>){
        this.access = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerHistoryBinding.inflate(layoutInflater)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(access[position])
    }

    override fun getItemCount(): Int = access.size

    class MyViewHolder(val binding: RecyclerHistoryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: HistoryData){
            binding.recyclerHistory = data
            binding.executePendingBindings()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) tempListAccess = access else {

                    val filteredList = ArrayList<HistoryData>()
                    access
                        .filter {
                            (it.marketName.contains(constraint.toString())) or
                                    (it.date.contains(constraint.toString()))

                        }
                        .forEach { filteredList.add(it) }
                    tempListAccess = filteredList
                    Log.i("abc","abc"+tempListAccess)
                }
                return FilterResults().apply { values = tempListAccess }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                tempListAccess = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<HistoryData>
                notifyDataSetChanged()
            }
        }
    }
}