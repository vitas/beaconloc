/*
 *
 *  Copyright (c) 2015 SameBits UG. All rights reserved.
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

package com.samebits.beacon.locator.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.ui.activity.MainNavigationActivity;
import com.samebits.beacon.locator.util.NotificationBuilder;


public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO
        // get event, action params
        //createNotification(context, "Beacon Event", "What to do", "Beacons are ready!");
    }

    private void createNotification(Context context, String title, String msgText, String msgAlert) {

        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainNavigationActivity.class), 0);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context);
        notificationBuilder.createNotification(R.mipmap.ic_launcher, title, notificationIntent);

        notificationBuilder.setMessage(msgText);
        notificationBuilder.setTicker(msgAlert);

       // builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
       // builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
       // builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        notificationBuilder.show(1);

    }
}





