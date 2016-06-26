package com.codeburrow.android.smart_pay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.tasks.GetAccessTokenTask;
import com.codeburrow.android.smart_pay.tasks.GetAccessTokenTask.AccessTokenAsyncResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/23-24/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class LoginActivity extends AppCompatActivity implements AccessTokenAsyncResponse {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    public static final String PREFERENCES = "Token Credentials";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String REFRESH_TOKEN_KEY = "refresh_token";

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    private String username;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameEditText = (EditText) findViewById(R.id.username_editText);
        mPasswordEditText = (EditText) findViewById(R.id.password_editText);
    }

    public void login(View view) {
        username = mUsernameEditText.getText().toString();
        password = mPasswordEditText.getText().toString();

        GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask(this, username, password, this);
        getAccessTokenTask.execute();
    }

//    @Override
//    public void processFindCustomerAsyncFinish(JSONObject loginCustomer, String errorFound) {
//        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
//        editor.putString(IBAN_PREFS_KEY, username);
//        editor.putString(PASSWORD_PREFS_KEY, password);
//        editor.putString(OWNER_PREFS_KEY, CustomerApi.findLegalNameFromCustomer(loginCustomer));
//        editor.commit();
//
//        startActivity(new Intent(this, ScanQrCodeActivity.class));
//        finish();
//    }

    @Override
    public void processGetAccessTokenAsyncFinish(JSONObject token, String errorFound) {
        try {
            SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
            editor.putString(ACCESS_TOKEN_KEY, token.getString("access_token"));
            editor.putString(REFRESH_TOKEN_KEY, token.getString("refresh_token"));
            editor.commit();
            Log.e(LOG_TAG, token.getString("access_token") + " " + token.getString("refresh_token"));

            startActivity(new Intent(LoginActivity.this, ScanQrCodeActivity.class));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}
