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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.NotificationAction;
import com.samebits.beacon.locator.util.Constants;

import net.dinglisch.android.tasker.TaskerIntent;

/**
 * Created by vitas on 03/01/16.
 */
public class TaskerAction extends NoneAction {


    public TaskerAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {

        switch (TaskerIntent.testStatus(context)) {
            case OK:
                final TaskerIntent newIntent = new TaskerIntent(param);
                final BroadcastReceiver broadcastRec = new BroadcastReceiver() {
                    @Override
                    public void onReceive(final Context context, final Intent recIntent) {
                        if (recIntent.getBooleanExtra(TaskerIntent.EXTRA_SUCCESS_FLAG, false)) {

                        }
                        context.unregisterReceiver(this);
                    }
                };
                context.registerReceiver(broadcastRec, newIntent.getCompletionFilter());
                context.sendBroadcast(newIntent);
                break;
            case NotEnabled:
                Log.w(Constants.TAG, "Tasker is not enabled");
                return context.getString(R.string.tasker_disabled);
            case AccessBlocked:
                Log.w(Constants.TAG, "Taskers access is blocked");
                return context.getString(R.string.tasker_external_access_denided);
            case NotInstalled:
                Log.w(Constants.TAG, "Tasker is not installed");
                return context.getString(R.string.tasker_not_installed);
            default:
                break;
        }
       return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "TaskerAction, param(s): " + param;
    }
}
