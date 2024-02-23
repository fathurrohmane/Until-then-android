@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

val buildToolsVersion: String by rootProject.extra
val compileSdkVersion: String by rootProject.extra
val targetSdkVersion: Int by rootProject.extra
val minSdkVersion: Int by rootProject.extra
val javaVersion: JavaVersion by rootProject.extra
val kotlinJvmTarget: String by rootProject.extra
val composeCompiler: String by rootProject.extra

android {
    namespace = "com.elkusnandi.core.design"
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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = kotlinJvmTarget
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeCompiler
    }
}

dependencies {
    implementation(project(":core:common"))

    coreLibraryDesugaring(libs.android.tools.desugarlibs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphic)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
}