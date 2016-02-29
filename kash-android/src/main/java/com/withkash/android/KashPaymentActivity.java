package com.withkash.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;


/**
 * Created by geoff on 2016-02-19.
 */
public class KashPaymentActivity extends Activity {

    private final static String API_BASE_URL = "https://api.withkash.com/v1/gateway/order";

    private WebView mWebView;
    private FrameLayout mFrameLayout;

    public static final String RESULT_OK = "com.withkash.android.KashPaymentActivity.RESULT_OK";
    public static final String RESULT_CANCELED = "com.withkash.android.KashPaymentActivity.RESULT_CANCELED";
    private String mPublishableKey;
    private String mAmount;
    private String mEmail;
    private boolean mTesting;
    private String mFirstName;
    private String mLastName;
    private String mPhoneNumber;

    // Helper static function which launches this activity with an intent
     public static void launchKashPaymentActivity(Activity launcher, String publishableKey, String amount, String email, String firstName, String lastName, String phoneNumber, int requestCode) {
         Intent intent = new Intent(launcher, KashPaymentActivity.class);
         intent.putExtra("publishable_key", publishableKey);
         intent.putExtra("amount", amount);
         intent.putExtra("email", email);

         if (firstName != null) {
             intent.putExtra("first_name", firstName);
         }

         if (lastName != null) {
             intent.putExtra("last_name", lastName);
         }

         if (phoneNumber != null) {
             intent.putExtra("phone_number", phoneNumber);
         }

         intent.putExtra("testing", false);
         launcher.startActivityForResult(intent, requestCode);
    }

     // Helper static function which launches this activity with an intent
     public static void launchKashPaymentActivityForTesting(Activity launcher, String publishableKey, String amount, String email, String firstName, String lastName, String phoneNumber, int requestCode) {
         Intent intent = new Intent(launcher, KashPaymentActivity.class);
         intent.putExtra("publishable_key", publishableKey);
         intent.putExtra("amount", amount);
         intent.putExtra("email", email);
         intent.putExtra("testing", true);

         if (firstName != null) {
             intent.putExtra("first_name", firstName);
         }

         if (lastName != null) {
             intent.putExtra("last_name", lastName);
         }

         if (phoneNumber != null) {
             intent.putExtra("phone_number", phoneNumber);
         }

         launcher.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kash_payment_activity);

        Intent launchedWithIntent = getIntent();
        mPublishableKey = launchedWithIntent.getStringExtra("publishable_key");
        mAmount = launchedWithIntent.getStringExtra("amount");
        mEmail = launchedWithIntent.getStringExtra("email");
        mTesting = launchedWithIntent.getBooleanExtra("testing", false);

        mFirstName = launchedWithIntent.getStringExtra("first_name");
        mLastName = launchedWithIntent.getStringExtra("last_name");
        mPhoneNumber = launchedWithIntent.getStringExtra("phone_number");

        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.clearCache(true);
        mWebView.clearHistory();

        mFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        mFrameLayout.addView(mWebView);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String path = Uri.parse(url).getPath();
                if (path.equals("/cancel")) {
                    cancel();
                    return true;
                }
                else if (path.contains("/complete")) {
                    complete(url);
                    return true;
                }
                return false;
            }
        });

        String url = API_BASE_URL
                + "?publishable_key=" + mPublishableKey
                + "&x_amount=" + mAmount
                + "&x_customer_email=" + mEmail;


        if (mFirstName != null) {
            url = url + "&x_customer_first_name=" + mFirstName;
        }

        if (mLastName != null) {
            url = url + "&x_customer_last_name=" + mLastName;
        }

        if (mPhoneNumber != null) {
            url = url + "&x_customer_phone=" + mPhoneNumber;
        }

        if (mTesting) {
            url = url + "&x_test=true";
        }
        mWebView.loadUrl(url);
    }

    public void cancel() {

        Intent result = new Intent(RESULT_CANCELED);
        setResult(Activity.RESULT_CANCELED, result);
        finish();
    }

    public void complete(String url) {
        String referenceId = Uri.parse(url).getQueryParameter("x_gateway_reference");
        Intent result = new Intent(RESULT_OK);
        result.putExtra("reference_id", referenceId);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

}
