package com.codeburrow.android.smart_pay.activities;

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

import com.codeburrow.android.smart_pay.JsonParser;
import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.apis.AccountApi;
import com.codeburrow.android.smart_pay.apis.CustomerApi;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindAccountTask;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindAccountTask.AccountAsyncResponse;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindCustomerTask;
import com.codeburrow.android.smart_pay.tasks.AttemptToFindCustomerTask.CustomerAsyncResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/23-24/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class TransferMoneyActivity extends AppCompatActivity implements AccountAsyncResponse, CustomerAsyncResponse {

    public static final String LOG_TAG = TransferMoneyActivity.class.getSimpleName();

    private EditText passEditText;
    private TextView infoTextView;

    // QR code information
    private String mIban;
    private String mAmountOfMoney;
    private boolean showReceiverToUser = false;

    private String receiverOwnerName;

    private String uuid = "codeburrow.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        passEditText = (EditText) findViewById(R.id.password_edittext);
        infoTextView = (TextView) findViewById(R.id.info_text_view);

        renderTransactionData();

        AttemptToFindAccountTask attemptToFindAccountTask = new AttemptToFindAccountTask(getApplicationContext(), this, mIban);
        attemptToFindAccountTask.execute();
    }

    private void renderTransactionData() {
        mAmountOfMoney = getIntent().getStringExtra(ScanQrCodeActivity.AMOUNT_OF_MONEY_EXTRA);
        mIban = getIntent().getStringExtra(ScanQrCodeActivity.IBAN_EXTRA);

        Log.e(LOG_TAG, "Send to\nQR_INFO: " + mIban + "\nThe amount of: " + mAmountOfMoney + "$\nuuid: " + uuid);
        updateInfoText();
    }

    public void updateInfoText() {
        if (showReceiverToUser) {
            infoTextView.setText("Send to\nNAME: " + receiverOwnerName + "\nIBAN: " + mIban + "\nThe amount of: " + mAmountOfMoney + " EUR\nuuid: " + uuid);
        } else {
            infoTextView.setText("Scanned an INVALID IBAN");
        }
    }

    public void verifyTransaction(View view) {
        String password = passEditText.getText().toString();

        if (showReceiverToUser) {
            if (validatePassword(password)) {
                AttemptToMakeTransactionTask attemptToMakeTransactionTask = new AttemptToMakeTransactionTask();
                attemptToMakeTransactionTask.execute();
            } else {
                toast("Wrong password.");
            }
        } else {
            toast("Invalid IBAN");
        }

    }

    public boolean validatePassword(String password) {
        String storedPassword = getSharedPreferences(LoginActivity.PREFERENCES, Context.MODE_PRIVATE)
                .getString(LoginActivity.PASSWORD_PREFS_KEY, null);

        return storedPassword != null & storedPassword.equals(password);
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

    @Override
    public void processFindAccountAsyncFinish(JSONObject receiverAccount, String errorFound) {
        AttemptToFindCustomerTask attemptToFindCustomerTask = new AttemptToFindCustomerTask(getApplicationContext(), this, AccountApi.findFirstCustomerNumberFromAccount(receiverAccount));
        attemptToFindCustomerTask.execute();
    }

    @Override
    public void processFindCustomerAsyncFinish(JSONObject receiverCustomer, String errorFound) {
        receiverOwnerName = CustomerApi.findLegalNameFromCustomer(receiverCustomer);

        showReceiverToUser = true;

        updateInfoText();
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
