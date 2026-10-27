import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.codingtutorials.googlemap"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.codingtutorials.googlemap"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        // Your Maps key lives in local.properties (never committed), as MAPS_API_KEY=AIza...
        val localProperties = Properties().apply {
            rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
        }
        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY", "")
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
    implementation(libs.play.services.maps)
}
