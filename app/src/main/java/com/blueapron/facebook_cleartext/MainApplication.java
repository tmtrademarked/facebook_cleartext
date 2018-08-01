package com.blueapron.facebook_cleartext;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

/**
 * Main application class for this app.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Turn on extremely verbose debugging.
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.GRAPH_API_DEBUG_INFO);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.DEVELOPER_ERRORS);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_RAW_RESPONSES);

        String fbAppId = getString(R.string.facebook_app_id);
        FacebookSdk.setApplicationId(fbAppId);
        // We must manually call sdkInitialize() because we have a dynamic application ID.
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this, fbAppId);

        StrictMode.VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder()
                .detectFileUriExposure()
                .detectLeakedRegistrationObjects()
                .penaltyLog()
                .penaltyDeath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vmBuilder.detectCleartextNetwork();
        }
        StrictMode.setVmPolicy(vmBuilder.build());

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyFlashScreen()
                .penaltyDeath()
                .build());
    }
}
