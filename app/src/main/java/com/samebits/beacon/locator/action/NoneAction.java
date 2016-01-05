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

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.NotificationAction;
import com.samebits.beacon.locator.util.Constants;

/**
 * Created by vitas on 03/01/16.
 */
public class NoneAction extends Action {

    protected final String param;
    protected final NotificationAction notification;

    public NoneAction(String param, NotificationAction notification) {
        this.param = param;
        this.notification = notification;
    }


    @Override
    public String execute(Context context) {
        if (isParamRequired() && isParamEmpty()) {
            return context.getString(R.string.action_action_param_is_required);
        }
        //empty
        sendAlarm(context);
        return null;
    }

    @Override
    public boolean isParamRequired() {
        return false;
    }

    protected void sendAlarm(Context context) {
        if (isNotificationRequired()) {
            Intent newIntent = new Intent(Constants.ALARM_NOTIFICATION_SHOW);
            newIntent.putExtra("NOTIFICATION", notification);
            context.sendBroadcast(newIntent);
        }
    }

    protected boolean isParamEmpty() {
        return param == null || param.isEmpty();
    }

    protected boolean isNotificationRequired() {
        return notification != null && notification.isEnabled();
    }

    @Override
    public String toString() {
        return "NoneAction, action: " + param;
    }
}