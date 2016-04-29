package com.codeburrow.android.smart_pay.async_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.api.AccountApi;
import com.codeburrow.android.smart_pay.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttemptToFindAccountTask extends AsyncTask<Void, Void, JSONObject> {
    private final String TAG = AttemptToFindAccountTask.class.getSimpleName();
    private final Context mContext;
    private String iban;
    public AsyncResponse delegate = null;

    // We may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish();
    }

    public AttemptToFindAccountTask(Context context, AsyncResponse delegate, String iban) {
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
                return;
            }

            JSONArray accounts = apiResponse.getJSONArray(Api.ACCOUNTS_KEY);

            if (accounts.length() == 0) {
                Toast.makeText(mContext, "IBAN Validation Failed.", Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject account = (JSONObject) accounts.get(0);

            Toast.makeText(mContext, "IBAN validation successful.", Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, account.toString(), Toast.LENGTH_LONG).show();

            delegate.processFinish();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }
}