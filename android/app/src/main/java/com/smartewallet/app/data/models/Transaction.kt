package com.smartewallet.app.data.models

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("id")
    val id: String = "",
    
    @SerializedName("fromUserId")
    val fromUserId: String = "",
    
    @SerializedName("toUserId")
    val toUserId: String? = null,
    
    @SerializedName("amount")
    val amount: Double = 0.0,
    
    @SerializedName("type")
    val type: String = "", // "transfer", "topup", "payment"
    
    @SerializedName("status")
    val status: String = "", // "pending", "completed", "failed"
    
    @SerializedName("description")
    val description: String = "",
    
    @SerializedName("referenceNo")
    val referenceNo: String = "",
    
    @SerializedName("createdAt")
    val createdAt: String = "",
    
    @SerializedName("updatedAt")
    val updatedAt: String = ""
)