package com.codeburrow.android.smart_pay;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransferMoneyActivity extends AppCompatActivity {

    public static final String IBAN_QR_CODE_JSON_KEY = "iban";
    public static final String AMOUNT_OF_MONEY_QR_CODE_KEY = "amount-of-money";
    private static final String LOG_TAG = TransferMoneyActivity.class.getSimpleName();
    private static final String TRANSACTIONS_API_KEY = "transactions";
    private static final String UUID_API_KEY = "uuid";
    private EditText passEditText;
    private String iban;// = "IBAN1124837027";

    private String uuid = "codeburrow.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        passEditText = (EditText) findViewById(R.id.password_edittext);

        TextView infoTextView = (TextView) findViewById(R.id.info_text_view);

        Intent transferMoneyIntent = getIntent();
        String qrCodeJson = transferMoneyIntent.getStringExtra(ScanQrCodeActivity.IBAN);

        try {
            JSONObject qrCodeJsonObject = new JSONObject(qrCodeJson);

            iban = qrCodeJsonObject.getString(IBAN_QR_CODE_JSON_KEY);
            String amountOfMoney = qrCodeJsonObject.getString(AMOUNT_OF_MONEY_QR_CODE_KEY);

            infoTextView.setText("Send to\nIBAN: " + iban + "\nThe amount of: " + amountOfMoney + "$\nuuid: " + uuid);

            Log.e(LOG_TAG, "Send to\nIBAN: " + iban + "\nThe amount of: " + amountOfMoney + "$\nuuid: " + uuid);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
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

    private JSONObject findTransactionByUuid(JSONArray transactions, String uuid) throws JSONException {

        for (int index = 0; index <= transactions.length(); index++) {
            JSONObject transaction = transactions.getJSONObject(index);

            uuid = transaction.getString(UUID_API_KEY);

            if (uuid.equalsIgnoreCase(iban)) {
                return transaction;
            }
        }
        return null;
    }

    public void verifyTransaction(View view) {
        String pass = passEditText.getText().toString();
        if ("1234".equals(pass)) {
//        CountTransactionsTask countTransactionsTask = new CountTransactionsTask();
//        countTransactionsTask.execute(BuildConfig.NBG_API_KEY_TOKEN);

            AttemptToMakeTransactionTask attemptToMakeTransactionTask = new AttemptToMakeTransactionTask();
            attemptToMakeTransactionTask.execute();

        } else {
            toast("Wrong pass\nPlease try again");
        }
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

                JSONObject mAccountToSendMoney = account;
            }
        }


        private JSONObject findAccountByIban(JSONArray accounts) throws JSONException {
            String iban;

            for (int index = 0; index <= accounts.length(); index++) {
                JSONObject account = accounts.getJSONObject(index);

                iban = account.getString(IBAN_API_KEY);

                Log.e(LOG_TAG, iban);
                Log.e(LOG_TAG, "-------------" + TransferMoneyActivity.this.iban);
                if (iban.equalsIgnoreCase(TransferMoneyActivity.this.iban)) {
                    Log.e(LOG_TAG, "sdfgdsg-f-dgd-f-gdfsg-dfsg-sddfddggfggg" + iban);
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

    private class AttemptToMakeTransactionTask extends AsyncTask<Void, Void, String> {
        private final String TAG = AttemptToMakeTransactionTask.class.getSimpleName();

        @Override
        protected String doInBackground(Void... args) {
            String setTransactionUrl =
                    "https://nbgdemo.azure-api.net/nodeopenapi/api/transactions/rest";
            JsonParser jsonParser = new JsonParser();

            String nbgTrackId = UUID.randomUUID().toString();

            JSONObject jsonParams = new JSONObject();
            JSONObject jsonPayload = new JSONObject();
            JSONObject jsonInsert = new JSONObject();
            JSONObject jsonDetails = new JSONObject();
            JSONObject jsonValue = new JSONObject();

            try {
                jsonValue.put("amount", "200");
                jsonDetails.put("value", jsonValue);
                jsonDetails.put("posted_by_user_id", "571a162f95806d5414110f20");
                jsonDetails.put("approved_by_user_id", "571b6c423ddcdb580cbee7db");
                jsonInsert.put("details", jsonDetails);
                jsonInsert.put("uuid", uuid);
                jsonPayload.put("insert", jsonInsert);
                jsonParams.put("payload", jsonPayload);
                jsonParams.put("nbgtrackid", nbgTrackId);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

            JSONObject manJson = new JSONObject();

            return jsonParser.makePutRequest(setTransactionUrl, jsonParams);
        }

        @Override
        protected void onPostExecute(String apiResponse) {
            if (null == apiResponse) {
                toast("Transaction failed.");
            } else {
                Log.e(TAG, apiResponse);
                toast("Transaction verified.");

                Intent succesfulTransactionIntent = new Intent(getApplicationContext(), SuccessfulTransactionActivity.class);
                startActivity(succesfulTransactionIntent);
            }
        }
    }

}
