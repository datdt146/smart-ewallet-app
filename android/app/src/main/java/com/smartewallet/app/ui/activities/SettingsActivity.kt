package com.smartewallet.app.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartewallet.app.databinding.ActivitySettingsBinding
import com.smartewallet.app.di.ServiceLocator

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private val totpManager = ServiceLocator.getTotpManager()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.backButton.setOnClickListener {
            finish()
        }
        
        binding.setupOtpButton.setOnClickListener {
            Toast.makeText(this, "Setup OTP feature coming soon", Toast.LENGTH_SHORT).show()
        }
        
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(
                this,
                if (isChecked) "Notifications enabled" else "Notifications disabled",
                Toast.LENGTH_SHORT
            ).show()
        }
        
        binding.aboutButton.setOnClickListener {
            Toast.makeText(this, "Smart E-Wallet v1.0.0", Toast.LENGTH_SHORT).show()
        }
    }
}