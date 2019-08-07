package xyz.photonlab.photonlabandroid.utils;

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

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                if (callback != null)
                    callback.onFailed(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull final Call call, @NotNull final Response response) {
                if (callback != null)
                    callback.onSuccess(response);
            }
        });
    }

    public void setCallback(NetworkCallback callback) {
        this.callback = callback;
    }
}
