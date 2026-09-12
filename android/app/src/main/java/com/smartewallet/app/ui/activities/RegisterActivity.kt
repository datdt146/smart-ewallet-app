package com.smartewallet.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartewallet.app.databinding.ActivityRegisterBinding
import com.smartewallet.app.di.ServiceLocator
import com.smartewallet.app.utils.ValidationUtils
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private val authRepository = ServiceLocator.getAuthRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            val firstName = binding.firstNameEditText.text.toString().trim()
            val lastName = binding.lastNameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            
            if (validateInputs(email, password, confirmPassword, firstName, lastName, phone)) {
                performRegister(email, password, firstName, lastName, phone)
            }
        }
        
        binding.loginLink.setOnClickListener {
            finish()
        }
    }
    
    private fun validateInputs(
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        phone: String
    ): Boolean {
        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required"
            return false
        }
        if (!ValidationUtils.isValidEmail(email)) {
            binding.emailEditText.error = "Invalid email format"
            return false
        }
        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required"
            return false
        }
        if (!ValidationUtils.isValidPassword(password)) {
            binding.passwordEditText.error = "Password must be at least 8 chars with uppercase, lowercase and digit"
            return false
        }
        if (password != confirmPassword) {
            binding.confirmPasswordEditText.error = "Passwords do not match"
            return false
        }
        if (firstName.isEmpty()) {
            binding.firstNameEditText.error = "First name is required"
            return false
        }
        if (lastName.isEmpty()) {
            binding.lastNameEditText.error = "Last name is required"
            return false
        }
        if (phone.isEmpty()) {
            binding.phoneEditText.error = "Phone is required"
            return false
        }
        if (!ValidationUtils.isValidPhone(phone)) {
            binding.phoneEditText.error = "Invalid phone format"
            return false
        }
        return true
    }
    
    private fun performRegister(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String
    ) {
        lifecycleScope.launch {
            setLoading(true)
            
            authRepository.register(email, password, firstName, lastName, phone)
                .onSuccess { response ->
                    setLoading(false)
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    // Navigate to login
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .onFailure { error ->
                    setLoading(false)
                    Toast.makeText(
                        this@RegisterActivity,
                        error.message ?: "Registration failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    
    private fun setLoading(isLoading: Boolean) {
        binding.registerButton.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}