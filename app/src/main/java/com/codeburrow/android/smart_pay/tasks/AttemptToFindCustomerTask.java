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
 * @since 4/30/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class AttemptToFindCustomerTask extends AsyncTask<Void, Void, JSONObject> {
    private final String LOG_TAG = AttemptToFindCustomerTask.class.getSimpleName();
    private final Context mContext;
    public CustomerAsyncResponse asyncResponse = null;
    private JSONObject customer = null;
    private String mError = null;
    private String customerNumber;

    public AttemptToFindCustomerTask(Context context, CustomerAsyncResponse asyncResponse,
                                     String customerNumber) {
        super();

        this.asyncResponse = asyncResponse;
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
            mError = "No API Response.";
            Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (!apiResponse.has(Api.CUSTOMERS_KEY)) {
                mError = "API Error.";
                Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, apiResponse.toString());
                return;
            }

            JSONArray customers = apiResponse.getJSONArray(Api.CUSTOMERS_KEY);

            if (customers.length() == 0) {
                mError = "Invalid Credentials.";
                Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, customers.toString());
                return;
            }

            customer = (JSONObject) customers.get(0);

            Toast.makeText(mContext, "Customer validation successful.", Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, customer.toString(), Toast.LENGTH_LONG).show();

            Log.e(LOG_TAG, customer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "CATCH EXCEPTION: " + e.getMessage());
        } finally {
            asyncResponse.processFindCustomerAsyncFinish(customer, mError);
        }
    }

    public interface CustomerAsyncResponse {
        void processFindCustomerAsyncFinish(JSONObject customer, String errorFound);
    }
}