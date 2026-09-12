# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-verbose

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonDeserializer
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonSerializationContext
-keep class * implements com.google.gson.JsonDeserializationContext

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Keep Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Keep TOTP
-keep class dev.turingcomplete.kotlinotp.** { *; }

-keep class com.smartewallet.app.models.** { *; }
-keep class com.smartewallet.app.api.** { *; }
