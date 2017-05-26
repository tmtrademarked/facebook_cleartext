package com.blueapron.facebook_cleartext;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

/**
 * Main application class for this app.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // This is basically "detectAll - detectActivityLeaks". The Activity testing framework
        // has an issue that can incorrectly hold references to our activities during a test -
        // which ends up triggering the leak detection. This results in highly flaky tests, which
        // is worse than the problem we're trying to solve. So to work around this issue, we only
        // enable this check in alpha builds, as opposed to the debug builds we run the tests
        // against on the build server.
        StrictMode.VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder()
                .detectFileUriExposure()
                // Commenting out because the first load of the WebView leaks a resource.
                // Related ticket: https://code.google.com/p/android/issues/detail?id=226751
                // This also causes bad behavior with Firebase. Disabling until this is fixed.
                //.detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
                // Disabled until Firebase fixes this:
                // https://code.google.com/p/android/issues/detail?id=229676
                //.detectLeakedSqlLiteObjects()
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
