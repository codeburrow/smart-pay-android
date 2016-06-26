package com.codeburrow.android.smart_pay.apis;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/26/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class EurobankApi {

    private static final String LOG_TAG = EurobankApi.class.getSimpleName();
    public static final String ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/json";

    public JSONObject makePostRequest(String url, List<NameValuePair> params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(ACCEPT, APPLICATION_JSON);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        StringEntity stringEntity = null;
        Log.e(LOG_TAG, params.toString());

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);

            return convertHttpResponse(httpResponse.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    public JSONObject makeGetRequest(String url, List<NameValuePair> params, String access_token) {
        if (null == params) {
            params = new ArrayList<>();
        }

        String paramString = URLEncodedUtils.format(params, "UTF-8");
        url += "?" + paramString;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Bearer " + access_token);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(httpGet);

            return convertHttpResponse(httpResponse.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.getMessage());
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
