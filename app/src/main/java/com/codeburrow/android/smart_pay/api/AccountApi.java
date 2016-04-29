package com.codeburrow.android.smart_pay.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/29/16
 */
public class AccountApi extends Api {

    static final String TAG = AccountApi.class.getSimpleName();
    static String API_URL = "https://nbgdemo.azure-api.net/nodeopenapi/api/accounts/rest";

    public String findByIban(String iban) {
        String nbgTrackId = UUID.randomUUID().toString();
        JSONObject parametersJson = new JSONObject();
        JSONObject payloadJson = new JSONObject();

        try {
            parametersJson.put(NBG_TRACK_ID, nbgTrackId);
            payloadJson.put(IBAN, iban);
            parametersJson.put(PAYLOAD, payloadJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return makePostRequest(API_URL, parametersJson);
    }
}
