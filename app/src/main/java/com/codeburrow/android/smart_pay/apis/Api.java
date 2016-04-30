package com.codeburrow.android.smart_pay.apis;

import android.util.Log;

import com.codeburrow.android.smart_pay.BuildConfig;
import com.codeburrow.android.smart_pay.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/29/16
 */
public abstract class Api {
    public static final String IBAN = "IBAN";
    public static final String CUSTOMER_NUMBER = "customer_number";
    public static final String NBG_TRACK_ID = "nbgtrackid";
    public static final String PAYLOAD = "payload";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
    public static final String APPLICATION_JSON = "application/json";
    private static final String TAG = JsonParser.class.getSimpleName();
    public static final String ACCOUNTS_KEY = "accounts";
    public static final String CUSTOMERS_KEY = "customers";

    protected JSONObject makePutRequest(String apiUrl, JSONObject jsonParams) {
        HttpPut httpPut = new HttpPut(apiUrl);
        httpPut.setHeader(CONTENT_TYPE, APPLICATION_JSON);
        httpPut.setHeader(OCP_APIM_SUBSCRIPTION_KEY, BuildConfig.NBG_API_KEY_TOKEN);

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        httpPut.setEntity(stringEntity);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPut);

            return convertHttpResponse(httpResponse.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public JSONObject makePostRequest(String url, JSONObject jsonParams) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(CONTENT_TYPE, APPLICATION_JSON);
        httpPost.setHeader(OCP_APIM_SUBSCRIPTION_KEY, BuildConfig.NBG_API_KEY_TOKEN);

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        httpPost.setEntity(stringEntity);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);

            return convertHttpResponse(httpResponse.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    private JSONObject convertHttpResponse(InputStream httpEntityContent) {
        String json = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpEntityContent, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            httpEntityContent.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jsonObject;
    }
}
