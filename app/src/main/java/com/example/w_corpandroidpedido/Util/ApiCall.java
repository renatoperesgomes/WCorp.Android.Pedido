package com.example.w_corpandroidpedido.Util;

import android.net.Uri;
import android.net.http.UrlRequest;
import android.util.Pair;
import android.webkit.URLUtil;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Http2Connection;


public class ApiCall<ApiModel> {
    Preferences.Key<String> BEARER = PreferencesKeys.stringKey("authentication");
    static String baseUrl = "http://192.168.2.189:45455/";
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
                    HttpUrl.Builder httpUrl = HttpUrl.parse(URLchamada).newBuilder();

                    if(listParameters != null){
                        for (int i = 0; i < listParameters.size(); i++){
                            httpUrl.addQueryParameter(listParameters.get(i).first, listParameters.get(i).second);
                        }
                    }

                    HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.build().url().openConnection();

                    if (bearer != null && !bearer.isEmpty())
                        httpURLConnection.setRequestProperty("Authorization", "Bearer " + bearer);

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
