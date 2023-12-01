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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    public ListenableFuture<ApiModel> CallApi(String function, String bearer, List<Pair<String, String>> listParameters, boolean isRequestBody)  {
        return CallbackToFutureAdapter.getFuture(completer -> {
            executor.execute(() -> {
                try {
                    String URLchamada = baseUrl + function;
                    HttpUrl.Builder httpUrl = HttpUrl.parse(URLchamada).newBuilder();

                    if(listParameters != null){
                        if(isRequestBody){
                            httpUrl.addQueryParameter(listParameters.get(0).first, listParameters.get(0).second);
                        }else{
                            for (int i = 0; i < listParameters.size(); i++){
                                httpUrl.addQueryParameter(listParameters.get(i).first, listParameters.get(i).second);
                            }
                        }
                    }

                    HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.build().url().openConnection();

                    if (bearer != null && !bearer.isEmpty())
                        httpURLConnection.setRequestProperty("Authorization", "Bearer " + bearer);

                    httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    if(isRequestBody){
                        JSONObject pedidoMaterialItem = new JSONObject();
                        if(listParameters != null){
                            for (int i = 1; i < listParameters.size(); i++){
                                pedidoMaterialItem.put(listParameters.get(i).first, listParameters.get(i).second);
                            }
                        }
                        httpURLConnection.setRequestProperty("Body", pedidoMaterialItem.toString());
                        OutputStream wr = httpURLConnection.getOutputStream();
                        wr.write(pedidoMaterialItem.toString().getBytes());
                        wr.flush();
                        wr.close();
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
