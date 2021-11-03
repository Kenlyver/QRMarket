package com.example.qrcodemarket.ui.auth

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import com.example.qrcodemarket.data.respositories.UserRepository
import com.example.qrcodemarket.util.ApiExceptions
import com.example.qrcodemarket.util.Coroutines
import com.example.qrcodemarket.util.NoInternetException


class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {


    lateinit var loginName: String
    lateinit var password: String
    lateinit var fullName: String
    lateinit var dateOfBirth: String
    lateinit var numberPhone: String

    lateinit var authListener: AuthListener


    fun onLoginButtonClick(view: View) {
        authListener.onStarted()
        if (loginName.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener.onFailure("Invalid login name or password ")
            return
        }
        Coroutines.main {
            try {
                val authResponse = repository.userLogin(loginName, password)

                authResponse?.user?.let {
                    authListener.onSuccess(it)
                    AppPreferences.isLogin = true
                    AppPreferences.username = loginName as String
                    AppPreferences.password = password as String
                    AppPreferences.role = authResponse.user.role as String
                    return@main
                }
                authResponse?.message?.let { authListener.onFailure(it) }
            } catch (e: ApiExceptions) {
                e.message?.let { authListener.onFailure(it) }
            } catch (e: NoInternetException) {
                e.message?.let { authListener.onFailure(it) }
            }
        }
    }
}
