plugins {
    alias(libs.plugins.android.application)
    // Android Gradle Plugin 9 compiles Kotlin itself; this adds the Compose compiler.
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.composebasics"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.composebasics"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    // The BoM picks matching versions for every Compose library.
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
