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
import android.media.AudioManager;

import com.samebits.beacon.locator.model.NotificationAction;
import com.samebits.beacon.locator.util.PreferencesUtil;

/**
 * Created by vitas on 03/01/16.
 */
public class SilentOffAction extends NoneAction {


    public SilentOffAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        int old_mode = PreferencesUtil.getSilentModeProfile(context);
        if (ringerMode != AudioManager.RINGER_MODE_VIBRATE) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        return super.execute(context);
    }

    @Override
    public String toString() {
        return "SilentOffAction, param: " + param;
    }
}
