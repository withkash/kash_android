## KashAPI for Android

The KashAPI for Android makes it easy to accept payments with Kash in your Android App.

Kash clears transactions without interchange or card fees and is guaranteed against chargeback. For more information visit [http://withkash.com](http://withkash.com).

## Quick Start

For a working example, see the example directory. This tutorial will walk you through creating that example from scratch.

### Testing vs Live

Kash provides two helper functions for invoking the KashPaymentActivity. One for testing and one for live transactions. See the example below or example app for details.

### Test credentials

For the purpose of testing you can use any combination of credentials, for example:

```
login: test
password: test
```

In test mode, security questions will also accept any answer as well:

```
What was the name of your highschool?
> 1
What was the color of your first car?
> 2
What year where you born in?
> 3

```

### Minimum SDK

The current minimum SDK version that the KASH API for Android supports is version 10 (2.3.3).

### Adding gradle Dependency

Open the build.gradle file for the app you will launching KashPaymentActivity from. Add 'compile 'com.withkash.android:kash-android:+' to your
dependencies section:

```
dependencies {
    ...
    compile 'com.withkash.android:kash-android:+'
}
```

Now sync your project with gradle: Tools -> Android -> Sync Project with Gradle Files

### Modify your activity

Open the activity you will be launching from, eg MainActivity.

Define the result code you want to use for Kash. Any unique integer will do.

```
// self defined result code for matching up results from KashPaymentActivity
static final int KASH_PAYMENT = 1;
```

Add the following method which launches KasyPaymentActivity:

```
void launchKashPaymentActivity() {
    // Your publishable key is available via the Kash Dashboard
    // note: the prefixes pk_test/pk_live determine whether the test of live api gets used
    String publishableKey = "<your key here>;

    // Simply the amount to be charged as a string in US dollars
    String amount = "10.00";

    // Your customers email
    String email = "john@doe.com";

    // Request code, use this is if you call multiple activities for results
    // and need to distinguish between their results
    int requestCode = KASH_PAYMENT;

    // Suggested: Customer first name, use null if you don't have it
    String firstName = "John";

    // Suggested: Customer last name, use null if you don't have it
    String lastName = "Doe";

    // Suggested: Customer phone number, use null if you don't have it
    String phoneNumber = "6504409852";

    // launch the KashPaymentActivity using this helper (remove the ForTesting for live transactions)
    KashPaymentActivity.launchKashPaymentActivityForTesting(this, publishableKey, amount, email, firstName, lastName, phoneNumber, requestCode);
}

```

Setup your onActivityResult handler:

```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

     // Check the requestCode to see if it's the Kash Payment We Launched
    if (requestCode == KASH_PAYMENT) {

        // Make sure the request was successful
        if (resultCode == RESULT_OK) {
            // payment succeeded, grab the reference_id
            String referenceId = data.getStringExtra("reference_id");
            // TODO store this server side along with your order
        }
        else if (resultCode == RESULT_CANCELED) {
            // payment failed and/or was cancelled
            // TODO present customer with payment options screen
        }
    }
}
```

## Internet permission required
Don't forget to add the Internet permission to your AndroidManifest.xml, it's probably there already unless you're following the tutorial from scratch.
```
<uses-permission android:name="android.permission.INTERNET" />
```
