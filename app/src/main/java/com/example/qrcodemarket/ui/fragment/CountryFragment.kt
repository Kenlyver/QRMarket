package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.model.InsertUser
import com.example.qrcodemarket.data.network.ApiProvince
import com.example.qrcodemarket.data.network.QRApi
import com.example.qrcodemarket.util.snackbar
import com.example.qrcodemarket.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_country.view.*

class CountryFragment : Fragment() {

    lateinit var optionProvince: Spinner
    var provinces: MutableList<String> = mutableListOf<String>()
    var provincesId: MutableList<Int> = mutableListOf<Int>()

    lateinit var optionDistrict: Spinner
    var districts: MutableList<String> = mutableListOf<String>()
    var districtId: MutableList<Int> = mutableListOf<Int>()
    var provincesIdInDistrict: MutableList<Int> = mutableListOf<Int>()

    lateinit var optionWard: Spinner
    var wards: MutableList<String> = mutableListOf<String>()
    var wardId: MutableList<Int> = mutableListOf<Int>()
    var districtIdInward: MutableList<Int> = mutableListOf<Int>()

    lateinit var progressBar: ProgressBar

    var disposable: Disposable? = null
    lateinit var getProvince: String
    lateinit var getDistrict: String
    lateinit var getWard: String
    lateinit var getAddress: String
    lateinit var fullName: String
    lateinit var dateOfBirth: String
    lateinit var numberPhone: String
    lateinit var loginName: String
    lateinit var password: String

    lateinit var mView: View

    private val args by navArgs<CountryFragmentArgs>()
    val province by lazy {
        ApiProvince.create()
    }
    val insertApi by lazy {
        QRApi.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_country, container, false)

        fullName = args.detailUser.fullName
        dateOfBirth = args.detailUser.dateOfBirth
        numberPhone = args.detailUser.numberPhone
        loginName = args.detailUser.loginName
        password = args.detailUser.password


        optionProvince = mView.findViewById(R.id.province_spinner) as Spinner
        optionDistrict = mView.findViewById(R.id.district_spinner) as Spinner
        optionWard = mView.findViewById(R.id.ward_spinner) as Spinner
        progressBar = mView.findViewById(R.id.progress)


        var btnAccept: Button = mView.findViewById(R.id.btnAccept)
        var edtAddress: EditText = mView.findViewById(R.id.edtAddress)

        getProvince()
        setSpinnerItem()

        btnAccept.setOnClickListener {
            getAddress = edtAddress.text.toString()
            if (getAddress.isNullOrEmpty() || getProvince.isNullOrEmpty() || getDistrict.isNullOrEmpty() || getWard.isNullOrEmpty()) {
                mView.country_layout.snackbar("Vui lòng nhập địa chỉ!")
            } else {
                submitData()

            }
        }

        val imageAvoidContact: ImageView = mView.findViewById(R.id.imgAvoidContact)

        Glide.with(this)
            .load(R.drawable.avoidcontact)
            .into(imageAvoidContact);

        return mView.rootView
    }

    private fun submitData() {
        val insertUser: InsertUser.Data
        insertUser = InsertUser.Data(
            loginName,
            password,
            fullName,
            dateOfBirth,
            numberPhone,
            getProvince,
            getDistrict,
            getWard,
            getAddress
        )
        disposable = insertApi.userSignUp(insertUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.i("abc", "abc: " + result.data.toString())
                    findNavController().navigate(R.id.action_countryFragment_to_loginFragment)
                    Toast.makeText(context, "${result.message}", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    Toast.makeText(context, "Register fail, please try again!!", Toast.LENGTH_SHORT).show()
                    Log.i("abc", "abc: " + error.localizedMessage + error.message + error)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun setSpinnerItem() {
        val adapterProvinsi = activity?.let {
            ArrayAdapter(
                it,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.Province)
            )
        }
        adapterProvinsi?.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        optionProvince.adapter = adapterProvinsi

        val adapterDistrict = activity?.let {
            ArrayAdapter(
                it,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.District)
            )
        }
        adapterProvinsi?.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        optionDistrict.adapter = adapterDistrict

        val adapterWard = activity?.let {
            ArrayAdapter(
                it,
                R.layout.spinner_single_simple, resources.getStringArray(R.array.Ward)
            )
        }
        adapterWard?.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        optionWard.adapter = adapterWard

    }

    private fun getProvince() {
        disposable = province.getDataProvince()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    provinces.clear()
                    provincesId.clear()

                    provinces.add("Tỉnh")
                    provincesId.add(0)


                    result.data.forEach {
                        provincesId.add(it.id)
                        provinces.add(it.name)
                    }

                    val adapterProvinsi = activity?.let {
                        ArrayAdapter(
                            it,
                            R.layout.spinner_single_simple, provinces
                        )
                    }
                    adapterProvinsi?.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    optionProvince.adapter = adapterProvinsi

                    provinceLoad(false)

                },
                { error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    provinceLoad(false)
                }
            )
        optionProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (provincesId.count() > 0 && position > 0) {
                    getProvince = parent?.getItemAtPosition(position).toString()

                    getDistrict(provincesId[position])
                }
            }
        }
    }

    private fun provinceLoad(status: Boolean) {
        progressBar.visibility = if (status) View.VISIBLE else View.GONE
        optionProvince.isEnabled = !status
        optionDistrict.isEnabled = false
        optionWard.isEnabled = false
        mView.btnAccept.isEnabled = false
    }

    private fun getDistrict(id: Int) {
        disposable = province.getDataDistrict()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
//                    Toast.makeText(this, "${result.data}", Toast.LENGTH_SHORT).show()

                    districts.clear()
                    districtId.clear()
                    provincesIdInDistrict.clear()

                    districts.add("Huyện")
                    districtId.add(0)
                    provincesIdInDistrict.add(0)


                    result.data.forEach {
                        if (it.idProvince == id) {
                            provincesIdInDistrict.add(it.idProvince)
                            districtId.add(it.idDistrict)
                            districts.add(it.name)
                        }
                    }

                    val adapterDistrict = activity?.let {
                        ArrayAdapter(
                            it,
                            R.layout.spinner_single_simple, districts
                        )
                    }
                    adapterDistrict?.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    optionDistrict.adapter = adapterDistrict

                    districtLoad(false)

                },
                { error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    districtLoad(false)
                }
            )
        optionDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (districtId.count() > 0 && position > 0) {
                    getDistrict = parent?.getItemAtPosition(position).toString()
                    getWard(districtId[position])
                }
            }
        }
    }


    private fun districtLoad(status: Boolean) {
        progressBar.visibility = if (status) View.VISIBLE else View.GONE
        optionProvince.isEnabled = !status
        optionDistrict.isEnabled = !status
        optionWard.isEnabled = false
        mView.btnAccept.isEnabled = false
    }

    private fun getWard(id: Int) {
        disposable = province.getDataWard()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    wards.clear()
                    wardId.clear()
                    districtIdInward.clear()

                    wards.add("Xã")
                    wardId.add(0)
                    districtIdInward.add(0)


                    result.data.forEach {
                        if (it.idDistrict == id) {
                            districtIdInward.add(it.idDistrict)
                            wardId.add(it.idWard)
                            wards.add(it.name)
                        }
                    }

                    val adapterWard = activity?.let {
                        ArrayAdapter(
                            it,
                            R.layout.spinner_single_simple, wards
                        )
                    }
                    adapterWard?.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    optionWard.adapter = adapterWard

                    wardLoad(false)

                },
                { error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    wardLoad(false)
                }
            )
        optionWard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (wardId.count() > 0 && position > 0) {
                    getWard = parent?.getItemAtPosition(position).toString()
                }
            }
        }
    }

    private fun wardLoad(status: Boolean) {
        progressBar.visibility = if (status) View.VISIBLE else View.GONE
        optionProvince.isEnabled = !status
        optionDistrict.isEnabled = !status
        optionWard.isEnabled = !status
        mView.btnAccept.isEnabled = !status
    }
}