package com.codeburrow.android.smart_pay.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.codeburrow.android.smart_pay.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ScanQrCodeActivity extends AppCompatActivity {
    public static final String LOG_TAG = ScanQrCodeActivity.class.getSimpleName();

    public static final String QR_INFO = "qrInfo";
    public static final String AMOUNT_OF_MONEY_EXTRA = "amount-of-money-extra";
    public static final String IBAN_EXTRA = "iban-extra";

    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    SurfaceView cameraView;
    CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);

        /**
         The CameraSource needs a BarcodeDetector,
         create one using the BarcodeDetector.Builder class.
         */
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        /**
         To fetch a stream of images from the device’s camera
         and display them in the SurfaceView,
         create a new instance of the CameraSource class using CameraSource.Builder.
         */
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        /**
         Add a callback to the SurfaceHolder of the SurfaceView
         so that you know when you can start drawing the preview frames.
         */
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
                        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                            return;
                        }
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (Exception exception) {
                    Log.e("CAMERA SOURCE", exception.getMessage());
                    Toast.makeText(ScanQrCodeActivity.this, "Camera is required.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // Call the stop method of the CameraSource to stop drawing the preview frames.
                cameraSource.stop();
            }
        });

        /**
         * Tell the BarcodeDetector what it should do when it detects a QR code.
         * Create an instance of a class that implements the Detector.Processor interface
         * and pass it to the setProcessor method of the BarcodeDetector.
         * Android Studio will automatically generate stubs for the methods of the interface.
         */
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (isQrCodeValid(barcodes)) {
                    handleQrCodeDetection(barcodes);
                    finish();
                }
            }

            private void handleQrCodeDetection(final SparseArray<Barcode> barcodes) {
                String qrCodeValues = barcodes.valueAt(0).rawValue;
                JSONObject json = null;
                try {
                    json = (JSONObject) new JSONParser().parse(qrCodeValues);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ScanQrCodeActivity.this, "Unable to extract data from QR code.", Toast.LENGTH_SHORT).show();
                }


                String amountOfMoney = (String) json.get(GenerateQrCodeActivity.AMOUNT_OF_MONEY_QR_CODE_KEY);
                String iban = (String) json.get(GenerateQrCodeActivity.IBAN_QR_CODE_KEY);

                Intent transferMoneyIntent = new Intent(getApplicationContext(), TransferMoneyActivity.class);
                transferMoneyIntent.putExtra(AMOUNT_OF_MONEY_EXTRA, amountOfMoney);
                transferMoneyIntent.putExtra(IBAN_EXTRA, iban);
                startActivity(transferMoneyIntent);
            }

            private boolean isQrCodeValid(SparseArray<Barcode> barcodes) {
                return barcodes.size() != 0;
            }
        });

    }

    public void startReceiveMoneyActivity(View view) {
        Intent showQrCodeIntent = new Intent(this, InsertAmountToReceiveActivity.class);
        startActivity(showQrCodeIntent);
    }

    public void showDetails(View view) {
//        Intent refreshAccountIntent = new Intent(this, RefreshAccountActivity.class);
//        startActivity(refreshAccountIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}