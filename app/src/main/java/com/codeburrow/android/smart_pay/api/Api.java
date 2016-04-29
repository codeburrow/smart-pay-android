package com.codeburrow.android.smart_pay.api;

import android.util.Log;

import com.codeburrow.android.smart_pay.BuildConfig;
import com.codeburrow.android.smart_pay.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/29/16
 */
public abstract class Api {
    public static final String IBAN = "IBAN";
    public static final String NBG_TRACK_ID = "nbgtrackid";
    public static final String PAYLOAD = "payload";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
    public static final String APPLICATION_JSON = "application/json";
    private static final String TAG = JsonParser.class.getSimpleName();

    protected String makePutRequest(String apiUrl, JSONObject jsonParams) {
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
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
