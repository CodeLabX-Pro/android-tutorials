plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.allcodingtutorial.camerafull1"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.allcodingtutorial.camerafull1"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
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
}
