package com.smartewallet.app.utils

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import dev.turingcomplete.kotlinotp.TOTP
import dev.turingcomplete.kotlinotp.TOTPGenerator

class TotpManager {
    
    /**
     * Generate OTP secret key
     */
    fun generateSecret(): String {
        return TOTP.generateSecret()
    }
    
    /**
     * Generate OTP code from secret
     */
    fun generateCode(secret: String): String {
        return try {
            val totp = TOTP.fromSecret(secret)
            totp.now()
        } catch (e: Exception) {
            ""
        }
    }
    
    /**
     * Verify OTP code
     */
    fun verifyCode(secret: String, code: String, timeWindow: Int = 1): Boolean {
        return try {
            val totp = TOTP.fromSecret(secret)
            val currentCode = totp.now()
            
            // Allow for time drift (±1 time step by default)
            if (code == currentCode) return true
            
            // Check previous time step
            val previousCode = TOTP.fromSecret(secret).at(System.currentTimeMillis() - 30000)
            if (code == previousCode) return true
            
            // Check next time step
            val nextCode = TOTP.fromSecret(secret).at(System.currentTimeMillis() + 30000)
            if (code == nextCode) return true
            
            false
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Generate QR Code for OTP secret
     */
    fun generateQrCode(
        email: String,
        secret: String,
        appName: String = "SmartEWallet",
        size: Int = 512
    ): Bitmap? {
        return try {
            val otpauthUrl = generateOtpauthUrl(email, secret, appName)
            val bitMatrix = MultiFormatWriter().encode(
                otpauthUrl,
                BarcodeFormat.QR_CODE,
                size,
                size
            )
            bitMatrixToBitmap(bitMatrix)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Generate otpauth URL for manual entry
     */
    fun generateOtpauthUrl(
        email: String,
        secret: String,
        appName: String = "SmartEWallet"
    ): String {
        return "otpauth://totp/$appName:$email?secret=$secret&issuer=$appName"
    }
    
    /**
     * Convert BitMatrix to Bitmap
     */
    private fun bitMatrixToBitmap(bitMatrix: BitMatrix): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        
        return bitmap
    }
    
    /**
     * Generate backup codes
     */
    fun generateBackupCodes(count: Int = 10): List<String> {
        val backupCodes = mutableListOf<String>()
        repeat(count) {
            backupCodes.add(generateRandomCode(8))
        }
        return backupCodes
    }
    
    private fun generateRandomCode(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
}