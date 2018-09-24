package com.bongo.pushservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    public static String CHANNEL_ID="push_test_channel";
    public static String KEY_USER_TYPE="bioscope_user_type";
    public static String KEY_PAYMENT_GATEWAY="payment_gateway";
    private static String KEY_COUNTRY_CODE="country_code";



    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText tvCountry;
    private EditText tvUserType;
    private EditText tvPamentGateway;
    private SharedPreferencesUtils sharedPreferencesUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createNotificationChannel();
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logFirebaseProperty();
            }
        });
    }

    private void logFirebaseProperty() {

        sharedPreferencesUtils.editStringValue(KEY_COUNTRY_CODE,getCountry().toUpperCase());
        sharedPreferencesUtils.editStringValue(KEY_PAYMENT_GATEWAY,getPaymentGateWay().toUpperCase());
        sharedPreferencesUtils.editStringValue(KEY_USER_TYPE,getUserType().toUpperCase());

        mFirebaseAnalytics.setUserProperty(KEY_COUNTRY_CODE, getCountry().toUpperCase());
        mFirebaseAnalytics.setUserProperty(KEY_USER_TYPE, getUserType().toUpperCase());
        mFirebaseAnalytics.setUserProperty(KEY_PAYMENT_GATEWAY, getPaymentGateWay().toUpperCase());

        Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_LONG).show();
    }


    private void init() {
       sharedPreferencesUtils=SharedPreferencesUtils.getInstance(this);

        tvCountry=findViewById(R.id.tvCountry);
        tvPamentGateway=findViewById(R.id.tvPaymentGateWay);
        tvUserType=findViewById(R.id.tvUserType);

        tvCountry.setText(sharedPreferencesUtils.getStringValue(KEY_COUNTRY_CODE,null));

        tvPamentGateway.setText(sharedPreferencesUtils.getStringValue(KEY_PAYMENT_GATEWAY,null));
        tvUserType.setText(sharedPreferencesUtils.getStringValue(KEY_USER_TYPE,null));

        mFirebaseAnalytics=FirebaseAnalytics.getInstance(getApplicationContext());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test_channel";
            String description = "target_push_test";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public String getCountry() {
        return tvCountry.getText().toString();
    }



    public String getUserType() {
        return tvUserType.getText().toString();
    }

    public String getPaymentGateWay() {
        return tvPamentGateway.getText().toString();
    }
}
