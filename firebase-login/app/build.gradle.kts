plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.firebaselogin"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.firebaselogin"
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
    // The BoM picks matching versions for every Firebase library.
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}
