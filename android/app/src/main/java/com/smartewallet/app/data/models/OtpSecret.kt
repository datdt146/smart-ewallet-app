package com.smartewallet.app.data.models

import com.google.gson.annotations.SerializedName

data class OtpSecret(
    @SerializedName("secret")
    val secret: String = "",
    
    @SerializedName("qrCode")
    val qrCode: String? = null,
    
    @SerializedName("backupCodes")
    val backupCodes: List<String> = listOf()
)