package com.example.qrcodemarket.ui.admin.statistical


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qrcodemarket.data.model.StatisticalData
import com.example.qrcodemarket.data.model.dataStatistical
import com.example.qrcodemarket.data.network.response.RetroInstance
import com.example.qrcodemarket.data.network.response.RetroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StatisticalViewModel() : ViewModel() {
    var recyclerListData: MutableLiveData<dataStatistical>
    var statisticalAdapter: StatisticalAdapter

    init {
        recyclerListData = MutableLiveData()
        statisticalAdapter = StatisticalAdapter()
    }

    fun getAdapter(): StatisticalAdapter {
        return statisticalAdapter
    }

    fun setAdapterData(data: ArrayList<StatisticalData>?){
        if (data != null) {
            statisticalAdapter.setDataList(data)
        }
        statisticalAdapter.notifyDataSetChanged()
    }

    fun getRecyclerListDataObserver(): MutableLiveData<dataStatistical> {
        return recyclerListData
    }

    fun makeAPICall() {
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.dataStatistical()
        call.enqueue(object : Callback<dataStatistical> {
            override fun onResponse(call: Call<dataStatistical>, response: Response<dataStatistical>) {
                if(response.isSuccessful){
                    recyclerListData.postValue(response.body())
                    Log.i("response","success "+response.body())
                }else{
                    recyclerListData.postValue(null)
                    Log.i("response","fail")
                }
            }

            override fun onFailure(call: Call<dataStatistical>, t: Throwable) {
                recyclerListData.postValue(null)
                Log.i("response","fail")
            }
        })
    }
}