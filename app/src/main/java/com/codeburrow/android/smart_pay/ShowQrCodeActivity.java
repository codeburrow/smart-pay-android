package com.codeburrow.android.smart_pay;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class ShowQrCodeActivity extends AppCompatActivity {
    private static final String TAG = ShowQrCodeActivity.class.getSimpleName();
    public static final String QR_CODE_JPG = "qrcode.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr_code);

        ImageView qrCodeImg = (ImageView) findViewById(R.id.imageView);

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
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }
}