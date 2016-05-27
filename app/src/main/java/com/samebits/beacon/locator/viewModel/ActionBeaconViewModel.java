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

package com.samebits.beacon.locator.viewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.view.View;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.ui.activity.BeaconActionActivity;
import com.samebits.beacon.locator.ui.fragment.TrackedBeaconsFragment;
import com.samebits.beacon.locator.util.BeaconUtil;
import com.samebits.beacon.locator.util.Constants;

/**
 * Created by vitas on 19/10/15.
 */
public class ActionBeaconViewModel extends BaseObservable {

    protected ActionBeacon mActionBeacon;
    protected TrackedBeaconsFragment mFragment;

    public ActionBeaconViewModel(@NonNull TrackedBeaconsFragment fragment, @NonNull ActionBeacon actionBeacon) {
        this.mActionBeacon = actionBeacon;
        this.mFragment = fragment;
    }

    public String getName() {
        return mActionBeacon.getName();
    }

    public String getEventName() {
        return mFragment.getString(BeaconUtil.getEventTypeResourceId(mActionBeacon.getEventType()));
    }

    public String getActionNames() {
        switch (mActionBeacon.getActionType()) {
            case ACTION_WEB:
                return mFragment.getString(R.string.mv_action_type_open_web);
            case ACTION_URL:
                return mFragment.getString(R.string.mv_action_type_open_url);
            case ACTION_INTENT_ACTION:
                return mFragment.getString(R.string.mv_action_type_broadcast_intent);
            case ACTION_START_APP:
                return mFragment.getString(R.string.mv_action_type_start_app);
            case ACTION_GET_LOCATION:
                return mFragment.getString(R.string.mv_action_type_get_location);
            case ACTION_SET_SILENT_ON:
                return mFragment.getString(R.string.mv_action_type_set_silent_on);
            case ACTION_SET_SILENT_OFF:
                return mFragment.getString(R.string.mv_action_type_set_silent_off);
            case ACTION_TASKER:
                return mFragment.getString(R.string.mv_action_type_tasker);
        }
        return mFragment.getString(R.string.mv_action_type_none);
    }

    public boolean isEnabled() {
        return mActionBeacon.isEnabled();
    }

    public String getEnableStatus() {
        return mActionBeacon.isEnabled() ? mFragment.getString(R.string.mv_action_status_enable) : mFragment.getString(R.string.mv_action_status_disabled);
    }

    public View.OnClickListener onClickEdit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActionDetailsActivity();
            }
        };
    }

    protected void launchActionDetailsActivity() {
        Intent intent = BeaconActionActivity.getStartIntent(mFragment.getActivity());
        intent.putExtra(Constants.ARG_ACTION_BEACON, mActionBeacon);
        mFragment.startActivityForResult(intent, Constants.REQ_UPDATED_ACTION_BEACON);
    }


    public View.OnClickListener onClickDelete() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.removeBeaconAction(mActionBeacon.getBeaconId(), mActionBeacon.getId());
            }
        };
    }

    public View.OnClickListener onClickEnable() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.enableBeaconAction(mActionBeacon.getBeaconId(), mActionBeacon.getId(), !mActionBeacon.isEnabled());
            }
        };
    }
}
