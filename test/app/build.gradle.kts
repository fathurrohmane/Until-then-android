@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
}

val buildToolsVersion: String by rootProject.extra
val compileSdkVersion: String by rootProject.extra
val targetSdkVersion: Int by rootProject.extra
val minSdkVersion: Int by rootProject.extra
val javaVersion: JavaVersion by rootProject.extra
val kotlinJvmTarget: String by rootProject.extra

android {
    targetProjectPath = ":app"
    namespace = "com.elkusnandi.test.app"
    compileSdk = targetSdkVersion

    defaultConfig {
        minSdk = minSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = kotlinJvmTarget
    }
}

dependencies {

    implementation(project(":app"))
    implementation(project(":core:common"))
    coreLibraryDesugaring(libs.android.tools.desugarlibs)
    implementation(libs.junit4)
    implementation(libs.mockito)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.turbine.test)
    implementation(libs.androidx.test.espresso.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.navigation.testing)
    implementation(libs.hilt.android.test)
}