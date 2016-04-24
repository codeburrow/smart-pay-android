package com.example.android.fintech_hackathon;

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
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanQRcodeActivity extends AppCompatActivity {

    // LOG_TAG
    private static final String TAG = ScanQRcodeActivity.class.getSimpleName();
    private static final String IBAN = "iban";
    // Request code for runtime permissions
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    // SurfaceView in order to display the preview frames captured by the camera.
    SurfaceView cameraView;
    // TextView to display the contents of the QR codes the API detects
    TextView qrCodeTextView;
    // CameraSource
    CameraSource cameraSource;
    // Scanned IBAN
    private String scannedIban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        // Get SurfaceView from the xml.
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        qrCodeTextView = (TextView) findViewById(R.id.qr_code_detals_text_view_fake);

        /**
         * the CameraSource needs a BarcodeDetector,
         * create one using the BarcodeDetector.Builder class.
         */
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        /*
            To fetch a stream of images from the device’s camera
            and display them in the SurfaceView,
            create a new instance of the CameraSource class using CameraSource.Builder.
        */
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        /*
            Add a callback to the SurfaceHolder of the SurfaceView
            so that you know when you can start drawing the preview frames.
         */
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Call the start method of the CameraSource to start drawing the preview frames.
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
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
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
                // Get the SparseArray of Barcode objects by calling the getDetectedItems() method
                // of the Detector.Detections class.
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (isQrCodeValid(barcodes)) {
                    handleQrCodeDetection(barcodes);
                }
            }

            private void handleQrCodeDetection(final SparseArray<Barcode> barcodes) {
                // Use the post method of the TextView
                qrCodeTextView.post(new Runnable() {
                    public void run() {
                        // Update the TextView
                        scannedIban = barcodes.valueAt(0).rawValue;

                        qrCodeTextView.setText(scannedIban);

                        Intent trasferMoneyIntent = new Intent(getApplicationContext(), TransferMoneyActivity.class);
                        trasferMoneyIntent.putExtra(IBAN, scannedIban);
                        startActivity(trasferMoneyIntent);
                    }
                });
            }

            private boolean isQrCodeValid(SparseArray<Barcode> barcodes) {
                return barcodes.size() != 0;
            }
        });

    }

    public void showQRcode(View view) {
        Intent showQRcodeIntent = new Intent(this, ShowQRcodeActivity.class);
        startActivity(showQRcodeIntent);
    }

    public void showDetails(View view) {
        Intent refreshAccountIntent = new Intent(this, RefreshAccountActivity.class);
        startActivity(refreshAccountIntent);
    }
}
