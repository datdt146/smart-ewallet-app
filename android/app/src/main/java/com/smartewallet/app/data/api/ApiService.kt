package com.smartewallet.app.data.api

import com.smartewallet.app.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // ============ Auth Endpoints ============
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("/auth/setup-otp")
    suspend fun setupOtp(): Response<AuthResponse>
    
    @POST("/auth/verify-otp")
    suspend fun verifyOtp(@Body request: OtpVerifyRequest): Response<AuthResponse>
    
    @POST("/auth/refresh-token")
    suspend fun refreshToken(): Response<AuthResponse>
    
    // ============ User Endpoints ============
    @GET("/users/profile")
    suspend fun getProfile(): Response<ApiResponse<User>>
    
    @PUT("/users/profile")
    suspend fun updateProfile(@Body user: User): Response<ApiResponse<User>>
    
    @PUT("/users/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<ApiResponse<Any>>
    
    @PUT("/users/pin")
    suspend fun setPin(@Body request: SetPinRequest): Response<ApiResponse<Any>>
    
    @PUT("/users/change-pin")
    suspend fun changePin(@Body request: ChangePinRequest): Response<ApiResponse<Any>>
    
    // ============ Wallet Endpoints ============
    @GET("/wallet/balance")
    suspend fun getBalance(): Response<ApiResponse<Wallet>>
    
    @POST("/wallet/transfer")
    suspend fun transfer(@Body request: TransferRequest): Response<ApiResponse<Transaction>>
    
    // ============ Transactions Endpoints ============
    @GET("/transactions/history")
    suspend fun getTransactionHistory(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<Transaction>>>
    
    @GET("/transactions/{id}")
    suspend fun getTransaction(@Path("id") id: String): Response<ApiResponse<Transaction>>
    
    // ============ VNPay Endpoints ============
    @POST("/vnpay/create-payment")
    suspend fun createVnpayPayment(
        @Body request: VnpayRequest
    ): Response<ApiResponse<VnpayResponse>>
    
    @GET("/vnpay/callback")
    suspend fun vnpayCallback(
        @QueryMap params: Map<String, String>
    ): Response<ApiResponse<Any>>
}

// Request/Response classes
data class ApiResponse<T>(
    @com.google.gson.annotations.SerializedName("success")
    val success: Boolean = false,
    
    @com.google.gson.annotations.SerializedName("message")
    val message: String = "",
    
    @com.google.gson.annotations.SerializedName("data")
    val data: T? = null
)

data class ChangePasswordRequest(
    @com.google.gson.annotations.SerializedName("oldPassword")
    val oldPassword: String,
    
    @com.google.gson.annotations.SerializedName("newPassword")
    val newPassword: String
)

data class SetPinRequest(
    @com.google.gson.annotations.SerializedName("pin")
    val pin: String,
    
    @com.google.gson.annotations.SerializedName("otp")
    val otp: String
)

data class ChangePinRequest(
    @com.google.gson.annotations.SerializedName("oldPin")
    val oldPin: String,
    
    @com.google.gson.annotations.SerializedName("newPin")
    val newPin: String,
    
    @com.google.gson.annotations.SerializedName("otp")
    val otp: String
)

data class TransferRequest(
    @com.google.gson.annotations.SerializedName("toEmail")
    val toEmail: String,
    
    @com.google.gson.annotations.SerializedName("amount")
    val amount: Double,
    
    @com.google.gson.annotations.SerializedName("description")
    val description: String = "",
    
    @com.google.gson.annotations.SerializedName("pin")
    val pin: String,
    
    @com.google.gson.annotations.SerializedName("otp")
    val otp: String
)

data class VnpayRequest(
    @com.google.gson.annotations.SerializedName("amount")
    val amount: Double,
    
    @com.google.gson.annotations.SerializedName("description")
    val description: String = "Nạp tiền vào ví"
)

data class VnpayResponse(
    @com.google.gson.annotations.SerializedName("paymentUrl")
    val paymentUrl: String,
    
    @com.google.gson.annotations.SerializedName("transactionId")
    val transactionId: String
)