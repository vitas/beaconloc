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

package com.samebits.beacon.locator.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.databinding.ItemTrackedBeaconBinding;
import com.samebits.beacon.locator.model.TrackedBeacon;
import com.samebits.beacon.locator.ui.view.RemovableViewHolder;
import com.samebits.beacon.locator.util.Constants;
import com.samebits.beacon.locator.viewModel.TrackedBeaconViewModel;

/**
 * Created by vitas on 09/12/2015.
 */
public class TrackedBeaconAdapter extends BeaconAdapter<TrackedBeaconAdapter.BindingHolder> {


    public TrackedBeaconAdapter(Context context) {
        mContext = context;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTrackedBeaconBinding beaconBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_tracked_beacon,
                parent,
                false);
        setupSwipe(beaconBinding);
        return new BindingHolder(beaconBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemTrackedBeaconBinding beaconBinding = holder.binding;
        beaconBinding.setViewModel(new TrackedBeaconViewModel(mContext, (TrackedBeacon) getItem(position)));
    }


    private void setupSwipe(ItemTrackedBeaconBinding beaconBinding) {

        final SwipeDismissBehavior<CardView> swipe = new SwipeDismissBehavior();
        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        swipe.setListener(new SwipeDismissBehavior.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                Log.d(Constants.TAG, "Swipe +");
            }

            @Override
            public void onDragStateChanged(int state) {
            }
        });

        //CoordinatorLayout.LayoutParams coordinatorParams = (CoordinatorLayout.LayoutParams) beaconBinding.cardView.getLayoutParams();
        //coordinatorParams.setBehavior(swipe);
    }

    public static class BindingHolder extends RemovableViewHolder {
        private ItemTrackedBeaconBinding binding;

        public BindingHolder(ItemTrackedBeaconBinding binding) {
            super(binding.cardView);
            this.binding = binding;

        }
    }

}
