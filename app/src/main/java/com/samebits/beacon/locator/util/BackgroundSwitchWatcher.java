package com.samebits.beacon.locator.util;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;


import com.samebits.beacon.locator.BeaconLocatorApp;

import org.altbeacon.beacon.logging.LogManager;

@TargetApi(18)
public class BackgroundSwitchWatcher implements Application.ActivityLifecycleCallbacks {
        @NonNull
        private static final String TAG = Constants.TAG;

        @NonNull
        private final Context mContext;

        private int activeActivityCount = 0;

        /**
         * Constructs a new BackgroundSwitchWatcher
         *
         * @param context
         */
    public BackgroundSwitchWatcher(Context context) {
            if (android.os.Build.VERSION.SDK_INT < 18) {
                LogManager.w(TAG, "BackgroundSwitchWatcher requires API 18 or higher.");
            }
            mContext = context;
            ((Application)context.getApplicationContext()).registerActivityLifecycleCallbacks(this);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            activeActivityCount++;
            LogManager.d(TAG, "activity started: %s active activities: %s", activity, activeActivityCount);

            if (activeActivityCount == 1) {
                BeaconLocatorApp.from(mContext).enableBackgroundScan(false);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activeActivityCount--;
            LogManager.d(TAG,"activity stopped: %s active activities: %s", activity, activeActivityCount);
            if (activeActivityCount <= 0) {
                BeaconLocatorApp.from(mContext).enableBackgroundScan(true);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }