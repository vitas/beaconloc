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
import android.view.ViewGroup;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.databinding.ItemActionBeaconBinding;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.ui.fragment.TrackedBeaconsFragment;
import com.samebits.beacon.locator.viewModel.ActionBeaconViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitas on 19/10/15.
 */
public class ActionBeaconAdapter extends RecyclerView.Adapter<ActionBeaconAdapter.BindingHolder> {
    private List<ActionBeacon> mItemsList;
    private TrackedBeaconsFragment mFragment;

    public ActionBeaconAdapter(TrackedBeaconsFragment fragment) {
        mFragment = fragment;
        mItemsList = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemActionBeaconBinding beaconBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_action_beacon,
                parent,
                false);
        return new BindingHolder(beaconBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemActionBeaconBinding itemBinding = holder.binding;
        itemBinding.setViewModel(new ActionBeaconViewModel(mFragment, mItemsList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public void setItems(List<ActionBeacon> itemsList) {
        this.mItemsList = itemsList;
        notifyDataSetChanged();
    }

    public void addItem(ActionBeacon itemsList) {
        if (!this.mItemsList.contains(itemsList)) {
            this.mItemsList.add(itemsList);
            notifyItemInserted(this.mItemsList.size() - 1);
        } else {
            this.mItemsList.set(mItemsList.indexOf(itemsList), itemsList);
            notifyItemChanged(this.mItemsList.indexOf(itemsList));
        }
    }

    public void removeItemById(int id) {
        for (ActionBeacon action : mItemsList) {
            if (action.getId() == id) {
                mItemsList.remove(action);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public ActionBeacon getItemById(int id) {
        for (ActionBeacon action : mItemsList) {
            if (action.getId() == id) {
                return action;
            }
        }
        return null;
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemActionBeaconBinding binding;

        public BindingHolder(ItemActionBeaconBinding binding) {
            super(binding.contentView);
            this.binding = binding;
        }
    }

}