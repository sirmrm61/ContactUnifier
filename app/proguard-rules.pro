# ProGuard rules for Contact Unifier

# Keep Hilt generated code
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Compose runtime
-keep class androidx.compose.** { *; }

# Keep model classes
-keep class com.contactunifier.data.model.** { *; }
-keep class com.contactunifier.domain.** { *; }

# Keep Coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep Lottie
-keep class com.airbnb.lottie.** { *; }

# General Android/Kotlin rules
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.app.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
