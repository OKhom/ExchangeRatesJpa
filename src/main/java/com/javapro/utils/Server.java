package com.javapro.utils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Server {
    private static final String URL = "https://api.privatbank.ua/p24api/";
    private static final String endpoint = "exchange_rates?json&date=";

    public static String getUrl = URL + endpoint;

    public static String request (String endpoint) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getUrl + endpoint)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }
}
