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
 * @since 5/2/2016.
 * ====================================================
 * CODE BURROW
 * ====================================================
 */
public class GetTransactionsTask extends AsyncTask<Void, Void, JSONObject> {

    private final String LOG_TAG = AttemptToFindAccountTask.class.getSimpleName();
    private final Context mContext;
    public TransactionAsyncResponse delegate = null;

    public GetTransactionsTask(Context context, TransactionAsyncResponse delegate) {
        super();

        this.delegate = delegate;
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
            Toast.makeText(mContext, "No API Response.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (!apiResponse.has(Api.TRANSACTIONS_KEY)) {
                Toast.makeText(mContext, "API Error.", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, apiResponse.toString());
                return;
            }

            JSONArray transactions = apiResponse.getJSONArray(Api.TRANSACTIONS_KEY);

            if (transactions.length() == 0) {
                Toast.makeText(mContext, "No transactions found!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, transactions.toString());
                return;
            }

            Toast.makeText(mContext, "Transactions found.", Toast.LENGTH_SHORT).show();

            Log.e(LOG_TAG, transactions.toString());
            delegate.processGetTransactionsAsyncFinish(transactions);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "CATCH EXCEPTION: " + e.getMessage());
        }
    }

    public interface TransactionAsyncResponse {
        void processGetTransactionsAsyncFinish(JSONArray transactions);
    }
}