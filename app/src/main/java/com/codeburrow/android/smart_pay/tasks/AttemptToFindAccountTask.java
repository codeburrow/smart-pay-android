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

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since  4/30/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class AttemptToFindAccountTask extends AsyncTask<Void, Void, JSONObject> {
    private final String LOG_TAG = AttemptToFindAccountTask.class.getSimpleName();
    private final Context mContext;
    public AccountAsyncResponse asyncResponse = null;
    private JSONObject account = null;
    private String mError = null;
    private String iban;

    public AttemptToFindAccountTask(Context context, AccountAsyncResponse asyncResponse, String iban) {
        super();

        this.asyncResponse = asyncResponse;
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
            mError = "No API Response.";
            Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (!apiResponse.has(Api.ACCOUNTS_KEY)) {
                mError = "API Error.";
                Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, apiResponse.toString());
                return;
            }

            JSONArray accounts = apiResponse.getJSONArray(Api.ACCOUNTS_KEY);

            if (accounts.length() == 0) {
                // The authentication only checks the IBAN typed by the user.
                // It does not do work with QR.
                mError = "Invalid Credentials.";
                Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, accounts.toString());
                return;
            }

            account = (JSONObject) accounts.get(0);

            Toast.makeText(mContext, "Authentication successful.", Toast.LENGTH_SHORT).show();

            Log.e(LOG_TAG, account.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "CATCH EXCEPTION: " + e.getMessage());

            mError = e.toString();
        } finally {
            asyncResponse.processFindAccountAsyncFinish(account, mError);
        }
    }

    public interface AccountAsyncResponse {
        void processFindAccountAsyncFinish(JSONObject foundAccount, String errorFound);
    }
}