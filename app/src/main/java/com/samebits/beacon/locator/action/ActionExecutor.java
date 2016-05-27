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
import android.util.Log;

import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.NotificationAction;
import com.samebits.beacon.locator.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitas on 03/01/16.
 */
public class ActionExecutor {

    private final Context mContext;
    private List<IAction> mHistory = new ArrayList<>();
    private List<IAction> mFailed = new ArrayList<>();

    public ActionExecutor(Context context) {
        this.mContext = context;
    }

    public static IAction actionBuilder(ActionBeacon.ActionType type, String param, NotificationAction notification) {
        switch (type) {
            case ACTION_NONE:
                return new NoneAction(param, notification);
            case ACTION_WEB:
                return new WebAction(param, notification);
            case ACTION_URL:
                return new UrlAction(param, notification);
            case ACTION_INTENT_ACTION:
                return new IntentAction(param, notification);
            case ACTION_START_APP:
                return new StartAppAction(param, notification);
            case ACTION_GET_LOCATION:
                return new LocationAction(param, notification);
            case ACTION_SET_SILENT_ON:
                return new SilentOnAction(param, notification);
            case ACTION_SET_SILENT_OFF:
                return new SilentOffAction(param, notification);
            case ACTION_TASKER:
                return new TaskerAction(param, notification);
        }
        return null;
    }

    public String storeAndExecute(IAction action) {
        this.mHistory.add(action); // optional
        try {
            return action.execute(mContext);
        } catch (Exception e) {
            mFailed.add(action);
            Log.d(Constants.TAG, "Error executing action: " + action, e);
        }
        return null;
    }
}
