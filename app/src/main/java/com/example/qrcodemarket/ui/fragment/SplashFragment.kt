package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.qrcodemarket.R
import kotlinx.android.synthetic.main.activity_home.*


class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.bottomNavigation?.visibility = View.GONE
        activity?.floatingActionButton?.visibility = View.GONE
        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        },5000)
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        return view
    }


}