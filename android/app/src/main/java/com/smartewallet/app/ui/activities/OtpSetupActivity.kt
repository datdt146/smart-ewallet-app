package com.smartewallet.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartewallet.app.databinding.ActivityOtpSetupBinding
import com.smartewallet.app.di.ServiceLocator
import kotlinx.coroutines.launch

class OtpSetupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityOtpSetupBinding
    private val authRepository = ServiceLocator.getAuthRepository()
    private val totpManager = ServiceLocator.getTotpManager()
    private var otpSecret: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupOtp()
    }
    
    private fun setupOtp() {
        lifecycleScope.launch {
            setLoading(true)
            
            authRepository.setupOtp()
                .onSuccess { response ->
                    setLoading(false)
                    response.otpSecret?.let { secret ->
                        otpSecret = secret.secret
                        
                        // Generate and display QR code
                        val email = ServiceLocator.getTokenManager().getUserEmailFlow()
                        val qrBitmap = totpManager.generateQrCode("user@example.com", otpSecret)
                        qrBitmap?.let {
                            binding.qrCodeImageView.setImageBitmap(it)
                        }
                        
                        // Display manual entry code
                        binding.secretKeyText.text = otpSecret
                        
                        // Display backup codes
                        val backupCodes = secret.backupCodes.joinToString("\n")
                        binding.backupCodesText.text = backupCodes
                        
                        // Setup button click listener
                        binding.verifyButton.setOnClickListener {
                            verifyAndSaveOtp()
                        }
                    }
                }
                .onFailure { error ->
                    setLoading(false)
                    Toast.makeText(
                        this@OtpSetupActivity,
                        error.message ?: "OTP setup failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    
    private fun verifyAndSaveOtp() {
        val otpCode = binding.otpCodeEditText.text.toString().trim()
        
        if (otpCode.isEmpty() || otpCode.length != 6) {
            binding.otpCodeEditText.error = "Enter 6-digit OTP code"
            return
        }
        
        if (totpManager.verifyCode(otpSecret, otpCode)) {
            Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show()
            // OTP setup is successful, navigate back or to dashboard
            finish()
        } else {
            Toast.makeText(this, "Invalid OTP code", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setLoading(isLoading: Boolean) {
        binding.verifyButton.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}