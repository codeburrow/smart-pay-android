package com.codeburrow.android.smart_pay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.R;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/23-24/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class InsertAmountToReceiveActivity extends AppCompatActivity {

    private static final String LOG_TAG = InsertAmountToReceiveActivity.class.getSimpleName();

    public static final String AMOUNT_OF_MONEY_EXTRA = "amount-of-money";
    private EditText mAmountOfMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_amount_to_receive);

        mAmountOfMoney = (EditText) findViewById(R.id.amount_of_money_to_receive_edit_text);
    }

    public void receiveMoney(View view) {
        String amountOfMoney = mAmountOfMoney.getText().toString();

        if (userInputIsValid(amountOfMoney)) {
            Toast.makeText(InsertAmountToReceiveActivity.this, "Generating QR code...", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(this, GenerateQrCodeActivity.class);
            myIntent.putExtra(AMOUNT_OF_MONEY_EXTRA, amountOfMoney);
            startActivity(myIntent);
        }
    }

    private boolean userInputIsValid(String amountOfMoney) {
        try {
            if (Double.parseDouble(amountOfMoney) < 0) {
                Toast.makeText(InsertAmountToReceiveActivity.this, "Amount of money need to be positive.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException numberFormatException) {
            Toast.makeText(InsertAmountToReceiveActivity.this, "Not a valid amount of money", Toast.LENGTH_SHORT).show();
            return false;        }

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }
}