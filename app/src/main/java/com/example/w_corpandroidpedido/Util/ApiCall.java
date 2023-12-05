package com.example.w_corpandroidpedido.Util;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlin.Triple;
import okhttp3.HttpUrl;


public class ApiCall<ApiModel> {
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    static String baseUrl = "http://dashboard.wcorp.com.br:5000/";
    private Type modelType;
    private Executor executor = Executors.newSingleThreadExecutor();
    public ApiCall(Type modelType) {
        this.modelType = modelType;
    }
    public ListenableFuture<ApiModel> CallApi(String function, String bearer, List<Triple<String, String, Boolean>> listParameters)  {
        return CallbackToFutureAdapter.getFuture(completer -> {
            executor.execute(() -> {
                try {
                    String URLchamada = baseUrl + function;
                    HttpUrl.Builder httpUrl = HttpUrl.parse(URLchamada).newBuilder();

                    if(listParameters != null) {
                        for (int i = 0; i < listParameters.size(); i++) {
                            if (!listParameters.get(i).getThird()) {
                                httpUrl.addQueryParameter(listParameters.get(i).getFirst(), listParameters.get(i).getSecond().toString());
                            }
                        }
                    }

                    HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.build().url().openConnection();

                    if (bearer != null && !bearer.isEmpty())
                        httpURLConnection.setRequestProperty("Authorization", "Bearer " + bearer);

                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "*/*");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    if(listParameters != null) {
                        for (int i = 0; i < listParameters.size(); i++) {
                            if (listParameters.get(i).getThird()) {
                                OutputStream outputStream = httpURLConnection.getOutputStream();
                                outputStream.write(listParameters.get(i).getSecond().getBytes());
                                outputStream.flush();
                                outputStream.close();
                            }
                        }
                    }

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
