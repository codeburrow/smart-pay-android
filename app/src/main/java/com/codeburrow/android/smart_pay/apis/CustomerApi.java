package com.codeburrow.android.smart_pay.apis;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @author George Spiridakis <george@codeburrow.com>
 * @since 4/30/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class CustomerApi extends Api {

    static final String LOG_TAG = AccountApi.class.getSimpleName();
    static String API_URL = "https://nbgdemo.azure-api.net/nodeopenapi/api/customers/rest";

    public JSONObject findByCustomerNumber(String customer_number) {
        String nbgTrackId = UUID.randomUUID().toString();
        JSONObject parametersJson = new JSONObject();
        JSONObject payloadJson = new JSONObject();

        try {
            parametersJson.put(NBG_TRACK_ID, nbgTrackId);
            payloadJson.put(CUSTOMER_NUMBER, customer_number);
            parametersJson.put(PAYLOAD, payloadJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.getMessage());
        }

        return makePostRequest(API_URL, parametersJson);
    }

    /**
     * Get the owner's name (legal_name in Customer.Api terms) of this customer
     *
     * @param customer
     * @return
     */
    public static String findLegalNameFromCustomer(JSONObject customer){
        try {
            return customer.getString(Api.CUSTOMER_LEGAL_NAME_KEY);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }
}
