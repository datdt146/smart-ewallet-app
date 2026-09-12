package com.smartewallet.app.di

import android.content.Context
import com.smartewallet.app.data.api.ApiClient
import com.smartewallet.app.data.repositories.AuthRepository
import com.smartewallet.app.data.repositories.WalletRepository
import com.smartewallet.app.data.repositories.TransactionRepository
import com.smartewallet.app.utils.TokenManager
import com.smartewallet.app.utils.TotpManager

object ServiceLocator {
    private lateinit var context: Context
    
    // API
    private lateinit var apiClient: ApiClient
    
    // Repositories
    private lateinit var authRepository: AuthRepository
    private lateinit var walletRepository: WalletRepository
    private lateinit var transactionRepository: TransactionRepository
    
    // Utils
    private lateinit var tokenManager: TokenManager
    private lateinit var totpManager: TotpManager
    
    fun init(appContext: Context) {
        context = appContext
        
        // Initialize managers
        tokenManager = TokenManager(context)
        totpManager = TotpManager()
        
        // Initialize API
        apiClient = ApiClient(tokenManager)
        
        // Initialize repositories
        authRepository = AuthRepository(apiClient)
        walletRepository = WalletRepository(apiClient)
        transactionRepository = TransactionRepository(apiClient)
    }
    
    fun getAuthRepository(): AuthRepository = authRepository
    fun getWalletRepository(): WalletRepository = walletRepository
    fun getTransactionRepository(): TransactionRepository = transactionRepository
    fun getTokenManager(): TokenManager = tokenManager
    fun getTotpManager(): TotpManager = totpManager
    fun getContext(): Context = context
}