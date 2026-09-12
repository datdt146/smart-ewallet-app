package com.smartewallet.app.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartewallet.app.databinding.ActivityProfileBinding
import com.smartewallet.app.di.ServiceLocator
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProfileBinding
    private val authRepository = ServiceLocator.getAuthRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.backButton.setOnClickListener {
            finish()
        }
        
        binding.editButton.setOnClickListener {
            Toast.makeText(this, "Edit profile feature coming soon", Toast.LENGTH_SHORT).show()
        }
        
        binding.changePasswordButton.setOnClickListener {
            Toast.makeText(this, "Change password feature coming soon", Toast.LENGTH_SHORT).show()
        }
        
        binding.changePinButton.setOnClickListener {
            Toast.makeText(this, "Change PIN feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }
}