package com.breakk.mvcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.breakk.mvcdemo.constants.UrlPath;
import com.breakk.mvcdemo.model.BaseModel;
import com.breakk.mvcdemo.model.LoginModel;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private EditText etPhoneNumber,etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhoneNumber = findViewById(R.id.et_phone_number);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                gotoLogin(phoneNumber,password);
            default:
                break;
        }
    }

    private void gotoLogin(String phoneNumber, String password) {
        //本地对输入情况做校验
        boolean validateOk = validateInput(phoneNumber, password);
        if (validateOk) {
            progressBar.setVisibility(View.VISIBLE);
            LoginModel loginModel = new LoginModel();
            loginModel.gotoLogin(phoneNumber, password, new LoginModel.OnNetResponseListener() {
                @Override
                public void onNetResponseError(String msg) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNetResponseSuccess(BaseModel baseData) {
                    progressBar.setVisibility(View.GONE);
                    switch (baseData.getCode()) {
                        case 200:   //登录成功
                            Toast.makeText(LoginActivity.this, baseData.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onResponse: = " + baseData.getMessage());

                            //本地保存必要的用户信息
                            //......

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            //登录页面直接消失
                            finish();
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, baseData.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onResponse: = " + baseData.getMessage());
                            break;
                    }
                }
            });
        }
    }

    private boolean validateInput(String phoneNumber, String password) {
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (!phoneNumber.matches(Constants.STR_PHONE_REGEX2)) {  //匹配正则表达式
//            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }
}
