package com.codeburrow.android.smart_pay.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since 4/30/2016.
 * ====================================================
 * CODE BURROW
 * ====================================================
 */
public class CustomerApi extends Api {

    static final String LOG_TAG = AccountApi.class.getSimpleName();
    static String API_URL = "https://nbgdemo.azure-api.net/nodeopenapi/api/customers/rest";

    public JSONObject findByCustomerNumber(String iban) {
        String nbgTrackId = UUID.randomUUID().toString();
        JSONObject parametersJson = new JSONObject();
        JSONObject payloadJson = new JSONObject();

        try {
            parametersJson.put(NBG_TRACK_ID, nbgTrackId);
            payloadJson.put(CUSTOMER_NUMBER, iban);
            parametersJson.put(PAYLOAD, payloadJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.getMessage());
        }

        return makePostRequest(API_URL, parametersJson);
    }
}
