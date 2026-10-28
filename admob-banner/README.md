# AdMob Banner Ads (Java)

An adaptive AdMob banner pinned to the bottom of the screen, sized for every phone.

- Video: https://www.youtube.com/watch?v=nHnrCW36jYE
- Step-by-step guide: https://codelab-x.com/videos/nHnrCW36jYE

## Run it

Open this folder in Android Studio and press **Run**. Debug builds always show Google's test ads.

## Publish with your own ads

Add your IDs from the [AdMob console](https://admob.google.com/) to `local.properties` (never committed):

```
ADMOB_APP_ID=ca-app-pub-xxxxxxxxxxxxxxxx~xxxxxxxxxx
ADMOB_BANNER_ID=ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx
```

Release builds use them; debug builds keep using test ads, so you never click your own real ads.

Built with Android Gradle Plugin 9.4.1, Google Mobile Ads 25.5.0, compileSdk 37, minSdk 24, Java 11.
