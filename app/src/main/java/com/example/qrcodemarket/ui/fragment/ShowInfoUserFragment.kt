package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.network.QRApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_show_info_user.view.*

class ShowInfoUserFragment : Fragment() {

    lateinit var mView: View
    private val args by navArgs<ShowInfoUserFragmentArgs>()
    lateinit var fullName: String
    var dateOfBirth: String? = null
    var numberPhone: String? = null
    var address: String? = null
    var ward: String? = null
    var district: String? = null
    var province: String? = null
    var disposable: Disposable? = null
    val insertApi by lazy {
        QRApi.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_show_info_user, container, false)
        fullName = args.fullName
        mView.imgBackk.setOnClickListener {
            findNavController().popBackStack()
        }

        getInfoUser()
        return mView.rootView
    }

    private fun getInfoUser() {
        disposable = insertApi.getInfoCitizen(fullName!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    fullName = result.data.fullName
                    dateOfBirth = result.data.dateOfBirth
                    address = result.data.address
                    numberPhone = result.data.numberPhone
                    ward = result.data.ward
                    district = result.data.district
                    province = result.data.province

                    mView.txtFullNameUser.setText(fullName)
                    mView.txtDateOfBirthUser.setText(dateOfBirth)
                    mView.txtNumberPhoneUser.setText(numberPhone)
                    mView.txtAddressUser.setText(address)
                    mView.txtWardUser.setText(ward)
                    mView.txtDistrictUser.setText(district)
                    mView.txtProvinceUser.setText(province)
                    Log.i("abc", "abc: " + result.data.toString())
                },
                { error ->
                    Log.i("abc", "abc: " + error.localizedMessage + error.message + error)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }
}