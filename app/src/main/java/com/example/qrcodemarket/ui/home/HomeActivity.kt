package com.example.qrcodemarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.qrcodemarket.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNavigation.background = null
        floatingActionButton.setOnClickListener {
            bottomNavigation?.selectedItemId = R.id.scanFragment
        }
        val navController = findNavController(R.id.fragment)
        bottomNavigation.setupWithNavController(navController)
    }
}