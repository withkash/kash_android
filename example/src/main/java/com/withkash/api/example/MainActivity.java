package com.withkash.api.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.withkash.android.KashPaymentActivity;

public class MainActivity extends Activity {

    // self defined result code for matching up results from KashPaymentActivity
    static final int KASH_PAYMENT = 1;

    TextView mTitleTextView;
    TextView mDetailsTextView;
    Button mButton;

    void launchKashPaymentActivity() {
        // Your publishable key is available via the Kash Dashboard
        String publishableKey = "<your publishable key>";

        // Required: Simply the amount to be charged as a string in US dollars
        String amount = "10.00";

        // Required: Your customers email
        String email = "john@doe.com";

        // Suggested: Customer first name, use null if you don't have it
        String firstName = "John";

        // Suggested: Customer last name, use null if you don't have it
        String lastName = "Doe";

        // Suggested: Customer phone number, use null if you don't have it
        String phoneNumber = "6504409852";

        // Request code, use this is if you call multiple activities for results
        // and need to distinguish between their results
        int requestCode = KASH_PAYMENT;

        // Launch the KashPaymentActivity using this helper
        // Note that the publishable key you use determines wether this is a test or a live transaction
         KashPaymentActivity.launchKashPaymentActivityForTesting(this,
                publishableKey, amount,  email,  firstName,
                lastName, phoneNumber, requestCode);

        // For live transactions use the following helper instead
        // KashPaymentActivity.launchKashPaymentActivity(this,
        //        publishableKey, amount,  email,  firstName,
        //        lastName, phoneNumber, requestCode);
    }


     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         // Check the requestCode to see if it's the Kash Payment We Launched
        if (requestCode == KASH_PAYMENT) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // payment succeeded, grab the reference_id
                String referenceId = data.getStringExtra("reference_id");
                mTitleTextView.setText("Payment Successful");
                mDetailsTextView.setText("reference id: " + referenceId);
            }
            else if (resultCode == RESULT_CANCELED) {
                // payment failed and/or was cancelledw
                mTitleTextView.setText("Payment Cancelled");
                mDetailsTextView.setText("");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mDetailsTextView = (TextView) findViewById(R.id.detailsTextView);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchKashPaymentActivity();
            }
        });

        launchKashPaymentActivity();
    }
}
