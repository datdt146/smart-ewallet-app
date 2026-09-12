package com.smartewallet.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartewallet.app.databinding.ActivityOtpVerifyBinding
import com.smartewallet.app.di.ServiceLocator
import kotlinx.coroutines.launch

class OtpVerifyActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityOtpVerifyBinding
    private val authRepository = ServiceLocator.getAuthRepository()
    private val tokenManager = ServiceLocator.getTokenManager()
    private var tempToken: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        tempToken = intent.getStringExtra("token") ?: ""
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.verifyButton.setOnClickListener {
            val otpCode = binding.otpCodeEditText.text.toString().trim()
            
            if (otpCode.isEmpty()) {
                binding.otpCodeEditText.error = "OTP code is required"
                return@setOnClickListener
            }
            
            if (otpCode.length != 6) {
                binding.otpCodeEditText.error = "OTP code must be 6 digits"
                return@setOnClickListener
            }
            
            verifyOtp(otpCode)
        }
        
        binding.resendButton.setOnClickListener {
            Toast.makeText(this, "OTP resent to your email", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun verifyOtp(code: String) {
        lifecycleScope.launch {
            setLoading(true)
            
            authRepository.verifyOtp(code, tempToken)
                .onSuccess { response ->
                    setLoading(false)
                    
                    response.token?.let {
                        tokenManager.saveToken(it, response.refreshToken)
                    }
                    
                    Toast.makeText(this@OtpVerifyActivity, "OTP verified successfully", Toast.LENGTH_SHORT).show()
                    
                    val intent = Intent(this@OtpVerifyActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .onFailure { error ->
                    setLoading(false)
                    Toast.makeText(
                        this@OtpVerifyActivity,
                        error.message ?: "OTP verification failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    
    private fun setLoading(isLoading: Boolean) {
        binding.verifyButton.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}