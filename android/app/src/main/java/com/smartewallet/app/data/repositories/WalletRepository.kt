package com.smartewallet.app.data.repositories

import com.smartewallet.app.data.api.ApiService
import com.smartewallet.app.data.models.Wallet
import retrofit2.HttpException
import java.io.IOException

class WalletRepository(private val apiService: ApiService) {
    
    suspend fun getBalance(): Result<Wallet> {
        return try {
            val response = apiService.getBalance()
            
            if (response.isSuccessful && response.body()?.success == true) {
                val wallet = response.body()?.data
                if (wallet != null) {
                    Result.success(wallet)
                } else {
                    Result.failure(Exception("Wallet data is null"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get balance"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}