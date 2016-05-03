package com.codeburrow.android.smart_pay.apis;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @author George Spiridakis <george@codeburrow.com>
 * @since 4/30/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class TransactionApi extends Api {

    private static final String LOG_TAG = TransactionApi.class.getSimpleName();
    private static String API_URL = "https://nbgdemo.azure-api.net/nodeopenapi/api/transactions/rest";

    public JSONObject getAllTransactions(List<NameValuePair> params){
        return makeGetRequest(API_URL, null);
    }
}
