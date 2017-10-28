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

import android.support.v7.widget.RecyclerView;

import com.samebits.beacon.locator.model.IManagedBeacon;
import com.samebits.beacon.locator.ui.fragment.BeaconFragment;
import com.samebits.beacon.locator.util.BeaconUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vitas on 09/12/2015.
 */

public abstract class BeaconAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Map<String, IManagedBeacon> mBeacons = new LinkedHashMap();
    protected BeaconFragment mFragment;
    protected OnBeaconLongClickListener onBeaconLongClickListener;


    public void insertBeacon(IManagedBeacon beacon) {
        this.mBeacons.put(beacon.getId(), beacon);
        notifyDataSetChanged();
    }

    public void insertBeacons(List<? extends IManagedBeacon> beacons) {
        for (IManagedBeacon beacon :
                beacons) {
            this.mBeacons.put(beacon.getId(), beacon);
        }
        notifyDataSetChanged();
    }

    public void sort(final int sortMode) {
        this.mBeacons = BeaconUtil.sortBecons(mBeacons, sortMode);
    }

    public void removeBeacon(int position) {
        IManagedBeacon beacon = (IManagedBeacon) getItem(position);
        if (beacon != null) {
            this.mBeacons.remove(beacon.getId());
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mBeacons.size();
    }

    public Object getItem(int idx) {
        int i = 0;
        for (Map.Entry<String, IManagedBeacon> entry : this.mBeacons.entrySet()) {
            if (i == idx) {
                return entry.getValue();
            }
            i += 1;
        }
        return null;
    }

    public void removeAll() {
        this.mBeacons.clear();
        notifyDataSetChanged();
    }

    public boolean isItemExists(String id) {
        return this.mBeacons.containsKey(id);
    }

    public void removeBeaconById(String beaconId) {
        this.mBeacons.remove(beaconId);
        notifyDataSetChanged();
    }

    public void setOnBeaconLongClickListener(OnBeaconLongClickListener onBeaconLongClickListener) {
        this.onBeaconLongClickListener = onBeaconLongClickListener;
    }

    public interface OnBeaconLongClickListener {
        void onBeaconLongClick(int position);
    }
}
