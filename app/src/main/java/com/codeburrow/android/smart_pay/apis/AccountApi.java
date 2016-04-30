package com.codeburrow.android.smart_pay.apis;

import android.util.Log;

import org.json.JSONArray;
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

    public JSONObject findByIban(String iban) {
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

    /**
     * Get the first owner (customer_number in Customer.Api terms) of this account
     *
     * @param account
     * @return
     */
    public static String findFirstCustomerNumberFromAccount(JSONObject account){
        try {
            JSONArray owners = account.getJSONArray(Api.ACCOUNT_OWNERS_KEY);

            return owners.get(0).toString();
        } catch (JSONException e) {
            e.getStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

}
