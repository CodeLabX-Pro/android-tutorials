import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.stocktracker"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.stocktracker"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        // Your Finnhub key lives in local.properties (never committed), as FINNHUB_API_KEY=abc123...
        val localProperties = Properties().apply {
            rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
        }
        buildConfigField("String", "FINNHUB_API_KEY", "\"${localProperties.getProperty("FINNHUB_API_KEY", "")}\"")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.volley)
}
