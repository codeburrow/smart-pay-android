package com.codeburrow.android.smart_pay.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.activities.LoginActivity;
import com.codeburrow.android.smart_pay.apis.EurobankApi;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/26/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class GetProductsTask extends AsyncTask<Void, Void, JSONObject> {

    private static String LOG_TAG = GetProductsTask.class.getSimpleName();

    private String mError;
    private final Context mContext;

    public GetProductsAsyncResponse mGetProductsAsyncResponse= null;

    public interface GetProductsAsyncResponse {

        void processGetProductsAsyncFinish(JSONObject accounts, String errorFound);
    }

    public GetProductsTask(Context context, GetProductsAsyncResponse getProductsAsyncResponse) {
        this.mContext = context;
        this.mGetProductsAsyncResponse= getProductsAsyncResponse;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        EurobankApi eurobankApi = new EurobankApi();

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("customer_id", "me"));

        String storedAccessToken = mContext.getSharedPreferences(LoginActivity.PREFERENCES, Context.MODE_PRIVATE).getString(LoginActivity.ACCESS_TOKEN_KEY, null);

        return eurobankApi.makeGetRequest("http://api.beyondhackathon.com/customers/me/products", nameValuePairs, storedAccessToken);
    }

    @Override
    protected void onPostExecute(JSONObject apiResponse) {
        if (null == apiResponse) {
            mError = "No API Response.";
            Log.e(LOG_TAG, mError);
            Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
        } else {
            mError = "";
            Log.e(LOG_TAG, apiResponse.toString());
        }
        mGetProductsAsyncResponse.processGetProductsAsyncFinish(apiResponse, mError);
    }
}