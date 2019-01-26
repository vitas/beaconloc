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
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.samebits.beacon.locator.R;


/**
 * Created by vitas on 24/10/15.
 */
public class NotificationBuilder {

    private Context mContext;
    private Builder mBuilder;
    private long[] mVibrate_pattern = new long[]{500, 500};
    private NotificationManagerCompat mNotificationManager;
    NotificationChannel mNotificationChannel;
    public static String NOTIFICATION_CHANNEL_ID = "beacon_locator_channel_01";
    String NOTIFICATION_CHANNEL_SERVICE_ID = "beacon_locator_channel_service";


    public NotificationBuilder(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    /**
     * Creation of notification on operations completed
     */
    public NotificationBuilder createNotification(String title, String ringtone, boolean vibrate, PendingIntent notifyIntent) {

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mNotificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    title, NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            mNotificationChannel.setDescription("beacon location notification channel");
            mNotificationChannel.enableLights(true);

            if (vibrate) {
                mNotificationChannel.setVibrationPattern(mVibrate_pattern);
                mNotificationChannel.enableVibration(true);
            } else {
                mNotificationChannel.enableVibration(false);
            }

            mNotificationChannel.setLightColor(Color.YELLOW);
            mNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationChannel.setSound(null, null);

           /* if (ringtone != null) {
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                mNotificationChannel.setSound(Uri.parse(ringtone), att);
            } else {
                mNotificationChannel.setSound(null, null);
            }
            */
            notificationManager.createNotificationChannel(mNotificationChannel);
        }

        mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);

        mBuilder.setAutoCancel(true)
                .setDefaults(vibrate?(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE):Notification.DEFAULT_LIGHTS)
                .setSmallIcon(getNotificationSmallIcon())
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(mContext, R.color.hn_orange));

        setRingtone(ringtone);

        if (notifyIntent != null) {
            mBuilder.setContentIntent(notifyIntent);
        }

        setLargeIcon(getNotificationLargeIcon());

        return this;

    }

    public NotificationBuilder createNotificationService(String title, PendingIntent notifyIntent) {

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_SERVICE_ID,
                    mContext.getString(R.string.text_scan_foreground_service), NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            mNotificationChannel.setDescription(mContext.getString(R.string.pref_title_background_scan));
            mNotificationChannel.enableLights(false);
            mNotificationChannel.enableVibration(false);
            mNotificationChannel.setSound(null, null);
            mNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

            notificationManager.createNotificationChannel(mNotificationChannel);
        }

        mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_SERVICE_ID);

        mBuilder.setAutoCancel(true)
                .setDefaults(0)
                .setSmallIcon(getNotificationSmallIcon())
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(mContext, R.color.hn_orange));

        if (notifyIntent != null) {
            mBuilder.setContentIntent(notifyIntent);
        }

        setLargeIcon(getNotificationLargeIcon());

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
        if (ringtone != null && ringtone.length() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                //mNotificationChannel.setSound(Uri.parse(ringtone), att);

                // if not a silent sound, play
                if (!ringtone.equals(mContext.getString(R.string.pref_bn_none_notification_action_ringtone))) {
                    Ringtone r = RingtoneManager.getRingtone(mContext.getApplicationContext(), Uri.parse(ringtone));
                    try {
                        r.play();
                    } catch (Exception e) {

                    }
                }
            } else {
                mBuilder.setSound(Uri.parse(ringtone), AudioManager.STREAM_NOTIFICATION);
            }
        }

        return this;
    }

    public NotificationBuilder setVibration() {
        return setVibration(null);
    }


    public NotificationBuilder setVibration(long[] pattern) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (pattern == null || pattern.length == 0) {
                mBuilder.setVibrate(mVibrate_pattern);
            } else {
                mBuilder.setVibrate(pattern);
            }
        }

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
        mNotificationManager = NotificationManagerCompat.from(mContext);

        Notification mNotification = mBuilder.build();
        if (mNotification.contentIntent == null) {
            // Creates a dummy PendingIntent
            mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(),
                    PendingIntent.FLAG_UPDATE_CURRENT));
        }

        mNotificationManager.notify((int) id, mBuilder.build());
        return this;
    }

    public static int getNotificationSmallIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;

    }

    public static int getNotificationLargeIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        //TODO
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;

    }

}
