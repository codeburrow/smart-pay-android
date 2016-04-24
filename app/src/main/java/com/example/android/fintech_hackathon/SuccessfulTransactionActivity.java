package com.example.android.fintech_hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SuccessfulTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_transaction);
    }

    public void scanAgain(View view) {
        Intent scanQRcodeIntent = new Intent(this, ScanQRcodeActivity.class);
        startActivity(scanQRcodeIntent);
    }
}
