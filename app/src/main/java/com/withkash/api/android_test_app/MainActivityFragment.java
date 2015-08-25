package com.withkash.api.android_test_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.withkash.android.KashPayment;

import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        final Activity thisActivity = this.getActivity();
        final Button button = (Button) view.findViewById(R.id.payButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HashMap<String,String> options = new HashMap<String, String>();
                options.put("apiEndpoint", "https://api-test.withkash.com/v1");

                new KashPayment("Insert Your Test Key")
                        .show(thisActivity, options, new KashPayment.ResultHandler() {
                            @Override
                            public void onComplete(KashPayment.Result result) {
                                Log.v("KASH", "Direct Debit Completed: " + result.toString());
                            }

                            @Override
                            public void onCancel() {
                                Log.v("KASH", "Direct Debit Cancelled");
                            }
                        });
            }
        });

        return view;
    }
}
