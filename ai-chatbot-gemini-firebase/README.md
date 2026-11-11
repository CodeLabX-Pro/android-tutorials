# AI Chatbot with Gemini and Firebase (Jetpack Compose)

A chat app where you ask a question and Google Gemini answers word by word. It remembers the conversation, has a New chat button, and keeps no API key in the app: Firebase AI Logic holds the key, and App Check makes sure only your app can use it.

- Video: coming soon
- Step-by-step guide: https://codelab-x.com/upcoming/ai-chatbot-gemini-firebase

## Run it

1. Create a Firebase project and add an Android app with the package `com.example.geminichatbot`.
2. Download `google-services.json` into the `app` folder.
3. In the Firebase console, open **AI Services → AI Logic**, click **Get started**, and pick the **Gemini Developer API** (the free one).
4. Open this folder in Android Studio and press **Run**. Type a message and tap **Send**. The first reply says the App Check token is invalid; that's expected.
5. In Logcat, search for `debug secret` and copy the code at the end of that line.
6. In the Firebase console, open **Security → App Check → Apps**, click the three dots next to your app, choose **Manage debug tokens**, and add the code.
7. Close the app and run it again. Gemini now answers.

If you uninstall the app or clear its data, it gets a new debug secret. Repeat steps 5 and 6.

Before publishing an app like this, replace the App Check debug provider with Play Integrity.

Built with Android Gradle Plugin 9.4.0, Kotlin 2.4.20, Compose BOM 2026.09.00, Firebase BoM 35.1.0 (Firebase AI Logic, model `gemini-3.5-flash-lite`), compileSdk 37, minSdk 24, Java 17.
