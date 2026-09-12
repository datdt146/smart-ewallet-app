package com.smartewallet.app.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartewallet.app.databinding.ActivityTopupBinding
import com.smartewallet.app.di.ServiceLocator
import com.smartewallet.app.utils.ValidationUtils
import kotlinx.coroutines.launch

class TopupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTopupBinding
    private val transactionRepository = ServiceLocator.getTransactionRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.topupButton.setOnClickListener {
            performTopup()
        }
        
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    
    private fun performTopup() {
        val amount = binding.amountEditText.text.toString().trim()
        
        if (amount.isEmpty()) {
            binding.amountEditText.error = "Amount is required"
            return
        }
        
        if (!ValidationUtils.isValidAmount(amount)) {
            binding.amountEditText.error = "Invalid amount"
            return
        }
        
        lifecycleScope.launch {
            setLoading(true)
            
            transactionRepository.createVnpayPayment(amount.toDouble())
                .onSuccess { paymentResponse ->
                    setLoading(false)
                    // Redirect to VNPay payment URL
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentResponse.paymentUrl))
                    startActivity(intent)
                }
                .onFailure { error ->
                    setLoading(false)
                    Toast.makeText(
                        this@TopupActivity,
                        error.message ?: "Failed to create payment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    
    private fun setLoading(isLoading: Boolean) {
        binding.topupButton.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}