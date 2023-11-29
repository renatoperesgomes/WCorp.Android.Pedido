package com.example.w_corpandroidpedido.Util;

import android.util.Pair;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Flowable;

public class ApiCall<ApiModel> {
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    static String baseUrl = "http://dashboard.wcorp.com.br:5000/";
    private Type modelType;
    private Executor executor = Executors.newSingleThreadExecutor();
    public ApiCall(Type modelType) {
        this.modelType = modelType;
    }
    public ListenableFuture<ApiModel> CallApi(String function, String bearer, List<Pair<String, String>> listParameters)  {
        return CallbackToFutureAdapter.getFuture(completer -> {
            executor.execute(() -> {
                try {
                    String URLchamada = baseUrl + function;
                    URL url = new URL(URLchamada);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    if (bearer != null && !bearer.isEmpty())
                        httpURLConnection.setRequestProperty("Authentication", "Bearer " + bearer);

                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                        throw new RuntimeException("HTTP error code: " + httpURLConnection.getResponseCode());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    String jsonEmString = Util.ConverteJsonEmString(bufferedReader);

                    Gson gson = new Gson();
                    ApiModel model = gson.fromJson(jsonEmString, modelType);
                    completer.set(model);
                } catch (Exception e) {
                    completer.setException(e);
                }
            });
            return null;
        });
    }
}
