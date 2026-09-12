package com.smartewallet.app.data.repositories

import com.smartewallet.app.data.api.ApiService
import com.smartewallet.app.data.api.TransferRequest
import com.smartewallet.app.data.api.VnpayRequest
import com.smartewallet.app.data.models.Transaction
import com.smartewallet.app.data.models.VnpayResponse
import retrofit2.HttpException
import java.io.IOException

class TransactionRepository(private val apiService: ApiService) {
    
    suspend fun transfer(
        toEmail: String,
        amount: Double,
        description: String,
        pin: String,
        otp: String
    ): Result<Transaction> {
        return try {
            val request = TransferRequest(
                toEmail = toEmail,
                amount = amount,
                description = description,
                pin = pin,
                otp = otp
            )
            val response = apiService.transfer(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val transaction = response.body()?.data
                if (transaction != null) {
                    Result.success(transaction)
                } else {
                    Result.failure(Exception("Transaction data is null"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Transfer failed"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTransactionHistory(page: Int = 1, limit: Int = 20): Result<List<Transaction>> {
        return try {
            val response = apiService.getTransactionHistory(page, limit)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val transactions = response.body()?.data ?: emptyList()
                Result.success(transactions)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get history"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTransaction(id: String): Result<Transaction> {
        return try {
            val response = apiService.getTransaction(id)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val transaction = response.body()?.data
                if (transaction != null) {
                    Result.success(transaction)
                } else {
                    Result.failure(Exception("Transaction data is null"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get transaction"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createVnpayPayment(amount: Double): Result<VnpayResponse> {
        return try {
            val request = VnpayRequest(amount = amount)
            val response = apiService.createVnpayPayment(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                val paymentResponse = response.body()?.data
                if (paymentResponse != null) {
                    Result.success(paymentResponse)
                } else {
                    Result.failure(Exception("Payment response is null"))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create payment"))
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