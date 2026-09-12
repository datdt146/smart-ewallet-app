package com.smartewallet.app.utils

import android.util.Patterns

object ValidationUtils {
    
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun isValidPassword(password: String): Boolean {
        // Password must be at least 8 characters
        // Must contain at least one uppercase letter, one lowercase letter, and one digit
        if (password.length < 8) return false
        
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        
        return hasUpperCase && hasLowerCase && hasDigit
    }
    
    fun isValidPin(pin: String): Boolean {
        // PIN must be 4-6 digits
        return pin.length in 4..6 && pin.all { it.isDigit() }
    }
    
    fun isValidAmount(amount: String): Boolean {
        return try {
            val doubleAmount = amount.toDouble()
            doubleAmount > 0
        } catch (e: Exception) {
            false
        }
    }
    
    fun isValidPhone(phone: String): Boolean {
        // Vietnamese phone number format
        return phone.matches("^0[0-9]{9}$".toRegex())
    }
    
    fun getPasswordStrength(password: String): PasswordStrength {
        var strength = 0
        
        if (password.length >= 8) strength++
        if (password.length >= 12) strength++
        if (password.any { it.isUpperCase() }) strength++
        if (password.any { it.isLowerCase() }) strength++
        if (password.any { it.isDigit() }) strength++
        if (password.any { !it.isLetterOrDigit() }) strength++
        
        return when {
            strength <= 2 -> PasswordStrength.WEAK
            strength <= 4 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }
    }
    
    enum class PasswordStrength {
        WEAK, MEDIUM, STRONG
    }
}