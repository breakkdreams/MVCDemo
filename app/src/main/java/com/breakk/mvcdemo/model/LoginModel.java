package com.breakk.mvcdemo.model;

import android.util.Log;

import com.breakk.mvcdemo.constants.UrlPath;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class LoginModel {

    private static final String TAG = "LoginModel";

    public void gotoLogin(String phoneNumber, String password, final OnNetResponseListener listener) {
        OkHttpUtils
                .post()
                .url(UrlPath.URL_LOGIN)
                .addParams("account", phoneNumber)
                .addParams("password", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Log.e(TAG, "onError: ---网络访问出现异常---" + e.getMessage());
                        e.printStackTrace();
                        listener.onNetResponseError("网络访问出现异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: 登录成功 response = " + response + " ---");
                        Gson gson = new Gson();
                        BaseModel baseData = gson.fromJson(response, BaseModel.class);
                        listener.onNetResponseSuccess(baseData);
                    }
                });
    }

    public interface OnNetResponseListener {

        void onNetResponseError(String msg);

        void onNetResponseSuccess(BaseModel baseData);
    }
}
