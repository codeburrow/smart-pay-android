package com.example.android.fintech_hackathon;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;


/*
    build.gradle(Module:app)

    android{
        useLibrary 'org.apache.http.legacy'
    }
 */

public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();

    static InputStream httpEntityContent = null;
    static JSONObject jsonObject = null;
    static String json = "";


    // Constructor
    public JsonParser() {

    }

    /**
     * @param url
     * @return
     */
    public JSONObject getJsonFromUrl(final String url) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            httpEntityContent = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntityContent, "UTF-8"), 8);
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

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jsonObject;

    }

    /**
     * @param url
     * @param method
     * @param params
     * @return
     */
    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            if (method.equalsIgnoreCase("POST")) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                httpEntityContent = httpEntity.getContent();

            } else if (method.equalsIgnoreCase("GET")) {
                String paramString = URLEncodedUtils.format(params, "UTF-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                httpEntityContent = httpEntity.getContent();
            } else if (method.equalsIgnoreCase("PUT")) {
                HttpPut httpPut = new HttpPut(url);
                JSONStringer jsonStringer = new JSONStringer();

                for (NameValuePair param : params) {
                    Log.e(TAG, "Debugging: " + param.getValue());
                    jsonStringer
                            .object()
                            .key(param.getName())
                            .value(param.getValue())
                            .endObject();
                }


                StringEntity stringEntity = new StringEntity(jsonStringer.toString());
                stringEntity.setContentEncoding(new BasicHeader("Ocp-Apim-Subscription-Key", LoginActivity.API_KEY_TOKEN));
                httpPut.setEntity(stringEntity);

                HttpResponse httpResponse = httpClient.execute(httpPut);
                HttpEntity httpEntity = httpResponse.getEntity();
                httpEntityContent = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        // try parse the string to a JSON object
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jsonObject;

    }

}
