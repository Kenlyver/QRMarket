package com.example.qrcodemarket.ui.admin.market

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qrcodemarket.data.model.MarketData
import com.example.qrcodemarket.data.model.dataMarketManage
import com.example.qrcodemarket.data.network.response.RetroInstance
import com.example.qrcodemarket.data.network.response.RetroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketManageViewModel():ViewModel() {
    var recyclerListData: MutableLiveData<dataMarketManage>
    var marketManageAdapter: MarketManageAdapter

    init {
        recyclerListData = MutableLiveData()
        marketManageAdapter = MarketManageAdapter()
    }

    fun getAdapter(): MarketManageAdapter {
        return marketManageAdapter
    }

    fun setAdapterData(data: ArrayList<MarketData>?){
        if (data != null) {
            marketManageAdapter.setDataList(data)
        }
        marketManageAdapter.notifyDataSetChanged()
    }

    fun getRecyclerListDataObserver(): MutableLiveData<dataMarketManage> {
        return recyclerListData
    }

    fun makeAPICall() {
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.dataMarketManage()
        call.enqueue(object : Callback<dataMarketManage> {
            override fun onResponse(call: Call<dataMarketManage>, response: Response<dataMarketManage>) {
                if(response.isSuccessful){
                    recyclerListData.postValue(response.body())
                    Log.i("response","success "+response.body())
                }else{
                    recyclerListData.postValue(null)
                    Log.i("response","fail")
                }
            }

            override fun onFailure(call: Call<dataMarketManage>, t: Throwable) {
                recyclerListData.postValue(null)
                Log.i("response","fail")
            }
        })
    }
}