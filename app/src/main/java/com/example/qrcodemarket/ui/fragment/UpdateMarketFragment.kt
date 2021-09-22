package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.model.UpdateMarket
import com.example.qrcodemarket.data.network.QRApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_update_market.edtImageQRIn
import kotlinx.android.synthetic.main.fragment_update_market.edtImageQROut
import kotlinx.android.synthetic.main.fragment_update_market.edtMarketLocation
import kotlinx.android.synthetic.main.fragment_update_market.edtNameMarket
import kotlinx.android.synthetic.main.fragment_update_market.view.*

class UpdateMarketFragment : Fragment() {

    companion object{
        fun newInstance(): UpdateMarketFragment{
            return UpdateMarketFragment()
        }
    }

    private val args by navArgs<UpdateMarketFragmentArgs>()
    lateinit var marketId: String
    lateinit var qrCodeManagementId: String
    lateinit var marketName:String
    lateinit var marketLocation:String
    lateinit var urlImageQRIn:String
    lateinit var urlImageQROut:String
    var disposable: Disposable? = null
    val insertApi by lazy {
        QRApi.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mView = inflater.inflate(R.layout.fragment_update_market, container, false)
        setHasOptionsMenu(true)
        val toolbar: Toolbar = mView.findViewById(R.id.toolbar_acc)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.show()
        val imageQRIn: ImageView = mView.findViewById(R.id.imgQRIn)
        val imageQROut: ImageView = mView.findViewById(R.id.imgQROut)


        mView.imgBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        marketId = args.detailMarrket.marketId
        marketName = args.detailMarrket.marketName
        qrCodeManagementId = args.detailMarrket.qrCodeManagementId
        marketLocation = args.detailMarrket.marketLocation
        urlImageQRIn = args.detailMarrket.imageQRCodeIn
        urlImageQROut = args.detailMarrket.imageQRCodeOut

        Glide.with(this)
            .load(urlImageQRIn)
            .into(imageQRIn)
        Glide.with(this)
            .load(urlImageQROut)
            .into(imageQROut)

        mView.edtNameMarket.setText(marketName)
        mView.edtMarketLocation.setText(marketLocation)
        mView.edtImageQRIn.setText(urlImageQRIn)
        mView.edtImageQROut.setText(urlImageQROut)

        mView.btnSaveChange.setOnClickListener {
            updateMarket()
        }


        return mView.rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    private fun updateMarket() {
        val updateData: UpdateMarket.Data
        val marketName = edtNameMarket.text.toString()
        val marketLocation = edtMarketLocation.text.toString()
        val imageQRCodeIn = edtImageQRIn.text.toString()
        val imageQRCodeOut = edtImageQROut.text.toString()
        updateData =
            UpdateMarket.Data(marketName, marketLocation, imageQRCodeIn, imageQRCodeOut, qrCodeManagementId)
        val marketIdInt = marketId.toInt()
        disposable = insertApi.updateMarket(marketIdInt, updateData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    if (result.error == false) {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_updateMarketFragment_to_dashboardFragment)
                    } else Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    Log.i("abc", "success: " + result.message)
                },
                { error ->
                    Log.i("abc", "fail: " + error.localizedMessage + error.message + error)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_delete, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.getItemId() == R.id.itemDelete){
            deleteMarket()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun deleteMarket() {
        disposable = insertApi.deleteMarket(marketName.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    if (result.error==false) {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_updateMarketFragment_to_dashboardFragment)
                    } else Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    Log.i("abc", "success: " + result.message)
                },
                { error ->
                    Log.i("abc", "fail: " + error.localizedMessage + error.message + error)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }
}