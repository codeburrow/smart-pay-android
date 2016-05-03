package com.codeburrow.android.smart_pay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.apis.AccountApi;
import com.codeburrow.android.smart_pay.apis.CustomerApi;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindAccountTask;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindAccountTask.AccountAsyncResponse;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindCustomerTask;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindCustomerTask.CustomerAsyncResponse;

import org.json.JSONObject;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/23-24/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class LoginActivity extends AppCompatActivity implements AccountAsyncResponse, CustomerAsyncResponse {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    public static final String PREFERENCES = "Credentials";
    public static final String IBAN_PREFS_KEY = "ibanKey";
    public static final String OWNER_PREFS_KEY = "ownerKey";
    public static final String PASSWORD_PREFS_KEY = "passKey";

    private EditText mIbanEditText;
    private EditText mPasswordEditText;

    private String iban;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIbanEditText = (EditText) findViewById(R.id.iban_editText);
        mPasswordEditText = (EditText) findViewById(R.id.password_editText);
    }

    public void login(View view) {
        iban = mIbanEditText.getText().toString();
        password = mPasswordEditText.getText().toString();

        AttemptToFindAccountTask attemptToFindAccountTask = new AttemptToFindAccountTask(getApplicationContext(), this, iban);
        attemptToFindAccountTask.execute();
    }

    @Override
    public void processFindAccountAsyncFinish(JSONObject loginAccount, String errorFound) {
            AttemptToFindCustomerTask attemptToFindCustomerTask = new AttemptToFindCustomerTask(getApplicationContext(), this, AccountApi.findFirstCustomerNumberFromAccount(loginAccount));
            attemptToFindCustomerTask.execute();
    }

    @Override
    public void processFindCustomerAsyncFinish(JSONObject loginCustomer, String errorFound) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(IBAN_PREFS_KEY, iban);
        editor.putString(PASSWORD_PREFS_KEY, password);
        editor.putString(OWNER_PREFS_KEY, CustomerApi.findLegalNameFromCustomer(loginCustomer));
        editor.commit();

        startActivity(new Intent(this, ScanQrCodeActivity.class));
        finish();
    }

}
