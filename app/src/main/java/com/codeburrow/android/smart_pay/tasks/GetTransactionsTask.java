package com.codeburrow.android.smart_pay.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.apis.Api;
import com.codeburrow.android.smart_pay.apis.TransactionApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since  5/2/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class GetTransactionsTask extends AsyncTask<Void, Void, JSONObject> {

    private final String LOG_TAG = GetTransactionsTask.class.getSimpleName();
    private final Context mContext;
    public TransactionAsyncResponse asyncResponse = null;
    private JSONArray transactions = null;
    private String mError = null;

    public GetTransactionsTask(Context context, TransactionAsyncResponse asyncResponse) {
        super();

        this.asyncResponse = asyncResponse;
        this.mContext = context;
    }

    @Override
    protected JSONObject doInBackground(Void... args) {
        TransactionApi transactionApi = new TransactionApi();
        return transactionApi.getAllTransactions(null);
    }

    @Override
    protected void onPostExecute(JSONObject apiResponse) {
        if (null == apiResponse) {
            mError = "No API Response.";
            Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (!apiResponse.has(Api.TRANSACTIONS_KEY)) {
                mError = "API Error.";
                Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, apiResponse.toString());
                return;
            }

            transactions = apiResponse.getJSONArray(Api.TRANSACTIONS_KEY);

            if (transactions.length() == 0) {
                mError = "No transactions found!";
                Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, transactions.toString());
                return;
            }

            Toast.makeText(mContext, "Transactions found.", Toast.LENGTH_SHORT).show();

            Log.e(LOG_TAG, transactions.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "CATCH EXCEPTION: " + e.getMessage());

            mError = e.toString();
        } finally {
            asyncResponse.processGetTransactionsAsyncFinish(transactions, mError);
        }
    }

    public interface TransactionAsyncResponse {
        void processGetTransactionsAsyncFinish(JSONArray transactions, String errorFound);
    }
}