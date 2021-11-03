package com.example.qrcodemarket.ui.user.History
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qrcodemarket.data.model.HistoryData
import com.example.qrcodemarket.data.model.dataHistory
import com.example.qrcodemarket.data.network.response.RetroInstance
import com.example.qrcodemarket.data.network.response.RetroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    lateinit var recyclerListData: MutableLiveData<dataHistory>
    lateinit var historyAdapter: HistoryAdapter

    init {
        recyclerListData = MutableLiveData()
        historyAdapter = HistoryAdapter()
    }


    fun getAdapter(): HistoryAdapter {
        return historyAdapter
    }

    fun setAdapterData(data: ArrayList<HistoryData>?){
        if (data != null) {
            historyAdapter.setDataList(data)
        }
        historyAdapter.notifyDataSetChanged()
    }

    fun getRecyclerListDataObserver(): MutableLiveData<dataHistory> {


        return recyclerListData
    }

    fun makeAPICall(input: Int) {
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.dataAccessMarket(input)
        call.enqueue(object : Callback<dataHistory>{
            override fun onResponse(call: Call<dataHistory>, response: Response<dataHistory>) {
                if(response.isSuccessful){
                    recyclerListData.postValue(response.body())
                    Log.i("response","success "+response.body())
                }else{
                    recyclerListData.postValue(null)
                    Log.i("response","fail")
                }
            }

            override fun onFailure(call: Call<dataHistory>, t: Throwable) {
                recyclerListData.postValue(null)
                Log.i("response","fail")
            }
        })
    }
}