package com.smartewallet.app.data.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.smartewallet.app.BuildConfig
import com.smartewallet.app.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient(private val tokenManager: TokenManager) {
    
    companion object {
        // TODO: Change to your backend server URL
        private const val BASE_URL = "http://192.168.1.100:6001/"
        // For emulator use: http://10.0.2.2:6001/
        // For actual device: http://YOUR_SERVER_IP:6001/
    }
    
    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        
        // Add logging interceptor
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }
        
        // Add authentication interceptor
        builder.addInterceptor(AuthInterceptor(tokenManager))
        
        // Add error interceptor
        builder.addInterceptor(ErrorInterceptor())
        
        // Configure timeouts
        builder.apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }
        
        builder.build()
    }
    
    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

private class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Get token from TokenManager
        val token = tokenManager.getToken()
        
        val requestBuilder = originalRequest.newBuilder()
        
        // Add Authorization header if token exists
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        
        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Content-Type", "application/json")
        
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

private class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        
        when (response.code) {
            401 -> {
                // Unauthorized - Token expired
                // Handle token refresh or redirect to login
            }
            403 -> {
                // Forbidden - Access denied
            }
            404 -> {
                // Not found
            }
            500 -> {
                // Server error
            }
        }
        
        return response
    }
}