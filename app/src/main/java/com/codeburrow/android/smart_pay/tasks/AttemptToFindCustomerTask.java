package com.codeburrow.android.smart_pay.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.apis.Api;
import com.codeburrow.android.smart_pay.apis.CustomerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/30/2016
 */

public class AttemptToFindCustomerTask extends AsyncTask<Void, Void, JSONObject> {
    private final String LOG_TAG = AttemptToFindAccountTask.class.getSimpleName();
    private final Context mContext;
    public CustomerAsyncResponse delegate = null;
    private String customerNumber;

    public AttemptToFindCustomerTask(Context context, CustomerAsyncResponse delegate,
                                     String customerNumber) {
        super();

        this.delegate = delegate;
        this.mContext = context;
        this.customerNumber = customerNumber;
    }

    @Override
    protected JSONObject doInBackground(Void... args) {
        CustomerApi customerApi = new CustomerApi();
        return customerApi.findByCustomerNumber(customerNumber);
    }

    @Override
    protected void onPostExecute(JSONObject apiResponse) {
        if (null == apiResponse) {
            Toast.makeText(mContext, "No API Response.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (!apiResponse.has(Api.CUSTOMERS_KEY)) {
                Toast.makeText(mContext, "API Error.", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, apiResponse.toString());
                return;
            }

            JSONArray customers = apiResponse.getJSONArray(Api.CUSTOMERS_KEY);

            if (customers.length() == 0) {
                Toast.makeText(mContext, "Customer Validation Failed.", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, customers.toString());
                return;
            }

            JSONObject customer = (JSONObject) customers.get(0);

            Toast.makeText(mContext, "Customer validation successful.", Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, customer.toString(), Toast.LENGTH_LONG).show();

            Log.e(LOG_TAG, customer.toString());
            delegate.processFindCustomerAsyncFinish(customer);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "CATCH EXCEPTION: " + e.getMessage());
        }
    }

    public interface CustomerAsyncResponse {
        void processFindCustomerAsyncFinish(JSONObject customer);
    }
}