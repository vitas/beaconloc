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
import com.samebits.beacon.locator.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitas on 03/01/16.
 */
public class ActionExecutor {

    private final Context mContext;
    private List<IAction> mHistory = new ArrayList<>();

    public ActionExecutor(Context context) {
        this.mContext = context;
    }

    public void storeAndExecute(IAction action) {
        this.mHistory.add(action); // optional
        try {
            action.execute(mContext);
        } catch (Exception e) {
            Log.d(Constants.TAG, "Error executing action: " + action, e);
        }
    }

    public static IAction actionBuilder(ActionBeacon.ActionType type, String param) {
        switch (type) {
            case ACTION_URL:
                return new UrlAction(param);
            case ACTION_INTENT_ACTION:
                return new IntentAction(param);
        }
        return null;
    }
}
