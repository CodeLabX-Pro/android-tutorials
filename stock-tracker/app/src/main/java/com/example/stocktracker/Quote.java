package com.example.stocktracker;

import org.json.JSONObject;

/**
 * The latest price for one stock, read from Finnhub's quote reply.
 * Example: {"c":227.5,"d":1.9,"dp":0.84,"h":228.1,"l":225.3,"o":225.9,"pc":225.6,"t":1761840000}
 */
final class Quote {
    final double price;
    final double change;
    final double percent;
    final double high;
    final double low;
    final double open;
    final double previousClose;

    private Quote(double price, double change, double percent, double high, double low, double open, double previousClose) {
        this.price = price;
        this.change = change;
        this.percent = percent;
        this.high = high;
        this.low = low;
        this.open = open;
        this.previousClose = previousClose;
    }

    /** Returns null for an unknown symbol: Finnhub answers with zeros and no time instead of an error. */
    static Quote fromJson(JSONObject json) {
        if (json.optLong("t") == 0) return null;
        return new Quote(json.optDouble("c"), json.optDouble("d"), json.optDouble("dp"),
                json.optDouble("h"), json.optDouble("l"), json.optDouble("o"), json.optDouble("pc"));
    }
}
