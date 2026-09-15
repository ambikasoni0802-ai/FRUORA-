plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.fruitapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fruitapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    // Razorpay checkout SDK (for Online Payment / UPI)
    implementation("com.razorpay:checkout:1.6.33")

    // Google Play Services Location (for "Use My Current Location" feature)
    implementation("com.google.android.gms:play-services-location:21.2.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")
}
