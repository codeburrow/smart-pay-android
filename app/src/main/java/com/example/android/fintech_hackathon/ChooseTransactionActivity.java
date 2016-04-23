package com.example.android.fintech_hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChooseTransactionActivity extends AppCompatActivity {

    // LOG_TAG
    private static final String TAG = ChooseTransactionActivity.class.getSimpleName();
    // Buttons
    private Button scanQRcodeBtn;
    private Button showQRcodeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_transaction);

        // Get Buttons from xml
        scanQRcodeBtn = (Button) findViewById(R.id.scan_QRcode_button);
        showQRcodeBtn = (Button) findViewById(R.id.show_QRcode_button);
    }

    public void showQRcode(View view) {
        Intent showQRcodeIntent = new Intent(this, ShowQRcodeActivity.class);
        startActivity(showQRcodeIntent);
    }

    public void scanQRcode(View view) {
        Intent scanQRcodeIntent = new Intent(this, ScanQRcodeActivity.class);
        startActivity(scanQRcodeIntent);
    }
}
