package com.smartewallet.app.data.models

import com.google.gson.annotations.SerializedName

data class Wallet(
    @SerializedName("id")
    val id: String = "",
    
    @SerializedName("userId")
    val userId: String = "",
    
    @SerializedName("balance")
    val balance: Double = 0.0,
    
    @SerializedName("currency")
    val currency: String = "VND",
    
    @SerializedName("isActive")
    val isActive: Boolean = true,
    
    @SerializedName("createdAt")
    val createdAt: String = "",
    
    @SerializedName("updatedAt")
    val updatedAt: String = ""
)