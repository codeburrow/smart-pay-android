package com.example.android.fintech_hackathon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public static final String API_KEY_TOKEN = "d8dd6ba4f73444f6b2ef5a92d1a98b6f";
    private static final String TAG = LoginActivity.class.getSimpleName();
    // EditText
    EditText loginEditText;
    // Button
    Button loginButton;
    // the IBAN_API_KEY we are looking for
    String mIbanLookingFor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = (EditText) findViewById(R.id.edittext);

        loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIbanLookingFor = loginEditText.getText().toString();
                CheckIbanAccountTask checkIBANAccountTask = new CheckIbanAccountTask();
                checkIBANAccountTask.execute(API_KEY_TOKEN, mIbanLookingFor);
            }
        });
    }

    private class CheckIbanAccountTask extends AsyncTask<String, Void, JSONObject> {
        private static final String ACCOUNTS_API_KEY = "accounts";
        private static final String IBAN_API_KEY = "IBAN";
        private final String LOG_TAG = CheckIbanAccountTask.class.getSimpleName();

        @Override
        protected JSONObject doInBackground(String... args) {
            JsonParser jsonParser = new JsonParser();
            final String GET_ACCOUNTS_LIST_URL =
                    "https://nbgdemo.azure-api.net/testnodeapi/api/accounts/list";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("key", args[0]));

            JSONObject jsonResponse = jsonParser.makeHttpRequest(GET_ACCOUNTS_LIST_URL, "GET", params);

            try {
                JSONArray accounts = jsonResponse.getJSONArray(ACCOUNTS_API_KEY);

                return findAccountByIban(accounts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject account) {
            if (null == account) {
                toast("Account not found.");
            } else {
                toast("Account found.");
            }
        }

        private void toast(String message) {
            Context context = getApplicationContext();
            CharSequence text = message;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        private JSONObject findAccountByIban(JSONArray accounts) throws JSONException {
            String iban;

            for (int index = 0; index <= accounts.length(); index++) {
                JSONObject account = accounts.getJSONObject(index);

                iban = account.getString(IBAN_API_KEY);

                if (iban.equalsIgnoreCase(mIbanLookingFor)) {
                    return account;
                }
            }
            return null;
        }
    }
}
