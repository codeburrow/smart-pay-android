package com.example.android.fintech_hackathon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RefreshAccountActivity extends AppCompatActivity {
    // the IBAN_API_KEY we are looking for
    String mIbanLookingFor = "IBAN1124837027";

    Button refreshAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_account);

        refreshAccountButton = (Button) findViewById(R.id.refresh_account_button);
        refreshAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshAccountTask refreshAccountTask = new RefreshAccountTask();
                refreshAccountTask.execute(LoginActivity.API_KEY, mIbanLookingFor);
            }
        });

        RefreshAccountTask refreshAccountTask = new RefreshAccountTask();
        refreshAccountTask.execute(LoginActivity.API_KEY, mIbanLookingFor);
    }

    /**
     * @param message
     */
    private void toast(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void updateTextView(String text){

    }

    private class RefreshAccountTask extends AsyncTask<String, Void, JSONObject> {
        private static final String ACCOUNTS_API_KEY = "accounts";
        private static final String IBAN_API_KEY = "IBAN";
        private final String LOG_TAG = RefreshAccountTask.class.getSimpleName();

        @Override
        protected JSONObject doInBackground(String... args) {
            // JSON Parser
            JSONParser jsonParser = new JSONParser();
            // GET url
            final String GET_ACCOUNTS_LIST_URL =
                    "https://nbgdemo.azure-api.net/testnodeapi/api/accounts/list";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("key", args[0]));

            // Make Http GET Request
            JSONObject jsonResponse = jsonParser.makeHttpRequest(
                    GET_ACCOUNTS_LIST_URL, "GET", params);

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
                try {
                    toast("Account found. id: " + account.getString("id"));
                } catch (JSONException e) {
                    toast("Unable to get account id.");
                }

            }
        }

        private JSONObject findAccountByIban(JSONArray accounts) throws JSONException {
            String iban;

            for (int index = 0; index <= accounts.length(); index++) {
                JSONObject account = accounts.getJSONObject(index);

                iban = account.getString(IBAN_API_KEY);
                Log.e(LOG_TAG, iban);

                if (iban.equalsIgnoreCase(mIbanLookingFor)) {
                    return account;
                }
            }
            return null;
        }
    }
}
