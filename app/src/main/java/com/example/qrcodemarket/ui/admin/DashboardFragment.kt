package com.example.qrcodemarket.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qrcodemarket.R
import com.example.qrcodemarket.ui.auth.AppPreferences
import kotlinx.android.synthetic.main.fragment_dashboard.view.*


class DashboardFragment : Fragment() {

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mView = inflater.inflate(R.layout.fragment_dashboard, container, false)


        mView.txtAdminName.setText(AppPreferences.fullname)

        mView.imgAccount.setOnClickListener {
            AppPreferences.isLogin = false
            AppPreferences.username = ""
            AppPreferences.password = ""
            AppPreferences.role = ""
            AppPreferences.fullname = ""
            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }
        mView.marketManager.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_marketManagerFragment)
        }

        mView.statistical.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_statisticalFragment)
        }

        mView.profile.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_accountSettingFragment)
        }
        return mView.rootView
    }
}