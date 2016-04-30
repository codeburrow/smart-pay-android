package com.codeburrow.android.smart_pay.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.encoders.QRCodeEncoder;
import com.google.zxing.WriterException;

public class GetMoneyActivity extends AppCompatActivity {
    private static final String TAG = GetMoneyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr_code);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        String qrData = "Data I want to encode in QR code";

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData);

        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


//        ImageView qrCodeImg = (ImageView) findViewById(R.id.imageView);

//        try {
//            // get input stream
//            InputStream inputStream = getAssets().open(QR_CODE_JPG);
//            // load image as Drawable
//            Drawable drawable = Drawable.createFromStream(inputStream, null);
//            // set image to ImageView
//            qrCodeImg.setImageDrawable(drawable);
//        } catch (IOException ex) {
//            Log.e(TAG, ex.toString());
//        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }
}