package com.example.android.fintech_hackathon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransferMoneyActivity extends AppCompatActivity {

    public static final String IBAN_QR_CODE_JSON_KEY = "iban";
    public static final String AMOUNT_OF_MONEY_QR_CODE_KEY = "amount-of-money";
    private static final String TAG = TransferMoneyActivity.class.getSimpleName();
    private static final String IBAN = "iban";
    String qrCodeJson;

    // the IBAN_API_KEY we are looking for
    String mIbanLookingFor;

    private String iban = "IBAN1124837027";
    private String amountOfMoney = "20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

//        Intent transferMoneyIntent = getIntent();
//
//        qrCodeJson = transferMoneyIntent.getStringExtra(IBAN);
//
//        try {
//            JSONObject qrCodeJsonObject = new JSONObject(qrCodeJson);
//            iban = qrCodeJsonObject.getString(IBAN_QR_CODE_JSON_KEY);
//            amountOfMoney = qrCodeJsonObject.getString(AMOUNT_OF_MONEY_QR_CODE_KEY);
//
//            Log.e(TAG, "iban: " + iban + ", amount of money: " + amountOfMoney);
//
//            Context context = getApplicationContext();
//            CharSequence text = "iban: " + iban + ", amount of money: " + amountOfMoney;
//            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//            toast.show();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        mIbanLookingFor = iban;

        CheckIbanAccountTask checkIBANAccountTask = new CheckIbanAccountTask();
        checkIBANAccountTask.execute(LoginActivity.API_KEY, mIbanLookingFor);
    }

    private class CheckIbanAccountTask extends AsyncTask<String, Void, JSONObject> {
        private static final String ACCOUNTS_API_KEY = "accounts";
        private static final String IBAN_API_KEY = "IBAN";
        private final String LOG_TAG = CheckIbanAccountTask.class.getSimpleName();

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
                    Log.e(TAG, "Found: " + iban);
                    return account;
                }
            }
            return null;
        }
    }

}
