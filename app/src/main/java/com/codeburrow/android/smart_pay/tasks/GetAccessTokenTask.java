package com.codeburrow.android.smart_pay.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
public class GetAccessTokenTask extends AsyncTask<Void, Void, JSONObject> {

    private static String LOG_TAG = GetAccessTokenTask.class.getSimpleName();

    private String mUsername;
    private String mPassword;
    private String mError;
    private final Context mContext;

    public AccessTokenAsyncResponse mAccessTokenAsyncResponse = null;

    public interface AccessTokenAsyncResponse {

        void processGetAccessTokenAsyncFinish(JSONObject token, String errorFound);
    }

    public GetAccessTokenTask(Context context, String username, String password, AccessTokenAsyncResponse accessTokenAsyncResponse) {
        this.mContext = context;
        this.mUsername = username;
        this.mPassword = password;
        this.mAccessTokenAsyncResponse  = accessTokenAsyncResponse;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        EurobankApi eurobankApi = new EurobankApi();

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        try {
            nameValuePairs.add(new BasicNameValuePair("client_id", "contestant"));
            nameValuePairs.add(new BasicNameValuePair("client_secret", "secret"));
            nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
            nameValuePairs.add(new BasicNameValuePair("username", mUsername));
            nameValuePairs.add(new BasicNameValuePair("password", mPassword));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eurobankApi.makePostRequest("http://api.beyondhackathon.com/authorization/token", nameValuePairs);
    }

    @Override
    protected void onPostExecute(JSONObject apiResponse) {
        if (null == apiResponse) {
            mError = "No API Response.";
            Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
        } else {
            mError = "";
            Log.e(LOG_TAG, apiResponse.toString());
        }
        mAccessTokenAsyncResponse.processGetAccessTokenAsyncFinish(apiResponse, mError);
    }
}
