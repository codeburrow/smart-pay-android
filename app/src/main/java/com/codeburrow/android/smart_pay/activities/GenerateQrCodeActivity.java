package com.codeburrow.android.smart_pay.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.encoders.QRCodeEncoder;
import com.codeburrow.android.smart_pay.tasks.GetCustomerTask;
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

public class GenerateQrCodeActivity extends AppCompatActivity implements GetCustomerTask.GetCustomerAsyncResponse {

    private static final String LOG_TAG = GenerateQrCodeActivity.class.getSimpleName();

    public static final String AMOUNT_OF_MONEY_QR_CODE_KEY = "amount-of-money";
    public static final String IBAN_QR_CODE_KEY = "iban";

    private ImageView mQrImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        mQrImageView = (ImageView) findViewById(R.id.imageView);

        new GetCustomerTask(this, this).execute();
    }

    /**
     * Render on UI the QR code.
     *
     * @param imageView The ImageView where the QR code will be generated.
     */
    private void renderQrCode(ImageView imageView, String id, String firstName, String lastName) throws JSONException {
        String transactionData = generateTransactionData(id, firstName, lastName);

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
    private String generateTransactionData(String id, String firstName, String lastName) throws JSONException {
        Intent myIntent = getIntent();
        String amountOfMoney = myIntent.getStringExtra(InsertAmountToReceiveActivity.AMOUNT_OF_MONEY_EXTRA);

        JSONObject jsonParams = new JSONObject();
        jsonParams.put(AMOUNT_OF_MONEY_QR_CODE_KEY, amountOfMoney);
        jsonParams.put("id", id);
        jsonParams.put("first_name", firstName);
        jsonParams.put("last_name", lastName);

        return jsonParams.toString();
    }

    @Override
    public void processGetCustomerAsyncFinish(JSONObject token, String errorFound) {
        try {
            String id = token.getString("id");
            String firstName = token.getString("first_name");
            String lastName = token.getString("last_name");
            try {
                renderQrCode(mQrImageView, id, firstName, lastName);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(GenerateQrCodeActivity.this, "System Error.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }
}