package com.codeburrow.android.smart_pay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindAccountTask;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindAccountTask.AsyncResponse;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {
    public static final String PREFERENCES = "Credentials";
    public static final String IBAN_PREFS_KEY = "ibanKey";
    public static final String PASSWORD_PREFS_KEY = "passKey";
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private EditText mIbanEditText;
    private EditText mPasswordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIbanEditText = (EditText) findViewById(R.id.iban_editText);
        mPasswordEditText = (EditText) findViewById(R.id.password_editText);
    }

    public void login(View view) {
        String iban = mIbanEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(IBAN_PREFS_KEY, iban);
        editor.putString(PASSWORD_PREFS_KEY, password);
        editor.commit();

        AttemptToFindAccountTask attemptToFindAccountTask = new AttemptToFindAccountTask(getApplicationContext(), this, iban);
        attemptToFindAccountTask.execute();
    }

    @Override
    public void processFinish() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }
}
