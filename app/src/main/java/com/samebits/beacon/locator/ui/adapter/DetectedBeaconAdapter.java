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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.databinding.ItemDetectedBeaconBinding;
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.ui.fragment.BeaconFragment;
import com.samebits.beacon.locator.viewModel.DetectedBeaconViewModel;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by vitas on 09/12/2015.
 */
public class DetectedBeaconAdapter extends BeaconAdapter<DetectedBeaconAdapter.BindingHolder> {

    public DetectedBeaconAdapter(BeaconFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDetectedBeaconBinding beaconBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_detected_beacon,
                parent,
                false);
        return new BindingHolder(beaconBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        ItemDetectedBeaconBinding beaconBinding = holder.binding;
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onBeaconLongClickListener != null) {
                    onBeaconLongClickListener.onBeaconLongClick(position);
                }
                return false;
            }
        });
        beaconBinding.setViewModel(new DetectedBeaconViewModel(mFragment, (DetectedBeacon) getItem(position)));
    }


    public void insertBeacons(Collection<Beacon> beacons) {
        Iterator<Beacon> iterator = beacons.iterator();
        while (iterator.hasNext()) {
            DetectedBeacon dBeacon = new DetectedBeacon(iterator.next());
            dBeacon.setTimeLastSeen(System.currentTimeMillis());
            this.mBeacons.put(dBeacon.getId(), dBeacon);
        }
        notifyDataSetChanged();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemDetectedBeaconBinding binding;

        public BindingHolder(ItemDetectedBeaconBinding binding) {
            super(binding.contentView);
            this.binding = binding;
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            binding.contentView.setOnLongClickListener(listener);
        }
    }

}
