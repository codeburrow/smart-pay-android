package com.codeburrow.android.smart_pay.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.EurobankProduct;
import com.codeburrow.android.smart_pay.EurobankProductArrayAdapter;
import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.tasks.GetProductsTask;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/23-24/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class TransferMoneyActivity extends AppCompatActivity implements GetProductsTask.GetProductsAsyncResponse {

    public static final String LOG_TAG = TransferMoneyActivity.class.getSimpleName();

    private TextView mInfoTextView;
    private ListView mAccountsListView;

    // QR code information
    private String mFirstName;
    private String mLastName;
    private String mAmountOfMoney;
    private boolean showReceiverToUser = false;

    private String receiverOwnerName;

    private String uuid = "codeburrow.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        mInfoTextView = (TextView) findViewById(R.id.info_text_view);
        mAccountsListView = (ListView) findViewById(R.id.accounts_list_view);
        ArrayList<EurobankProduct> accounts = new ArrayList<>();
        accounts.add(new EurobankProduct("account", "1325243598", 1200, 50, "euro"));
        accounts.add(new EurobankProduct("account", "4387589913", 200, 200, "euro"));
        accounts.add(new EurobankProduct("account", "1355543598", 120, 60, "euro"));
        accounts.add(new EurobankProduct("account", "1355543598", 5520, 5520, "euro"));
        accounts.add(new EurobankProduct("account", "6676766698", 120, 1250, "euro"));
        accounts.add(new EurobankProduct("account", "2353333398", 100, 100, "euro"));
        accounts.add(new EurobankProduct("account", "4387589913", 200, 200, "euro"));
        accounts.add(new EurobankProduct("account", "1355543598", 120, 60, "euro"));
        accounts.add(new EurobankProduct("account", "1355543598", 5520, 5520, "euro"));
        accounts.add(new EurobankProduct("account", "6676766698", 120, 1250, "euro"));
        accounts.add(new EurobankProduct("account", "4387589913", 200, 200, "euro"));
        accounts.add(new EurobankProduct("account", "1355543598", 120, 60, "euro"));
        accounts.add(new EurobankProduct("account", "1355543598", 5520, 5520, "euro"));
        accounts.add(new EurobankProduct("account", "6676766698", 120, 1250, "euro"));
        mAccountsListView.setAdapter(new EurobankProductArrayAdapter(this, accounts));

        renderTransactionData();

        // It is not executed.
        new GetProductsTask(this, this);
    }

    private void renderTransactionData() {
        mAmountOfMoney = getIntent().getStringExtra(ScanQrCodeActivity.AMOUNT_OF_MONEY_EXTRA);
        mFirstName = getIntent().getStringExtra("first_name");
        mLastName = getIntent().getStringExtra("last_name");

        Log.e(LOG_TAG, "Send to\n: " + mFirstName + " " + mLastName + "\nThe amount of: " + mAmountOfMoney);
        updateInfoText();
    }

    public void updateInfoText() {
        mInfoTextView.setText("Send to:\n " + mFirstName + " " + mLastName + "\nThe amount of:\n " + mAmountOfMoney);
    }

//    public void verifyTransaction(View view) {
//        String password = passEditText.getText().toString();
//
//        if (showReceiverToUser) {
//            if (validatePassword(password)) {
//                AttemptToMakeTransactionTask attemptToMakeTransactionTask = new AttemptToMakeTransactionTask();
//                attemptToMakeTransactionTask.execute();
//            } else {
//                toast("Wrong password.");
//            }
//        } else {
//            toast("Invalid IBAN");
//        }
//
//    }

    public boolean validatePassword(String password) {
//        String storedPassword = getSharedPreferences(LoginActivity.PREFERENCES, Context.MODE_PRIVATE)
//                .getString(LoginActivity.PASSWORD_PREFS_KEY, null);

//        return storedPassword != null & storedPassword.equals(password);
        return true;
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
        finish();
    }

    public void cancel(View view) {
        startActivity(new Intent(TransferMoneyActivity.this, ScanQrCodeActivity.class));
    }

    @Override
    public void processGetProductsAsyncFinish(JSONObject accounts, String errorFound) {
//        Log.e(LOG_TAG, accounts.toString());
    }

    public void next(View view) {
        startActivity(new Intent(TransferMoneyActivity.this, VerifyTransactionActivity.class));
    }

//    private class AttemptToMakeTransactionTask extends AsyncTask<Void, Void, String> {
//        private final String TAG = AttemptToMakeTransactionTask.class.getSimpleName();
//
//        @Override
//        protected String doInBackground(Void... args) {
//            String setTransactionUrl =
//                    "https://nbgdemo.azure-api.net/nodeopenapi/api/transactions/rest";
//            JsonParser jsonParser = new JsonParser();
//
//            String nbgTrackId = UUID.randomUUID().toString();
//
//            JSONObject jsonParams = new JSONObject();
//            JSONObject jsonPayload = new JSONObject();
//            JSONObject jsonInsert = new JSONObject();
//            JSONObject jsonDetails = new JSONObject();
//            JSONObject jsonValue = new JSONObject();
//
//            try {
//                jsonValue.put("amount", "200");
//                jsonDetails.put("value", jsonValue);
//                jsonDetails.put("posted_by_user_id", "571a162f95806d5414110f20");
//                jsonDetails.put("approved_by_user_id", "571b6c423ddcdb580cbee7db");
//                jsonInsert.put("details", jsonDetails);
//                jsonInsert.put("uuid", uuid);
//                jsonPayload.put("insert", jsonInsert);
//                jsonParams.put("payload", jsonPayload);
//                jsonParams.put("nbgtrackid", nbgTrackId);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.e(TAG, e.getMessage());
//            }
//
//            JSONObject manJson = new JSONObject();
//
//            return jsonParser.makePutRequest(setTransactionUrl, jsonParams);
//        }
//
//        @Override
//        protected void onPostExecute(String apiResponse) {
//            if (null == apiResponse) {
//                toast("Transaction failed.");
//            } else {
//                Log.e(TAG, apiResponse);
//                toast("Transaction verified.");
//
//                Intent succesfulTransactionIntent = new Intent(getApplicationContext(), SuccessfulTransactionActivity.class);
//                startActivity(succesfulTransactionIntent);
//            }
//        }
//    }

}
