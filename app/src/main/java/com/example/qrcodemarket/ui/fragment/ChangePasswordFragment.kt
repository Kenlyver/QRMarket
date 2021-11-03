package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.network.QRApi
import com.example.qrcodemarket.ui.auth.AppPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_change_password.view.*
import kotlinx.android.synthetic.main.fragment_change_password.view.imgBackArrow


class ChangePasswordFragment : Fragment() {
    companion object {
        fun newInstance(): ChangePasswordFragment {
            return ChangePasswordFragment()
        }
    }

    lateinit var disposable: Disposable
    lateinit var loginName: String
    lateinit var currentPassword: String
    lateinit var newPassword: String
    lateinit var confirmPassword: String
    val insertApi by lazy {
        QRApi.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_change_password, container, false)
        mView.imgBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        mView.btnChangePass.setOnClickListener {
            currentPassword = edtOldPassword.text.toString()
            newPassword = edtNewPassword.text.toString()
            confirmPassword = edtConfirmPassword.text.toString()
            if(currentPassword.isNullOrEmpty()){
                edtOldPassword.requestFocus()
                edtOldPassword.setError("Vui lòng nhập mật khẩu cũ")
            }
            else if(newPassword.isNullOrEmpty()){
                edtNewPassword.requestFocus()
                edtNewPassword.setError("Hãy nhập mật khẩu mới")
            }
            else if(confirmPassword.isNullOrEmpty()){
                edtConfirmPassword.requestFocus()
                edtConfirmPassword.setError("Hãy nhập số điện thoại")
            }else updatePassword()
        }
        val imageHand: ImageView =mView.findViewById(R.id.imgHand)

        Glide.with(this)
            .load(R.drawable.hand)
            .into(imageHand);
        return mView.rootView
    }

    private fun updatePassword() {
        if (!newPassword.equals(confirmPassword)) {
            edtConfirmPassword.requestFocus()
            edtConfirmPassword.setError("Mật khẩu không khớp")
        } else {
            loginName = AppPreferences.username
            val dataPassword: UpdatePassword.Data
            dataPassword = UpdatePassword.Data(currentPassword,newPassword)
            disposable = insertApi.updatePassword(loginName, dataPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if(result.error == false){
                            edtOldPassword.setText("")
                            edtNewPassword.setText("")
                            edtConfirmPassword.setText("")
                            val builder = context?.let { AlertDialog.Builder(it) }
                            builder?.setTitle("Thành công!!")
                            builder?.setMessage("Mật khẩu đã được thay đổi, bạn có muốn thoát ?")

                            builder?.setPositiveButton("Thoát") { dialog, which ->
                                onClicksLogout()
                            }

                            builder?.setNegativeButton("Không") { dialog, which ->

                            }
                            builder?.show()
                            Log.i("abc", "success: " + result.message)
                        }
                        else{
                            val builder = context?.let { AlertDialog.Builder(it) }
                            builder?.setTitle("Lỗi!!")
                            builder?.setMessage("Thử lại?")

                            builder?.setPositiveButton("Có") { dialog, which ->

                            }

                            builder?.setNegativeButton("Không") { dialog, which ->

                            }
                            builder?.show()
                        }

                    },
                    { error ->
                        Log.i("abc", "fail: " + error.localizedMessage + error.message + error)
                        Toast.makeText(context!!, error.message, Toast.LENGTH_SHORT).show()
                    }
                )
        }
    }

    private fun onClicksLogout() {
        AppPreferences.isLogin = false
        AppPreferences.username = ""
        AppPreferences.password = ""
        AppPreferences.role = ""
        AppPreferences.fullname = ""

        findNavController().navigate(R.id.action_settingFragment2_to_loginFragment)
    }

}