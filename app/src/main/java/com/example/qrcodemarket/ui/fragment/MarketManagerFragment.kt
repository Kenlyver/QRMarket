package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.model.DetailMarket
import com.example.qrcodemarket.data.model.MarketAdapter
import com.example.qrcodemarket.data.model.getMarket
import com.example.qrcodemarket.data.network.QRApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_market_manager.view.*

class MarketManagerFragment : Fragment() {

    companion object{
        fun newInstance():MarketManagerFragment{
            return MarketManagerFragment()
        }
    }
    lateinit var mView: View
    var disposable : Disposable? = null

    val insertApi by lazy {
        QRApi.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_market_manager, container, false)
        getMarketData()

        mView.imgBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        mView.btnAddMarket.setOnClickListener {
            findNavController().navigate(R.id.action_marketManagerFragment_to_addMarketFragment)
        }

        return mView.rootView
    }



    private fun showMarket(dataMarket:List<getMarket.Data>){

        mView.recycleMarket.layoutManager = LinearLayoutManager(context)
        mView.recycleMarket.adapter = MarketAdapter(dataMarket,object: MarketAdapter.OnAdapterListener{
            override fun onCLick(dataMarket: getMarket.Data) {
                val detailMarket = DetailMarket(dataMarket.marketId, dataMarket.marketName, dataMarket.qrCodeManagementId,dataMarket.marketLocation,dataMarket.imageQRCodeIn,dataMarket.imageQRCodeOut)
                val action = MarketManagerFragmentDirections.actionMarketManagerFragmentToUpdateMarketFragment(detailMarket)
                findNavController().navigate(action)
            }
        })
    }


    private fun getMarketData(){
        disposable = insertApi.dataMarket()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    showMarket(result.data)
                    Log.i("abc", "abc: " + result.data.toString())
                },
                { error ->
                    Log.i("abc", "abc: " + error.localizedMessage + error.message + error)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }

}