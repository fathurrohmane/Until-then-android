@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.android.room)
}

val buildToolsVersion: String by rootProject.extra
val compileSdkVersion: String by rootProject.extra
val targetSdkVersion: Int by rootProject.extra
val minSdkVersion: Int by rootProject.extra
val javaVersion: JavaVersion by rootProject.extra
val kotlinJvmTarget: String by rootProject.extra

android {
    namespace = "com.elkusnandi.data.countdown"
    compileSdk = targetSdkVersion

    defaultConfig {
        minSdk = minSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion

        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = kotlinJvmTarget
    }

    buildFeatures {
        buildConfig = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    implementation(project(":core:common"))

    coreLibraryDesugaring(libs.android.tools.desugarlibs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // Hilt dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Room database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    // Paging
    implementation(libs.androidx.paging)
    androidTestImplementation(libs.androidx.paging.testing)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}