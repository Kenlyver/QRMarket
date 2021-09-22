package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.network.MyApi
import com.example.qrcodemarket.data.network.response.NetworkConnectionInterceptor
import com.example.qrcodemarket.data.network.response.User
import com.example.qrcodemarket.data.respositories.UserRepository
import com.example.qrcodemarket.databinding.FragmentLoginBinding
import com.example.qrcodemarket.ui.auth.*
import com.example.qrcodemarket.util.hide
import com.example.qrcodemarket.util.show
import com.example.qrcodemarket.util.snackbar
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment() : Fragment(), AuthListener {
    lateinit var viewModel: AuthViewModel
    lateinit var mView:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        context?.let { AppPreferences.init(it) }
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        mView = binding.root

        if (AppPreferences.isLogin) {
            if (AppPreferences.role.equals("Admin")) {
                findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_historyFragment)
            }
        } else {
            val networkConnectionInterceptor = context?.let { NetworkConnectionInterceptor(it) }
            val api = networkConnectionInterceptor?.let { MyApi(it) }
            val repository = api?.let { UserRepository(it) }
            val factory = repository?.let { AuthViewModelFactory(it) }
            viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)

            binding.viewModel = viewModel
            viewModel.authListener = this

        }

        mView.txtSingUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return mView
    }

    override fun onStarted() {
        mView.progress_bar.show()
    }

    override fun onSuccess(user: User) {
        mView.progress_bar.hide()
        var role: String = user.role.toString()
        var fullName: String = user.fullName.toString()
        var citizenId: String = user.citizenid.toString()
        AppPreferences.citizenId = citizenId
        AppPreferences.role = role
        AppPreferences.fullname = fullName
        if (role.equals("Admin")) {
            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        } else {
            findNavController().navigate(R.id.action_loginFragment_to_historyFragment)
        }
    }

    override fun onFailure(message: String) {
        mView.progress_bar.hide()
        mView.login.snackbar(message)
    }
}