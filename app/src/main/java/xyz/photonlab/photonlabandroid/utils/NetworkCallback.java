package xyz.photonlab.photonlabandroid.utils;

import okhttp3.Response;

public interface NetworkCallback {
    void onSuccess(Response response);

    void onFailed(String msg);
}
