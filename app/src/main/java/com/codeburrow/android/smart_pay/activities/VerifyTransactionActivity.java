package com.codeburrow.android.smart_pay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codeburrow.android.smart_pay.R;

public class VerifyTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_transaction);
    }

    public void verifyTransaction(View view) {
        startActivity(new Intent(VerifyTransactionActivity.this, SuccessfulTransactionActivity.class));
    }
}
