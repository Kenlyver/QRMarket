package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.network.QRApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_market.view.*


class AddMarketFragment : Fragment() {

    lateinit var marketName:String
    lateinit var marketLocation:String
    lateinit var qrCodeIn:String
    lateinit var qrCodeOut:String

    var disposable: Disposable? = null

    val insertApi by lazy {
        QRApi.create()
    }
    lateinit var mView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_add_market, container, false)

        mView.imgBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        nextMarketId()
        onAddMarketClick()
        return mView.rootView
    }

    private fun nextMarketId() {
        disposable = insertApi.nextMarketId()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val idNext:String = result.nextId
                    var nextId:Int = idNext.toInt()
                    nextId = nextId+1
                    mView.txtIdMarketNext.setText(nextId.toString())
                    Log.i("abc", "abc: " + result.toString())
                },
                { error ->
                    Toast.makeText(context, "fail insert", Toast.LENGTH_SHORT).show()
                    Log.i("abc", "abc: " + error.localizedMessage + error.message + error)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun onAddMarketClick() {
        mView.btnSaveChange.setOnClickListener {
            marketName = mView.edtNameMarket.text.toString()
            marketLocation = mView.edtLocation.text.toString()
            qrCodeIn = mView.edtUrlQRIn.text.toString()
            qrCodeOut = mView.edtUrlQROut.text.toString()
            if (marketName.isNullOrEmpty()) {
                mView.edtNameMarket.requestFocus()
                mView.edtNameMarket.setError("Vui lòng nhập tên chợ")
            } else if (marketLocation.isNullOrEmpty()) {
                mView.edtLocation.requestFocus()
                mView.edtLocation.setError("Hãy nhập địa chỉ")
            } else if (qrCodeIn.isNullOrEmpty()) {
                mView.edtUrlQRIn.requestFocus()
                mView.edtUrlQRIn.setError("Hãy nhập url chứa mã QR vào chợ")
            } else if (qrCodeOut.isNullOrEmpty()) {
                mView.edtUrlQROut.requestFocus()
                mView.edtUrlQROut.setError("Hãy nhập url chứa mã QR ra chợ")
            } else insertMarket()
        }
    }


    private fun insertMarket() {
        val insertMarket: InsertMarket.Data
        insertMarket = InsertMarket.Data(marketName,marketLocation,qrCodeIn,qrCodeOut)
        disposable = insertApi.addMarket(insertMarket)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    findNavController().navigate(R.id.action_addMarketFragment_to_marketManagerFragment)
                    Log.i("abc", "abc: " + result.data.toString())
                    Toast.makeText(context, "${result.message}", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    Toast.makeText(context, "fail insert", Toast.LENGTH_SHORT).show()
                    Log.i("abc", "abc: " + error.localizedMessage + error.message + error)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }
}