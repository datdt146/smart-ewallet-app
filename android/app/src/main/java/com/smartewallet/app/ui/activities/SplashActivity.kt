package com.smartewallet.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.smartewallet.app.R
import com.smartewallet.app.utils.TokenManager
import com.smartewallet.app.di.ServiceLocator

class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        tokenManager = ServiceLocator.getTokenManager()
        
        // Delay 2 seconds then navigate
        handler.postDelayed({
            navigateNext()
        }, 2000)
    }
    
    private fun navigateNext() {
        val token = tokenManager.getToken()
        
        val intent = if (token.isNullOrEmpty()) {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        
        startActivity(intent)
        finish()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}