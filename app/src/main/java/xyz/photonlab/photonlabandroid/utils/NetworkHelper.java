package xyz.photonlab.photonlabandroid.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkHelper {
    private NetworkCallback callback;
    private static OkHttpClient client = new OkHttpClient();


    public NetworkHelper() {

    }

    public void connect(Request request) {
        this.connect(request, client);
    }

    public void connect(Request request, OkHttpClient client) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                Log.e("request error", e.getMessage());
                if (callback != null)
                    callback.onFailed(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull final Call call, @NotNull final Response response) {
                Log.i("request success", response.toString());
                if (callback != null)
                    callback.onSuccess(response);
            }
        });
    }

    public void setCallback(NetworkCallback callback) {
        this.callback = callback;
    }
}
