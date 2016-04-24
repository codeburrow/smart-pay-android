package com.example.android.fintech_hackathon;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class ShowQRcodeActivity extends AppCompatActivity {

    // LOG_TAG
    private static final String TAG = ShowQRcodeActivity.class.getSimpleName();
    public static final String QR_CODE_JPG = "qrcode.jpg";
    // ImageView for the QR code
    private ImageView qrCodeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);

        qrCodeImg = (ImageView) findViewById(R.id.imageView);

        try {
            // get input stream
            InputStream inputStream = getAssets().open(QR_CODE_JPG);
            // load image as Drawable
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            // set image to ImageView
            qrCodeImg.setImageDrawable(drawable);
        }
        catch(IOException ex) {
            Log.e(TAG, ex.toString());
        }
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, ScanQRcodeActivity.class);
        startActivity(backIntent);
    }

    public void startAccountRefresh(View view) {
        Intent refreshAccountIntent = new Intent(this, RefreshAccountActivity.class);
        startActivity(refreshAccountIntent);
    }
}
