package com.codeburrow.android.smart_pay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.api.AccountApi;
import com.codeburrow.android.smart_pay.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFERENCES = "Credentials";
    public static final String IBAN_PREFS_KEY = "ibanKey";
    public static final String PASSWORD_PREFS_KEY = "passKey";
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private EditText ibanEditText;
    private EditText passwordEditText;

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

        AttemptToFindAccountTask attemptToFindAccountTask = new AttemptToFindAccountTask(iban);
        attemptToFindAccountTask.execute();
    }

    private class AttemptToFindAccountTask extends AsyncTask<Void, Void, JSONObject> {
        private final String TAG = AttemptToFindAccountTask.class.getSimpleName();
        private String iban;

        public AttemptToFindAccountTask(String iban) {
            super();

            this.iban = iban;
        }

        @Override
        protected JSONObject doInBackground(Void... args) {
            AccountApi accountApi = new AccountApi();

            return accountApi.findByIban(iban);
        }

        @Override
        protected void onPostExecute(JSONObject apiResponse) {
            if (null == apiResponse) {
                Toast.makeText(getApplicationContext(), "No API Response.", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                if (!apiResponse.has(Api.ACCOUNTS_KEY)) {
                    Toast.makeText(getApplicationContext(), "API Unkown Error.", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONArray accounts = apiResponse.getJSONArray(Api.ACCOUNTS_KEY);

                if (accounts.length() == 0) {
                    Toast.makeText(getApplicationContext(), "IBAN Validation Failed.", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject account = (JSONObject) accounts.get(0);

                Toast.makeText(getApplicationContext(), "IBAN validation successful.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), account.toString(), Toast.LENGTH_LONG).show();

                startActivity(new Intent(getApplicationContext(), ScanQrCodeActivity.class));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
