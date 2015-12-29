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

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

import android.view.View;

import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.ui.fragment.BaseFragment;

/**
 * Created by vitas on 19/10/15.
 */
public class ActionBeaconViewModel extends BaseObservable {

    public static final String ARG_BEACON = "ARG_BEACON";
    public static final String ARG_MODE = "ARG_MODE";
    protected Context mContext;
    protected ActionBeacon mActionBeacon;
    protected BaseFragment mFragment;

    public ActionBeaconViewModel(@NonNull BaseFragment fragment, @NonNull ActionBeacon actionBeacon) {
        this.mActionBeacon = actionBeacon;
        this.mFragment = fragment;
        this.mContext = fragment.getActivity();
    }

    public String getName() { return mActionBeacon.getName();}

    public View.OnClickListener onClickBeacon() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActionDetailsActivity();
            }
        };
    }

    protected void launchActionDetailsActivity() {
    }
}
