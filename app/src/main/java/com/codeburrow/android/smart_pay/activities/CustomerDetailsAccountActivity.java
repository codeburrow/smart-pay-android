package com.codeburrow.android.smart_pay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.codeburrow.android.smart_pay.R;
import com.codeburrow.android.smart_pay.tasks.GetCustomerTask;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerDetailsAccountActivity extends AppCompatActivity implements GetCustomerTask.GetCustomerAsyncResponse {

    private static final String LOG_TAG = CustomerDetailsAccountActivity.class.getSimpleName();

    private TextView mIdTextView;
    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mCorporateTextView;
    private TextView mEmailTextView;
    private TextView mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_account);

        mIdTextView = (TextView) findViewById(R.id.id_text_view);
        mFirstNameTextView = (TextView) findViewById(R.id.first_name_text_view);
        mLastNameTextView = (TextView) findViewById(R.id.last_name_text_view);
        mCorporateTextView = (TextView) findViewById(R.id.corporate_text_view);
        mEmailTextView = (TextView) findViewById(R.id.email_text_view);
        mPhoneNumber = (TextView) findViewById(R.id.phone_number_text_view);

        new GetCustomerTask(this, this).execute();
    }

    @Override
    public void processGetCustomerAsyncFinish(JSONObject token, String errorFound) {
        Log.e(LOG_TAG, token.toString());

        try {
            mIdTextView.setText(token.getString("id"));
            mFirstNameTextView.setText(token.getString("first_name"));
            mLastNameTextView.setText(token.getString("last_name"));
            mCorporateTextView.setText(token.getString("corporate"));
            mEmailTextView.setText(token.getJSONArray("email_addresses").getString(0));
            mPhoneNumber.setText(token.getJSONArray("mobile_numbers").getString(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CustomerDetailsAccountActivity.this, ScanQrCodeActivity.class));
    }
}
