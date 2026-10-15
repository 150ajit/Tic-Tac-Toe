# Proguard / R8 configuration for Tic Tac Toe – XOXO

# Keep DataStore preference models if needed
-keepclassmembers class * {
    @androidx.annotation.Keep <fields>;
    @androidx.annotation.Keep <methods>;
}

# Keep Compose models and StateFlow properties
-keepclassmembers class com.antigravity.tictactoe.domain.model.** { *; }

# General Android optimizations
-dontwarn javax.annotation.**
-dontwarn org.jetbrains.annotations.**
