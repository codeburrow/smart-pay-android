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

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @author Rizart Dokollari <r.dokollari@gmail.com>
 * @since 4/23-24/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */

public class GenerateQrCodeActivity extends AppCompatActivity {

    private static final String LOG_TAG = GenerateQrCodeActivity.class.getSimpleName();

    public static final String AMOUNT_OF_MONEY_QR_CODE_KEY = "amount-of-money";
    public static final String IBAN_QR_CODE_KEY = "iban";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        try {
            renderQrCode(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(GenerateQrCodeActivity.this, "System Error.", Toast.LENGTH_SHORT).show();
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
        Intent myIntent = getIntent();
        String amountOfMoney = myIntent.getStringExtra(InsertAmountToReceiveActivity.AMOUNT_OF_MONEY_EXTRA);
//        String iban = getSharedPreferences(LoginActivity.PREFERENCES, Context.MODE_PRIVATE)
//                .getString(LoginActivity.IBAN_PREFS_KEY, null);

        JSONObject jsonParams = new JSONObject();
        jsonParams.put(AMOUNT_OF_MONEY_QR_CODE_KEY, amountOfMoney);
//        jsonParams.put(IBAN_QR_CODE_KEY, iban);

        return jsonParams.toString();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }
}