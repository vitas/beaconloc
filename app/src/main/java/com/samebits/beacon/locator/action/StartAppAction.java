/*
 *
 *  Copyright (c) 2016 SameBits UG. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.samebits.beacon.locator.action;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.NotificationAction;

import java.util.List;

/**
 * Created by vitas on 03/01/16.
 */
public class StartAppAction extends NoneAction {


    public StartAppAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        try {
            if (!launchApp(context, param)) {
                openApp(context, param);
            }
        } catch (Exception e) {
            return context.getString(R.string.action_start_application_error);
        }
        return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "StartAppAction, app_package: " + param;
    }

    private boolean launchApp(Context context, String packageName) {

        final PackageManager manager = context.getPackageManager();
        final Intent appLauncherIntent = new Intent(Intent.ACTION_MAIN);
        appLauncherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = manager.queryIntentActivities(appLauncherIntent, 0);
        if ((null != resolveInfos) && (!resolveInfos.isEmpty())) {
            for (ResolveInfo rInfo : resolveInfos) {
                String className = rInfo.activityInfo.name.trim();
                String targetPackageName = rInfo.activityInfo.packageName.trim();
                if (packageName.trim().equals(targetPackageName)) {
                    Intent intent = new Intent();
                    intent.setClassName(targetPackageName, className);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            return false;
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;
    }
}
