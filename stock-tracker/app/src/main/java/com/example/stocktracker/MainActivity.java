package com.example.stocktracker;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Type a stock symbol and watch its price. While the screen is visible, the price refreshes every 15 seconds,
 * and flashes green or red when it moves.
 */
public class MainActivity extends AppCompatActivity {
    private static final long REFRESH_MS = 15_000;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable refresh = new Runnable() {
        @Override
        public void run() {
            loadQuote();
            handler.postDelayed(this, REFRESH_MS);
        }
    };

    private StockClient client;
    private SharedPreferences prefs;
    private String symbol;
    private NumberFormat money = moneyFormat("USD");
    private double lastPrice;
    private int priceColour;

    private EditText symbolInput;
    private View content;
    private TextView error, company, details, price, change, open, high, low, previousClose, updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);
        symbolInput = findViewById(R.id.inputSymbol);
        content = findViewById(R.id.content);
        error = findViewById(R.id.textError);
        company = findViewById(R.id.textCompany);
        details = findViewById(R.id.textDetails);
        price = findViewById(R.id.textPrice);
        change = findViewById(R.id.textChange);
        open = findViewById(R.id.textOpen);
        high = findViewById(R.id.textHigh);
        low = findViewById(R.id.textLow);
        previousClose = findViewById(R.id.textPreviousClose);
        updated = findViewById(R.id.textUpdated);
        priceColour = price.getCurrentTextColor();

        client = new StockClient(this, BuildConfig.FINNHUB_API_KEY);
        prefs = getSharedPreferences("stocks", MODE_PRIVATE);
        symbol = prefs.getString("symbol", "AAPL");
        symbolInput.setText(symbol);

        findViewById(R.id.btnTrack).setOnClickListener(v -> track());
        symbolInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_GO) return false;
            track();
            return true;
        });
    }

    /** Refresh only while the screen is visible, so the app never uses data in the background. */
    @Override
    protected void onStart() {
        super.onStart();
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(refresh);
        client.cancelAll();
    }

    private void track() {
        String typed = symbolInput.getText().toString().trim().toUpperCase(Locale.ROOT);
        if (typed.isEmpty()) {
            symbolInput.setError(getString(R.string.error_empty));
            return;
        }
        WindowCompat.getInsetsController(getWindow(), symbolInput).hide(WindowInsetsCompat.Type.ime());
        symbol = typed;
        lastPrice = 0;
        content.setVisibility(View.INVISIBLE);
        handler.removeCallbacks(refresh);
        start();
    }

    private void start() {
        if (BuildConfig.FINNHUB_API_KEY.isEmpty()) {
            showError(R.string.error_no_key);
            return;
        }
        loadProfile();
        handler.post(refresh);
    }

    private void loadProfile() {
        client.profile(symbol, new StockClient.Listener<JSONObject>() {
            @Override
            public void onResult(JSONObject profile) {
                String name = profile.optString("name");
                company.setText(name.isEmpty() ? symbol : name);
                String exchange = profile.optString("exchange");
                details.setText(exchange.isEmpty() ? symbol : getString(R.string.details, symbol, exchange));
                money = moneyFormat(profile.optString("currency", "USD"));
            }

            @Override
            public void onError(int message) {
                // The quote request reports problems; the name is a nice extra.
                company.setText(symbol);
            }
        });
    }

    private void loadQuote() {
        client.quote(symbol, new StockClient.Listener<Quote>() {
            @Override
            public void onResult(Quote quote) {
                show(quote);
                prefs.edit().putString("symbol", symbol).apply();
            }

            @Override
            public void onError(int message) {
                showError(message);
            }
        });
    }

    private void show(Quote quote) {
        error.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        price.setText(money.format(quote.price));
        boolean up = quote.change >= 0;
        int colour = ContextCompat.getColor(this, up ? R.color.up : R.color.down);
        change.setText(getString(up ? R.string.change_up : R.string.change_down,
                money.format(Math.abs(quote.change)), Math.abs(quote.percent)));
        change.setTextColor(colour);
        open.setText(money.format(quote.open));
        high.setText(money.format(quote.high));
        low.setText(money.format(quote.low));
        previousClose.setText(money.format(quote.previousClose));
        updated.setText(getString(R.string.updated, DateFormat.getTimeFormat(this).format(new Date())));

        // Flash the price when it moves since the last refresh, then fade back.
        if (lastPrice != 0 && quote.price != lastPrice) {
            int flash = ContextCompat.getColor(this, quote.price > lastPrice ? R.color.up : R.color.down);
            ObjectAnimator.ofArgb(price, "textColor", flash, priceColour).setDuration(1200).start();
        }
        lastPrice = quote.price;
    }

    private void showError(int message) {
        error.setText(message);
        error.setVisibility(View.VISIBLE);
    }

    private static NumberFormat moneyFormat(String currencyCode) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        try {
            format.setCurrency(Currency.getInstance(currencyCode));
        } catch (IllegalArgumentException unknownCurrency) {
            format.setCurrency(Currency.getInstance("USD"));
        }
        return format;
    }
}
