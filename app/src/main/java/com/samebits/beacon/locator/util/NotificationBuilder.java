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

package com.samebits.beacon.locator.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.ContextCompat;

import com.samebits.beacon.locator.R;


/**
 * Created by vitas on 24/10/15.
 */
public class NotificationBuilder {

    private Context mContext;
    private Builder mBuilder;
    private NotificationManager mNotificationManager;


    public NotificationBuilder(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    /**
     * Creation of notification on operations completed
     */
    public NotificationBuilder createNotification(int smallIcon, String title, PendingIntent notifyIntent) {
        mBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(smallIcon).setContentTitle(title)
                .setAutoCancel(true).setColor(ContextCompat.getColor(mContext, R.color.hn_orange));
        if (notifyIntent != null) {
            mBuilder.setContentIntent(notifyIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // setLargeIcon(R.drawable.logo_notification_lollipop);
            //FIXME
            setLargeIcon(R.mipmap.ic_launcher);
        } else {
            setLargeIcon(R.mipmap.ic_launcher);
        }

        return this;
    }


    public Builder getBuilder() {
        return mBuilder;
    }


    public NotificationBuilder setLargeIcon(Bitmap largeIconBitmap) {
        mBuilder.setLargeIcon(largeIconBitmap);
        return this;
    }


    public NotificationBuilder setLargeIcon(int largeIconResource) {
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(mContext.getResources(), largeIconResource);
        return setLargeIcon(largeIconBitmap);
    }


    public NotificationBuilder setRingtone(String ringtone) {
        // Ringtone options
        if (ringtone != null) {
            mBuilder.setSound(Uri.parse(ringtone));
        }
        return this;
    }


    public NotificationBuilder setVibration() {
        return setVibration(null);
    }


    public NotificationBuilder setVibration(long[] pattern) {
        if (pattern == null || pattern.length == 0) {
            pattern = new long[]{500, 500};
        }
        mBuilder.setVibrate(pattern);
        return this;
    }


    public NotificationBuilder setLedActive() {
        mBuilder.setLights(Color.BLUE, 1000, 1000);
        return this;
    }


    public NotificationBuilder setIcon(int icon) {
        mBuilder.setSmallIcon(icon);
        return this;
    }


    public NotificationBuilder setMessage(String message) {
        mBuilder.setContentText(message);
        return this;
    }

    public NotificationBuilder setTicker(String ticker) {
        this.mBuilder.setTicker(ticker);
        return this;
    }

    public NotificationBuilder setIndeterminate() {
        mBuilder.setProgress(0, 0, true);
        return this;
    }


    public NotificationBuilder setOngoing() {
        mBuilder.setOngoing(true);
        return this;
    }


    public NotificationBuilder show() {
        show(0);
        return this;
    }


    public NotificationBuilder show(long id) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Notification mNotification = mBuilder.build();
        if (mNotification.contentIntent == null) {
            // Creates a dummy PendingIntent
            mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(),
                    PendingIntent.FLAG_UPDATE_CURRENT));
        }
        // Builds an anonymous Notification object from the builder, and passes it to the NotificationManager
        mNotificationManager.notify((int) id, mBuilder.build());
        return this;
    }


}
