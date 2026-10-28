# AdMob Interstitial Ads (Java)

A tiny level game that loads a full-screen AdMob interstitial early and shows it between levels.

- Video: https://www.youtube.com/watch?v=zlnRM3oKYas
- Step-by-step guide: https://codelab-x.com/videos/zlnRM3oKYas

## Run it

Open this folder in Android Studio and press **Run**. Debug builds always show Google's test ads.

## Publish with your own ads

Add your IDs from the [AdMob console](https://admob.google.com/) to `local.properties` (never committed):

```
ADMOB_APP_ID=ca-app-pub-xxxxxxxxxxxxxxxx~xxxxxxxxxx
ADMOB_INTERSTITIAL_ID=ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx
```

Release builds use them; debug builds keep using test ads, so you never click your own real ads.

Built with Android Gradle Plugin 9.4.1, Google Mobile Ads 25.5.0, compileSdk 37, minSdk 24, Java 11.
