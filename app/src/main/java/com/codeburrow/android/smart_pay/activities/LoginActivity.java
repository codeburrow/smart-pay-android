package com.codeburrow.android.smart_pay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.async_tasks.AttemptToFindAccountTask;
import com.codeburrow.android.smart_pay.async_tasks.AttemptToFindAccountTask.AsyncResponse;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {
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

        AttemptToFindAccountTask attemptToFindAccountTask = new AttemptToFindAccountTask(getApplicationContext(), this, iban);
        attemptToFindAccountTask.execute();
    }

    @Override
    public void processFinish() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }

//    public class AttemptToFindAccountTask extends AsyncTask<Void, Void, JSONObject> {
//        private final String TAG = AttemptToFindAccountTask.class.getSimpleName();
//        private final Context mContext;
//        private String iban;
//
//        public AttemptToFindAccountTask(Context context, String iban) {
//            super();
//
//            this.mContext = context;
//            this.iban = iban;
//        }
//
//        @Override
//        protected JSONObject doInBackground(Void... args) {
//            AccountApi accountApi = new AccountApi();
//            return accountApi.findByIban(iban);
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject apiResponse) {
//            if (null == apiResponse) {
//                Toast.makeText(mContext, "No API Response.", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            try {
//                if (!apiResponse.has(Api.ACCOUNTS_KEY)) {
//                    Toast.makeText(mContext, "API Error.", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                JSONArray accounts = apiResponse.getJSONArray(Api.ACCOUNTS_KEY);
//
//                if (accounts.length() == 0) {
//                    Toast.makeText(mContext, "IBAN Validation Failed.", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                JSONObject account = (JSONObject) accounts.get(0);
//
//                Toast.makeText(mContext, "IBAN validation successful.", Toast.LENGTH_SHORT).show();
//                Toast.makeText(mContext, account.toString(), Toast.LENGTH_LONG).show();
//
//                startActivity(new Intent(mContext, ScanQrCodeActivity.class));
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.e(TAG, e.getMessage());
//            }
//        }
//    }
}
