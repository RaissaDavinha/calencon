package com.example.calencon.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.calencon.R
import com.example.calencon.data.remote.WebServiceClient
import com.example.calencon.presentation.home.HomeActivity
import com.example.calencon.presentation.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val webService: WebServiceClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        Handler().postDelayed({
            startActivity()
        }, 2000.toLong())
    }

    private fun startActivity() {
        // Check if user is signed in (non-null)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            webService.createService()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}