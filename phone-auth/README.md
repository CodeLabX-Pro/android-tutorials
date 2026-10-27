# Phone Number Login with Firebase (Java)

Log in with a phone number and an SMS code using Firebase Authentication.

- Video: https://www.youtube.com/watch?v=BWseVs2MXaI
- Step-by-step guide: https://codelab-x.com/videos/BWseVs2MXaI

## Run it

1. Create a Firebase project and add an Android app with the package `com.example.firebasephoneactivity`.
2. Add your SHA-1 and SHA-256 fingerprints in the app settings (run `./gradlew signingReport`).
3. Turn on **Authentication → Sign-in method → Phone**.
4. Download `google-services.json` into the `app` folder.
5. Run on a real phone with a SIM, or add a test number under Phone sign-in settings.

Built with Android Gradle Plugin 9.4.1, Firebase BoM 35.1.0, compileSdk 37, minSdk 24, Java 11.
