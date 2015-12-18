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
import com.samebits.beacon.locator.databinding.ItemDetectedBeaconBinding;
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.viewModel.DetectedBeaconViewModel;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by vitas on 09/12/2015.
 */
public class DetectedBeaconAdapter extends RecyclerView.Adapter<DetectedBeaconAdapter.BindingHolder> {

    private Map<String, DetectedBeacon> mBeacons = new LinkedHashMap();
    private Context mContext;

    public DetectedBeaconAdapter(Context context) {
        mContext = context;
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
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemDetectedBeaconBinding beaconBinding = holder.binding;
        beaconBinding.setViewModel(new DetectedBeaconViewModel(mContext, (DetectedBeacon) getItem(position)));
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
        DetectedBeacon selectedBeacon = (DetectedBeacon) this.getItem(idx);
        if (selectedBeacon.isEddystone()) {
            return selectedBeacon.getServiceUuid();
        }
        return UUID.fromString(selectedBeacon.getId1().toString()).getMostSignificantBits();
    }

    public void clearAll() {
        this.mBeacons.clear();
        notifyDataSetChanged();
    }

    public void insertBeacon(Beacon beacon) {
        DetectedBeacon dBeacon = new DetectedBeacon(beacon);
        dBeacon.setTimeLastSeen(System.currentTimeMillis());
        this.mBeacons.put(dBeacon.getId(), dBeacon);
        notifyDataSetChanged();
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

    public void sort(final int sortMode) {
        Object localObject = new ArrayList(this.mBeacons.entrySet());
        Collections.sort((List) localObject, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                return compare((Map.Entry<String, DetectedBeacon>) (lhs), (Map.Entry<String, DetectedBeacon>) (rhs));
            }

            private int compare(Map.Entry<String, DetectedBeacon> obj1, Map.Entry<String, DetectedBeacon> obj2) {
                if (sortMode == 2) {
                    int i = obj1.getValue().getId1().toString().compareTo(obj2.getValue().getId1().toString());
                    if (i != 0) {
                        return i;
                    }
                    i = obj1.getValue().getId2().toString().compareTo(obj2.getValue().getId2().toString());
                    if (i != 0) {
                        return i;
                    }
                    if ((!obj1.getValue().isEddystone()) && (!obj2.getValue().isEddystone())) {
                        return obj1.getValue().getId3().toString().compareTo(obj2.getValue().getId3().toString());
                    }
                    return 0;
                }
                double d1 = obj1.getValue().getDistance();
                double d2 = obj2.getValue().getDistance();
                if (d1 == d2) {
                    return 0;
                }
                if (sortMode == 0) {
                    if (d1 < d2) {
                        return -1;
                    }
                    return 1;
                }
                if (sortMode == 1) {
                    if (d1 < d2) {
                        return 1;
                    }
                    return -1;
                }
                return -1;
            }
        });
        LinkedHashMap localLinkedHashMap = new LinkedHashMap();
        localObject = ((List) localObject).iterator();
        while (((Iterator) localObject).hasNext()) {
            Map.Entry localEntry = (Map.Entry) ((Iterator) localObject).next();
            localLinkedHashMap.put(localEntry.getKey(), localEntry.getValue());
        }
        this.mBeacons = localLinkedHashMap;
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemDetectedBeaconBinding binding;

        public BindingHolder(ItemDetectedBeaconBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }

}
