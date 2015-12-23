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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.databinding.ItemTrackedBeaconBinding;
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.model.TrackedBeacon;
import com.samebits.beacon.locator.util.BeaconUtil;
import com.samebits.beacon.locator.util.Constants;
import com.samebits.beacon.locator.viewModel.TrackedBeaconViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vitas on 09/12/2015.
 */
public class TrackedBeaconAdapter extends RecyclerView.Adapter<TrackedBeaconAdapter.BindingHolder> {

    private Map<String, IManagedBeacon> mBeacons = new LinkedHashMap();
    private Context mContext;

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
        return new BindingHolder(beaconBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemTrackedBeaconBinding beaconBinding = holder.binding;
        beaconBinding.setViewModel(new TrackedBeaconViewModel(mContext, (TrackedBeacon) getItem(position)));
    }

    @Override
    public int getItemCount() {
        return mBeacons.size();
    }

    public Object getItem(int idx) {
        int i = 0;
        Iterator localIterator = this.mBeacons.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            if (i == idx) {
                return localEntry.getValue();
            }
            i += 1;
        }
        return null;
    }

    public long getItemId(int idx) {
        return 0;
    }

    public void clearAll() {
        this.mBeacons.clear();
        notifyDataSetChanged();
    }

    public void insertBeacon(IManagedBeacon beacon) {
        this.mBeacons.put(beacon.getId(), beacon);
        notifyDataSetChanged();
    }

    public void insertBeacons(List<IManagedBeacon> beacons) {
        for (IManagedBeacon beacon :
                beacons) {
            this.mBeacons.put(beacon.getId(), beacon);
        }
        notifyDataSetChanged();
    }

    public void sort(final int sortMode) {
        this.mBeacons = BeaconUtil.sortBecons(mBeacons, sortMode);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemTrackedBeaconBinding binding;

        public BindingHolder(ItemTrackedBeaconBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }

}
