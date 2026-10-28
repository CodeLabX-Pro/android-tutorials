import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// Your real AdMob IDs live in local.properties, which is never committed.
val localProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

android {
    namespace = "com.example.admobinterstitial"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.admobinterstitial"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        // Debug builds always show Google's test ads, so you can never click your own real ads by mistake.
        debug {
            manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-3940256099942544~3347511713"
            buildConfigField("String", "INTERSTITIAL_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
        }
        // Release builds use your own IDs from local.properties.
        release {
            manifestPlaceholders["ADMOB_APP_ID"] = localProperties.getProperty("ADMOB_APP_ID", "")
            buildConfigField("String", "INTERSTITIAL_AD_UNIT_ID", "\"${localProperties.getProperty("ADMOB_INTERSTITIAL_ID", "")}\"")
        }
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
    implementation(libs.play.services.ads)
}
