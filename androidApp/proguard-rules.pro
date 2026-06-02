-keep class androidx.work.InputMerger { *; }
-keep class * extends androidx.work.InputMerger { *; }
-keep class androidx.work.impl.** { *; }
-keepattributes *Annotation*
-keepattributes Signature
-keep class * {
    @androidx.room3.TypeConverter <methods>;
}
-keep class com.shub39.grit.core.data.Converters {
    *;
}
-keepclassmembers enum kotlinx.datetime.DayOfWeek {
    *;
}
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-keep class com.shub39.grit.core.data.Converters { *; }
-keep class com.shub39.grit.core.data.Converters$* { *; }
-keepclassmembers class com.shub39.grit.core.data.Converters {
    *;
}

-keepclasseswithmembernames class com.shub39.grit.core.data.Converters {
    *;
}

-keep class kotlinx.datetime.** { *; }
-keepclassmembers class kotlinx.datetime.** { *; }
-keepclassmembers enum kotlinx.datetime.** { *; }

-keep class * extends androidx.room3.RoomDatabase { *; }
-keep @androidx.room3.Database class * { *; }
-keep @androidx.room3.Dao class * { *; }
-keep @androidx.room3.Entity class * { *; }

-keep class com.shub39.grit.core.data.Converters { *; }
-keepclassmembers class com.shub39.grit.core.data.Converters { *; }

-keepnames class com.shub39.grit.core.data.Converters