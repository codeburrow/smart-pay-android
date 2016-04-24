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

    String mIbanLookingFor;

    private String iban = "IBAN1124837027";
    private String amountOfMoney = "20";
    private JSONObject mAccountToSendMoney;

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

//        CheckIbanAccountTask checkIbanAccountTask = new CheckIbanAccountTask();
//        checkIbanAccountTask.execute(LoginActivity.API_KEY_TOKEN, mIbanLookingFor);

//        CountTransactionsTask countTransactionsTask = new CountTransactionsTask();
//        countTransactionsTask.execute(LoginActivity.API_KEY_TOKEN);

        AttemptToMakeTransactionTask attemptToMakeTransactionTask = new AttemptToMakeTransactionTask();
        attemptToMakeTransactionTask.execute();
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

    private class CheckIbanAccountTask extends AsyncTask<String, Void, JSONObject> {
        private static final String ACCOUNTS_API_KEY = "accounts";
        private static final String IBAN_API_KEY = "IBAN";
        private final String LOG_TAG = CheckIbanAccountTask.class.getSimpleName();

        @Override
        protected JSONObject doInBackground(String... args) {
            // JSON Parser
            JsonParser jsonParser = new JsonParser();
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

                mAccountToSendMoney = account;
            }
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

    private class CountTransactionsTask extends AsyncTask<String, Void, String> {
        public static final String TRANSACTIONS_API_KEY = "transactions";
        // LOG_TAG
        private final String LOG_TAG = CountTransactionsTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... args) {
            // JSON Parser
            JsonParser jsonParser = new JsonParser();
            // GET url
            final String GET_TRANSACTIONS_LIST_URL =
                    "https://nbgdemo.azure-api.net/nodeopenapi/api/transactions/rest";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("key", args[0]));

            JSONObject jsonResponse = jsonParser.makeHttpRequest(GET_TRANSACTIONS_LIST_URL, "GET", params);

            try {
                JSONArray transactions = jsonResponse.getJSONArray(TRANSACTIONS_API_KEY);

                return transactions.length() + "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String numberOfTransactions) {
            if (null == numberOfTransactions) {
                toast("No transactions to count");
            } else {
                toast("Number Of Transactions: " + numberOfTransactions);
            }
        }
    }

    private class AttemptToMakeTransactionTask extends AsyncTask<Void, Void, JSONObject> {

        private final String TAG = AttemptToMakeTransactionTask.class.getSimpleName();

        @Override
        protected JSONObject doInBackground(Void... args) {
            String setTransactionUrl =
                    "https://nbgdemo.azure-api.net/testnodeapi/api/transactions/set";
            JsonParser jsonParser = new JsonParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("key", LoginActivity.API_KEY_TOKEN));
            params.add(new BasicNameValuePair("nbgtrackid", "15145645"));
            params.add(new BasicNameValuePair("payload[insert][uuid]", "443d9a28-34de-4496-badf-6e1f13ac04af"));
            params.add(new BasicNameValuePair("payload[insert][details][posted_by_user_id]", "571a162f95806d5414110f20"));
            params.add(new BasicNameValuePair("payload[insert][details][approved_by_user_id]", "571b6c423ddcdb580cbee7db"));
            params.add(new BasicNameValuePair("payload[insert][details][value][amount]", "200"));

            JSONObject jsonResponse = jsonParser.makeHttpRequest(setTransactionUrl, "POST", params);

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject httpResponseJsonObject) {
            try {
                toast(httpResponseJsonObject.getString("ok") + "");
                toast(httpResponseJsonObject.toString());
            } catch (JSONException e) {
                toast(e.getMessage());
            }
        }
    }
}
