# Stock Tracker (Java)

Type a stock symbol and watch its live price, daily change, open, high, low and previous close. Refreshes every 15 seconds while the app is open, and flashes green or red when the price moves.

The video used IEX Cloud, which has shut down; this version uses the free [Finnhub](https://finnhub.io/) API.

- Video: https://www.youtube.com/watch?v=MzB4Z_Of55k
- Step-by-step guide: https://codelab-x.com/videos/MzB4Z_Of55k

## Run it

1. Sign up at [finnhub.io](https://finnhub.io/register) and copy your free API key.
2. Add this line to `local.properties` in this folder (never committed):

   ```
   FINNHUB_API_KEY=your-key-here
   ```

3. Open the folder in Android Studio and press **Run**.

Built with Android Gradle Plugin 9.4.1, Volley 1.2.1, compileSdk 37, minSdk 24, Java 11.
