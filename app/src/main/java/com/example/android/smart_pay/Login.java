package com.example.android.smart_pay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.ScanQrCodeActivity;

public class Login extends AppCompatActivity{
    private static final String LOG_TAG = Login.class.getSimpleName();
    private EditText ibanEditText;
    private EditText passwordEditText;

    // Shared Preferrences
    public static final String PREFERENCES = "Credentials" ;
    public static final String IBAN_PREFS_KEY = "ibanKey";
    public static final String PASSWORD_PREFS_KEY = "passKey";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ibanEditText = (EditText) findViewById(R.id.iban_editText);
        passwordEditText = (EditText) findViewById(R.id.password_editText);
    }

    public void login(View view) {
        String iban = ibanEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(IBAN_PREFS_KEY, iban);
        editor.putString(PASSWORD_PREFS_KEY, password);
        editor.commit();

        startActivity(new Intent(this, ScanQrCodeActivity.class));

        Log.e(LOG_TAG, "\n\nIBAN: " + getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(IBAN_PREFS_KEY,"0") + "\nPASSWORD: " + getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(PASSWORD_PREFS_KEY, "0") + "\n\n");
    }
}
