# Chat App with Firebase (Java)

A real-time chat app: create an account, see everyone else who has one, and chat. Messages appear instantly on both phones.

- Video: https://www.youtube.com/watch?v=V9li7YP1NWg
- Step-by-step guide: https://codelab-x.com/videos/V9li7YP1NWg

## Run it

1. Create a Firebase project and add an Android app with the package `com.example.chatapp`.
2. In **Authentication → Sign-in method**, enable **Email/Password**.
3. Create a **Realtime Database**, then paste `database.rules.json` from this folder into its **Rules** tab and publish.
4. Download `google-services.json` into the `app` folder.
5. Open this folder in Android Studio and press **Run**. Create two accounts (on two emulators or phones) to chat between them.

Built with Android Gradle Plugin 9.4.1, Firebase BoM 35.1.0, compileSdk 37, minSdk 24, Java 11.
