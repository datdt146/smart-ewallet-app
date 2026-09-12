package com.smartewallet.app.data.repositories

import com.smartewallet.app.data.api.ApiService
import com.smartewallet.app.data.models.*
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(private val apiService: ApiService) {
    
    suspend fun register(email: String, password: String, firstName: String, lastName: String, phone: String): Result<AuthResponse> {
        return try {
            val request = RegisterRequest(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                phone = phone
            )
            val response = apiService.register(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Registration failed"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val request = LoginRequest(email = email, password = password)
            val response = apiService.login(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Login failed"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun setupOtp(): Result<AuthResponse> {
        return try {
            val response = apiService.setupOtp()
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "OTP setup failed"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun verifyOtp(code: String, token: String): Result<AuthResponse> {
        return try {
            val request = OtpVerifyRequest(code = code, token = token)
            val response = apiService.verifyOtp(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "OTP verification failed"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP Error: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshToken(): Result<AuthResponse> {
        return try {
            val response = apiService.refreshToken()
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Token refresh failed"))
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