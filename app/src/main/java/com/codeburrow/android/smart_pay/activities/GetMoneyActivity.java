package com.codeburrow.android.smart_pay.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.codeburrow.android.smart_pay.R;
import com.google.android.gms.drive.Contents;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.io.InputStream;

public class GetMoneyActivity extends AppCompatActivity {
    private static final String TAG = GetMoneyActivity.class.getSimpleName();
    public static final String QR_CODE_JPG = "qrcode.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr_code);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        String qrData = "Data I want to encode in QR code";
        int qrCodeDimention = 500;

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);

        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }






//        ImageView qrCodeImg = (ImageView) findViewById(R.id.imageView);

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