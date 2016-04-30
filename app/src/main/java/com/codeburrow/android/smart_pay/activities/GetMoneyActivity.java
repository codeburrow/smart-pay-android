package com.codeburrow.android.smart_pay.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.encoders.QRCodeEncoder;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

public class GetMoneyActivity extends AppCompatActivity {
    public static final String AMOUNT_OF_MONEY = "amount-of-money";
    public static final String IBAN = "iban";
    private static final String TAG = GetMoneyActivity.class.getSimpleName();
    private String mAmountOfMoney;
    private String mIban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr_code);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        try {
            renderQrCode(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(GetMoneyActivity.this, "System Error.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Render on UI the QR code.
     *
     * @param imageView The ImageView where the QR code will be generated.
     */
    private void renderQrCode(ImageView imageView) throws JSONException {
        String transactionData = generateTransactionData();

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(transactionData);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate transaction data in json format.
     *
     * @return String The transaction data.
     */
    private String generateTransactionData() throws JSONException {
        JSONObject jsonParams = new JSONObject();
        jsonParams.put(AMOUNT_OF_MONEY, mAmountOfMoney);
        jsonParams.put(IBAN, mIban);

        return jsonParams.toString();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }
}