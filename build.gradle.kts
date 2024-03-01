// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.android.room) apply false
}

val buildToolsVersion by extra("34.0.0")
val compileSdkVersion by extra("android-34")
val targetSdkVersion by extra(34)
val minSdkVersion by extra(24)
val javaVersion by extra(JavaVersion.VERSION_17)
val kotlinJvmTarget by extra("17")
val composeCompiler by extra("1.5.5")