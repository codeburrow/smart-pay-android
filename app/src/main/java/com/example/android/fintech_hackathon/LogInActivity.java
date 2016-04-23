package com.example.android.fintech_hackathon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = LogInActivity.class.getSimpleName();
    private static final String API_KEY = "d8dd6ba4f73444f6b2ef5a92d1a98b6f";

    // EditText
    EditText loginEditText;
    // Button
    Button loginButton;
    // the IBAN we are looking for
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
                Log.e(TAG, "IBAN looking for: " + mIbanLookingFor);
                CheckIBANAccountTask checkIBANAccountTask = new CheckIBANAccountTask();
                checkIBANAccountTask.execute(API_KEY, mIbanLookingFor);
            }
        });


    }

    private class CheckIBANAccountTask extends AsyncTask<String, Void, Void> {

        private static final String ACCOUNTS = "accounts";
        private static final String IBAN = "IBAN";


        private final String LOG_TAG = CheckIBANAccountTask.class.getSimpleName();


        @Override
        protected Void doInBackground(String... args) {

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

            Log.e(LOG_TAG, jsonResponse.toString());

            try {
                JSONArray accounts = jsonResponse.getJSONArray(ACCOUNTS);

                for (int index = 0; index <= accounts.length(); index ++){
                    JSONObject account = accounts.getJSONObject(index);

                    String iban = account.getString(IBAN);

                    Log.e(LOG_TAG, "Account " + index + ": " + iban);

                    if (iban.equals(mIbanLookingFor)){
                        Log.e(LOG_TAG, "Account Found" + iban);
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


    }

}
