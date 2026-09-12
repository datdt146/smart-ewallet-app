package com.smartewallet.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartewallet.app.databinding.ActivityMainBinding
import com.smartewallet.app.di.ServiceLocator
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val walletRepository = ServiceLocator.getWalletRepository()
    private val tokenManager = ServiceLocator.getTokenManager()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        loadBalance()
    }
    
    private fun setupUI() {
        binding.transferButton.setOnClickListener {
            startActivity(Intent(this, TransferActivity::class.java))
        }
        
        binding.topupButton.setOnClickListener {
            startActivity(Intent(this, TopupActivity::class.java))
        }
        
        binding.historyButton.setOnClickListener {
            Toast.makeText(this, "Transaction history", Toast.LENGTH_SHORT).show()
        }
        
        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        binding.logoutButton.setOnClickListener {
            logout()
        }
        
        binding.refreshButton.setOnClickListener {
            loadBalance()
        }
    }
    
    private fun loadBalance() {
        lifecycleScope.launch {
            setLoading(true)
            
            walletRepository.getBalance()
                .onSuccess { wallet ->
                    setLoading(false)
                    val formatter = DecimalFormat("#,##0.00")
                    binding.balanceText.text = formatter.format(wallet.balance) + " VND"
                }
                .onFailure { error ->
                    setLoading(false)
                    Toast.makeText(
                        this@MainActivity,
                        error.message ?: "Failed to load balance",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    
    private fun logout() {
        lifecycleScope.launch {
            tokenManager.clearToken()
            
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    
    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}