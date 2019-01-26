package com.samebits.beacon.locator.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Log;


import com.samebits.beacon.locator.BuildConfig;
import com.samebits.beacon.locator.R;

import java.util.List;

/**
 *
 * https://gist.github.com/moopat/e9735fa8b5cff69d003353a4feadcdbc
 *
 * http://www.davidgyoungtech.com/2017/08/07/beacon-detection-with-android-8
 */
public class WhiteListUtils {


    private WhiteListUtils() {
    }


    public static void startAutoStartActivity(Context context) {

        for (Intent intent : POWERMANAGER_INTENTS) {

            if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                try {
                    if (isCallable(context, intent)) {
                        showAlertWindow(context, intent);
                        //context.startActivity(intent);
                        break;
                    } else {
                        if (BuildConfig.BUILD_TYPE.contains("release")) {
                            // Crashlytics.log("Intent not callable for whitelisting " + intent.toString());
                        }
                        Log.w(Constants.TAG, "Intent not callable for whitelisting " + intent.toString());
                    }
                } catch (Exception e) {
                    if (BuildConfig.BUILD_TYPE.contains("release")) {
                        //Crashlytics.logException(e);
                    }
                    Log.e(Constants.TAG, "checkOSCompat Error ",  e);
                }
            }
        }
    }


    private static void showAlertWindow(final Context context, final Intent intent) {

        Pair<AlertDialog, Boolean> result = DialogBuilder.createDoNotAskDialog(
                context,
                "user_warn_protected_app",
                context.getString(R.string.action_settings),
                context.getString(R.string.message_need_autostartt_settings),
                R.string.action_settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(intent);
                        //settings.edit().putBoolean(SKIP_WHITELIST_APP, true).apply();
                    }
                });
        if (result.second) {
            result.first.show();
        }

    }

    private static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * List of all know third party power management, autostart activities
     */
    private static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    };


}
