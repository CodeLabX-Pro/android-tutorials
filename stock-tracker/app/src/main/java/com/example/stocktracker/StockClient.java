package com.example.stocktracker;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.StringRes;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Talks to Finnhub with Volley. Volley sends requests in the background and calls back on the main thread,
 * so the results can go straight onto the screen.
 */
final class StockClient {
    interface Listener<T> {
        void onResult(T result);

        void onError(@StringRes int message);
    }

    private static final Object TAG = new Object();
    private final RequestQueue queue;
    private final String apiKey;

    StockClient(Context context, String apiKey) {
        this.queue = Volley.newRequestQueue(context.getApplicationContext());
        this.apiKey = apiKey;
    }

    /** The company behind a symbol: its name, exchange and currency. Finnhub sends {} for an unknown symbol. */
    void profile(String symbol, Listener<JSONObject> listener) {
        get("stock/profile2", symbol, listener);
    }

    void quote(String symbol, Listener<Quote> listener) {
        get("quote", symbol, new Listener<JSONObject>() {
            @Override
            public void onResult(JSONObject json) {
                Quote quote = Quote.fromJson(json);
                if (quote == null) listener.onError(R.string.error_symbol);
                else listener.onResult(quote);
            }

            @Override
            public void onError(int message) {
                listener.onError(message);
            }
        });
    }

    /** Stops every request still waiting, for example when the screen is hidden. */
    void cancelAll() {
        queue.cancelAll(TAG);
    }

    private void get(String path, String symbol, Listener<JSONObject> listener) {
        // Uri.Builder encodes the symbol safely, for symbols like "BRK.B".
        String url = Uri.parse("https://finnhub.io/api/v1/").buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter("symbol", symbol)
                .appendQueryParameter("token", apiKey)
                .build().toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                listener::onResult,
                error -> listener.onError(messageFor(error)));
        request.setTag(TAG);
        queue.add(request);
    }

    @StringRes
    private static int messageFor(VolleyError error) {
        int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
        if (status == 401 || status == 403) return R.string.error_key;
        if (status == 429) return R.string.error_limit;
        return status == 0 ? R.string.error_network : R.string.error_server;
    }
}
