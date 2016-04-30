package com.codeburrow.android.smart_pay.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.apis.AccountApi;
import com.codeburrow.android.smart_pay.apis.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttemptToFindAccountTask extends AsyncTask<Void, Void, JSONObject> {
    private final String LOG_TAG = AttemptToFindAccountTask.class.getSimpleName();
    private final Context mContext;
    public AccountAsyncResponse delegate = null;
    private String iban;

    public AttemptToFindAccountTask(Context context, AccountAsyncResponse delegate, String iban) {
        super();

        this.delegate = delegate;
        this.mContext = context;
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
            Toast.makeText(mContext, "No API Response.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (!apiResponse.has(Api.ACCOUNTS_KEY)) {
                Toast.makeText(mContext, "API Error.", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, apiResponse.toString());
                return;
            }

            JSONArray accounts = apiResponse.getJSONArray(Api.ACCOUNTS_KEY);

            if (accounts.length() == 0) {
                // Removed QR_INFO. The authentication only checks the IBAN typed by the user.
                // It does not do work with QR.
                // Remove this comment when you read this.
                // In any case, this task does not care from where it is used.
                // Toast your message from the QR activity.
                Toast.makeText(mContext, "Invalid Credentials.", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, accounts.toString());
                return;
            }

            JSONObject account = (JSONObject) accounts.get(0);

            Toast.makeText(mContext, "Authentication successful.", Toast.LENGTH_SHORT).show();

            Log.e(LOG_TAG, account.toString());
            delegate.processFindAccountAsyncFinish(account);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "CATCH EXCEPTION: " + e.getMessage());
        }
    }

    public interface AccountAsyncResponse {
        void processFindAccountAsyncFinish(JSONObject foundAccount);
    }
}