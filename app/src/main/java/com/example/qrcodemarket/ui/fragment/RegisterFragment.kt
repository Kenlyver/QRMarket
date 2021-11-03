package com.example.qrcodemarket.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.model.DetailUser
import com.example.qrcodemarket.data.network.MyApi
import com.example.qrcodemarket.data.network.response.NetworkConnectionInterceptor
import com.example.qrcodemarket.data.repositories.UserRepository
import com.example.qrcodemarket.databinding.FragmentRegisterBinding
import com.example.qrcodemarket.ui.auth.AuthViewModel
import com.example.qrcodemarket.ui.auth.AuthViewModelFactory
import kotlinx.android.synthetic.main.fragment_register.view.*
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    lateinit var mView:View
    lateinit var viewModel: AuthViewModel
    lateinit var fullName:String
    lateinit var dateOfBirth:String
    lateinit var numberPhone:String
    lateinit var loginName:String
    lateinit  var password:String

    private var mDatePickerDialog: DatePickerDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )
        mView = binding.root

        val networkConnectionInterceptor = context?.let { NetworkConnectionInterceptor(it) }
        val api = networkConnectionInterceptor?.let { MyApi(it) }
        val repository = api?.let { UserRepository(it) }
        val factory = repository?.let { AuthViewModelFactory(it) }

        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)

        setDateTimeField()

        mView.edtDateOfBirth.setOnTouchListener(
            object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    mDatePickerDialog!!.show()
                    return false
                }
            })

        binding.btnContinue.setOnClickListener {
            fullName = binding.edtFullName.text.toString()
            dateOfBirth = binding.edtDateOfBirth.text.toString()
            numberPhone = binding.edtNumberPhone.text.toString()
            loginName = binding.edtLoginName.text.toString()
            password = binding.edtPassword.text.toString()
            if(fullName.isNullOrEmpty()){
                mView.edtFullName.requestFocus()
                mView.edtFullName.setError("Hãy nhập họ tên")
            }
            else if(dateOfBirth.isNullOrEmpty()){
                mView.edtDateOfBirth.setError("Hãy nhập ngày tháng năm sinh")
            }
            else if(numberPhone.isNullOrEmpty()){
                mView.edtNumberPhone.requestFocus()
                mView.edtNumberPhone.setError("Vui lòng nhập số điện thoại")
            }
            else if(loginName.isNullOrEmpty() || loginName!!.length<8){
                mView.edtLoginName.requestFocus()
                mView.edtLoginName.setError("Tên đăng nhập không thể trống hoặc ít hơn 8 kí tự")
            }
            else if(password.isNullOrEmpty() || password!!.length<6 ){
                mView.edtPassword.requestFocus()
                mView.edtPassword.setError("Mật khẩu không thể trống hoặc ít hơn 6 kí tự")
            }
            else {
                val action = RegisterFragmentDirections.actionRegisterFragmentToCountryFragment(DetailUser(fullName, dateOfBirth, numberPhone, loginName, password))
                findNavController().navigate(action)
            }
        }

        mView.txtSingIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return mView
    }

    private fun setDateTimeField() {
        val newCalendar: Calendar = Calendar.getInstance()
        mDatePickerDialog = context?.let {
            DatePickerDialog(
                it, R.style.DialogTheme,
                { view, year, monthOfYear, dayOfMonth ->
                    val newDate: Calendar = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    val sd = SimpleDateFormat("dd-MM-yyyy")
                    val startDate: Date = newDate.getTime()
                    val fdate: String = sd.format(startDate)
                    mView.edtDateOfBirth.setText(fdate)
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        mDatePickerDialog?.getDatePicker()?.setMaxDate(System.currentTimeMillis())
    }
}