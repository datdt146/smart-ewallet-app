package com.smartewallet.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartewallet.app.databinding.ActivityLoginBinding
import com.smartewallet.app.di.ServiceLocator
import com.smartewallet.app.utils.ValidationUtils
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val authRepository = ServiceLocator.getAuthRepository()
    private val tokenManager = ServiceLocator.getTokenManager()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            
            if (validateInputs(email, password)) {
                performLogin(email, password)
            }
        }
        
        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        
        binding.forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Forgot password feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun validateInputs(email: String, password: String): Boolean {
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
        
        return true
    }
    
    private fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            setLoading(true)
            
            authRepository.login(email, password).onSuccess { response ->
                if (response.requiresOtp) {
                    // Navigate to OTP verification
                    val intent = Intent(this@LoginActivity, OtpVerifyActivity::class.java)
                    intent.putExtra("token", response.token)
                    startActivity(intent)
                    finish()
                } else {
                    // Save token and navigate to dashboard
                    response.token?.let {
                        tokenManager.saveToken(it, response.refreshToken)
                    }
                    tokenManager.saveUserEmail(email)
                    
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.onFailure { error ->
                setLoading(false)
                Toast.makeText(
                    this@LoginActivity,
                    error.message ?: "Login failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun setLoading(isLoading: Boolean) {
        binding.loginButton.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}