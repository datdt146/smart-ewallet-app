package com.smartewallet.app.data.models

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("success")
    val success: Boolean = false,
    
    @SerializedName("message")
    val message: String = "",
    
    @SerializedName("token")
    val token: String? = null,
    
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    
    @SerializedName("user")
    val user: User? = null,
    
    @SerializedName("requiresOtp")
    val requiresOtp: Boolean = false,
    
    @SerializedName("otpSecret")
    val otpSecret: OtpSecret? = null
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("firstName")
    val firstName: String,
    
    @SerializedName("lastName")
    val lastName: String,
    
    @SerializedName("phone")
    val phone: String
)

data class OtpVerifyRequest(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("token")
    val token: String
)