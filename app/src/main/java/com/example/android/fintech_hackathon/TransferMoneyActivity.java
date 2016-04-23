package com.example.android.fintech_hackathon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferMoneyActivity extends AppCompatActivity {

    public static final String IBAN_QR_CODE_JSON_KEY = "iban";
    public static final String AMOUNT_OF_MONEY_QR_CODE_KEY = "amount-of-money";
    private static final String TAG = TransferMoneyActivity.class.getSimpleName();
    private static final String IBAN = "iban";
    String qrCodeJson;
    private String iban;
    private String amountOfMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent transferMoneyIntent = getIntent();

        qrCodeJson = transferMoneyIntent.getStringExtra(IBAN);

        try {
            JSONObject qrCodeJsonObject = new JSONObject(qrCodeJson);
            iban = qrCodeJsonObject.getString(IBAN_QR_CODE_JSON_KEY);
            amountOfMoney = qrCodeJsonObject.getString(AMOUNT_OF_MONEY_QR_CODE_KEY);

            Log.e(TAG, "iban: " + iban + ", amount of money: " + amountOfMoney);

            Context context = getApplicationContext();
            CharSequence text = "iban: " + iban + ", amount of money: " + amountOfMoney;
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
