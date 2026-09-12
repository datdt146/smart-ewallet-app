package com.smartewallet.app.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String = "",
    
    @SerializedName("email")
    val email: String = "",
    
    @SerializedName("phone")
    val phone: String = "",
    
    @SerializedName("firstName")
    val firstName: String = "",
    
    @SerializedName("lastName")
    val lastName: String = "",
    
    @SerializedName("avatar")
    val avatar: String? = null,
    
    @SerializedName("idCard")
    val idCard: String = "",
    
    @SerializedName("address")
    val address: String = "",
    
    @SerializedName("dateOfBirth")
    val dateOfBirth: String = "",
    
    @SerializedName("isOtpEnabled")
    val isOtpEnabled: Boolean = false,
    
    @SerializedName("isPinSet")
    val isPinSet: Boolean = false,
    
    @SerializedName("createdAt")
    val createdAt: String = "",
    
    @SerializedName("updatedAt")
    val updatedAt: String = ""
)