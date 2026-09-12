package com.smartewallet.app.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartewallet.app.databinding.ActivityTransferBinding
import com.smartewallet.app.di.ServiceLocator
import com.smartewallet.app.utils.ValidationUtils
import kotlinx.coroutines.launch

class TransferActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTransferBinding
    private val transactionRepository = ServiceLocator.getTransactionRepository()
    private val totpManager = ServiceLocator.getTotpManager()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.transferButton.setOnClickListener {
            performTransfer()
        }
        
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    
    private fun performTransfer() {
        val recipientEmail = binding.recipientEmailEditText.text.toString().trim()
        val amount = binding.amountEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val pin = binding.pinEditText.text.toString()
        val otp = binding.otpEditText.text.toString().trim()
        
        if (!validateInputs(recipientEmail, amount, pin, otp)) {
            return
        }
        
        lifecycleScope.launch {
            setLoading(true)
            
            transactionRepository.transfer(
                toEmail = recipientEmail,
                amount = amount.toDouble(),
                description = description,
                pin = pin,
                otp = otp
            ).onSuccess { transaction ->
                setLoading(false)
                Toast.makeText(this@TransferActivity, "Transfer successful", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { error ->
                setLoading(false)
                Toast.makeText(
                    this@TransferActivity,
                    error.message ?: "Transfer failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun validateInputs(
        recipientEmail: String,
        amount: String,
        pin: String,
        otp: String
    ): Boolean {
        if (recipientEmail.isEmpty()) {
            binding.recipientEmailEditText.error = "Recipient email is required"
            return false
        }
        if (!ValidationUtils.isValidEmail(recipientEmail)) {
            binding.recipientEmailEditText.error = "Invalid email format"
            return false
        }
        if (amount.isEmpty()) {
            binding.amountEditText.error = "Amount is required"
            return false
        }
        if (!ValidationUtils.isValidAmount(amount)) {
            binding.amountEditText.error = "Invalid amount"
            return false
        }
        if (pin.isEmpty()) {
            binding.pinEditText.error = "PIN is required"
            return false
        }
        if (!ValidationUtils.isValidPin(pin)) {
            binding.pinEditText.error = "PIN must be 4-6 digits"
            return false
        }
        if (otp.isEmpty()) {
            binding.otpEditText.error = "OTP is required"
            return false
        }
        if (otp.length != 6) {
            binding.otpEditText.error = "OTP must be 6 digits"
            return false
        }
        return true
    }
    
    private fun setLoading(isLoading: Boolean) {
        binding.transferButton.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}