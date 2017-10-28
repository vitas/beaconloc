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

import android.databinding.DataBindingUtil;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.databinding.ItemTrackedBeaconBinding;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.model.TrackedBeacon;
import com.samebits.beacon.locator.ui.fragment.BeaconFragment;
import com.samebits.beacon.locator.ui.fragment.TrackedBeaconsFragment;
import com.samebits.beacon.locator.ui.view.WrapLinearLayoutManager;
import com.samebits.beacon.locator.util.Constants;
import com.samebits.beacon.locator.viewModel.TrackedBeaconViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vitas on 09/12/2015.
 */
public class TrackedBeaconAdapter extends BeaconAdapter<TrackedBeaconAdapter.BindingHolder> {

    private Map<String, ActionBeaconAdapter> mActionAdapters = new HashMap<>();

    public TrackedBeaconAdapter(BeaconFragment fragment) {
        mFragment = fragment;
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
    public void onBindViewHolder(BindingHolder holder, final int position) {
        ItemTrackedBeaconBinding beaconBinding = holder.binding;

        ActionBeaconAdapter adapter = new ActionBeaconAdapter((TrackedBeaconsFragment) mFragment);
        beaconBinding.recyclerActions.setLayoutManager(new WrapLinearLayoutManager(mFragment.getActivity()));
        beaconBinding.recyclerActions.setAdapter(adapter);

        TrackedBeacon beacon = (TrackedBeacon) getItem(position);
        adapter.setItems(beacon.getActions());

        mActionAdapters.put(beacon.getId(), adapter);

        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onBeaconLongClickListener != null) {
                    onBeaconLongClickListener.onBeaconLongClick(position);
                }
                return false;
            }
        });

        beaconBinding.setViewModel(new TrackedBeaconViewModel(mFragment, beacon));
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

    }

    public int getActionCount(String beaconId) {
        ActionBeaconAdapter adapter = mActionAdapters.get(beaconId);
        if (adapter != null) {
            return adapter.getItemCount();
        }
        return 0;
    }

    public void removeBeaconAction(String beaconId, int id) {
        ActionBeaconAdapter adapter = mActionAdapters.get(beaconId);
        if (adapter != null) {
            adapter.removeItemById(id);
        }
    }

    public void addBeaconAction(ActionBeacon newAction) {
        ActionBeaconAdapter adapter = mActionAdapters.get(newAction.getBeaconId());
        if (adapter != null) {
            adapter.addItem(newAction);
        }
    }

    public void updateBeaconAction(ActionBeacon action) {
        ActionBeaconAdapter adapter = mActionAdapters.get(action.getBeaconId());
        if (adapter != null) {
            adapter.addItem(action);
        }
    }

    public IManagedBeacon getBeacon(int position) {
        return (IManagedBeacon) getItem(position);
    }

    public void enableAction(String beaconId, int id, boolean enable) {
        ActionBeaconAdapter adapter = mActionAdapters.get(beaconId);
        if (adapter != null) {
            ActionBeacon action = adapter.getItemById(id);
            if (action != null) {
                action.setIsEnabled(enable);
                adapter.addItem(action);
            }
        }
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemTrackedBeaconBinding binding;

        public BindingHolder(ItemTrackedBeaconBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            binding.cardView.setOnLongClickListener(listener);
        }
    }

}
