# Google Sign-In with Credential Manager (Java)

Sign in with a Google account using Credential Manager, Google's current sign-in API, and show the name, email and photo.

- Video: https://www.youtube.com/watch?v=XLbwd_zPbpw
- Step-by-step guide: https://codelab-x.com/videos/XLbwd_zPbpw

## Run it

1. In the Google Cloud console, create an **Android** OAuth client (package `com.allcodingtutorials.googlesignin` + your SHA-1) and a **Web application** OAuth client.
2. Put the **Web** client ID in `app/src/main/res/values/strings.xml` as `web_client_id`.
3. Open this folder in Android Studio and press **Run** on a device or emulator with Google Play.

Built with Android Gradle Plugin 9.4.1, Credentials 1.6.0, Google ID 1.2.1, Glide 5.0.9, compileSdk 37, minSdk 24, Java 11.
