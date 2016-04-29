package com.codeburrow.android.smart_pay.async_tasks;

/**
 * Created by giorgos on 4/29/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.api.AccountApi;

public class AttemptToFindAccountTask extends AsyncTask<Void, Void, String> {
    private final String TAG = AttemptToFindAccountTask.class.getSimpleName();
    private String iban;
    private final Context mContext;

    public AttemptToFindAccountTask(Context context, String iban) {
        super();

        this.mContext = context;
        this.iban = iban;
    }

    @Override
    protected String doInBackground(Void... args) {
        AccountApi accountApi = new AccountApi();
        return accountApi.findByIban(iban);
    }

    @Override
    protected void onPostExecute(String apiResponse) {
        if (null == apiResponse) {
            Toast.makeText(mContext, "No API Response.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, apiResponse, Toast.LENGTH_LONG).show();
        }
    }
}