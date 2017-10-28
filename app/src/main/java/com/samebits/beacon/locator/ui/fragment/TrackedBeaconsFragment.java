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

package com.samebits.beacon.locator.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;

import com.samebits.beacon.locator.BeaconLocatorApp;
import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.data.DataManager;
import com.samebits.beacon.locator.model.ActionBeacon;
import com.samebits.beacon.locator.model.TrackedBeacon;
import com.samebits.beacon.locator.ui.adapter.BeaconAdapter;
import com.samebits.beacon.locator.ui.adapter.TrackedBeaconAdapter;
import com.samebits.beacon.locator.ui.view.ContextMenuRecyclerView;
import com.samebits.beacon.locator.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by vitas on 9/11/15.
 */
public class TrackedBeaconsFragment extends BeaconFragment implements SwipeRefreshLayout.OnRefreshListener, BeaconAdapter.OnBeaconLongClickListener {

    @BindView(R.id.recycler_beacons)
    ContextMenuRecyclerView mListBeacons;
    @BindView(R.id.progress_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.empty_view)
    ViewStub mEmpty;
    EmptyView mEmptyView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private TrackedBeaconAdapter mBeaconsAdapter;
    private DataManager mDataManager;

    public static TrackedBeaconsFragment newInstance() {
        TrackedBeaconsFragment beaconsFragment = new TrackedBeaconsFragment();
        return beaconsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mDataManager = BeaconLocatorApp.from(getActivity()).getComponent().dataManager();
        mBeaconsAdapter = new TrackedBeaconAdapter(this);
        mBeaconsAdapter.setOnBeaconLongClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_tracked_beacons, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        setupToolbar();
        setupRecyclerView();

        loadBeacons();

        return fragmentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.title_fragment_tracked_beacons);
        }
    }

    private void setupRecyclerView() {
        View viewFromEmpty = mEmpty.inflate();
        mEmptyView = new EmptyView(viewFromEmpty);
        mEmptyView.text.setText(getString(R.string.text_empty_list_tracked_beacons));

        mListBeacons.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListBeacons.setHasFixedSize(true);
        mListBeacons.setAdapter(mBeaconsAdapter);

        registerForContextMenu(mListBeacons);

        mProgressBar.setVisibility(View.GONE);

    }

    private void setupSwipe() {

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new UndoSwipableCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT));
        swipeToDismissTouchHelper.attachToRecyclerView(mListBeacons);
    }

    private void emptyListUpdate() {
        if (mBeaconsAdapter.getItemCount() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
            mEmptyView.text.setText(getString(R.string.text_empty_list_tracked_beacons));
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    private void loadBeacons() {
        showLoadingViews();

        getExtras();

        mBeaconsAdapter.insertBeacons(mDataManager.getAllBeacons());

        emptyListUpdate();
        hideLoadingViews();
    }

    private void getExtras() {
        if (getArguments() != null && !getArguments().isEmpty()) {

            TrackedBeacon beacon = getArguments().getParcelable(Constants.ARG_BEACON);
            if (beacon != null) {
                if (!mBeaconsAdapter.isItemExists(beacon.getId())) {
                    if (!mDataManager.isBeaconExists(beacon.getId())) {
                        if (!mDataManager.createBeacon(beacon)) {
                            //TODO error
                        }
                    }
                } else {
                    //TODO make selection of updated
                    if (!mDataManager.updateBeacon(beacon)) {
                       //TODO error
                    }
                }
            }
        }
    }

    public void removeBeacon(String beaconId) {
        if (mDataManager.deleteBeacon(beaconId, true)) {
            mBeaconsAdapter.removeBeaconById(beaconId);
        } else {
            //TODO error
        }
        emptyListUpdate();
    }

    public void removeBeaconAction(String beaconId, int id) {
        if (mDataManager.deleteBeaconAction(id)) {
            mBeaconsAdapter.removeBeaconAction(beaconId, id);
        } else {
            //TODO error
        }
    }

    public void newBeaconAction(String beaconId) {
        String defName = getString(R.string.pref_bd_default_name);
        int actionCount = mBeaconsAdapter.getActionCount(beaconId);

        if (actionCount > 0) {
            defName += " (" + actionCount + ")";
        }

        ActionBeacon newAction = new ActionBeacon(beaconId, defName);
        if (mDataManager.createBeaconAction(newAction)) {
            mBeaconsAdapter.addBeaconAction(newAction);
        } else {
            //FIXME error
        }
    }

    public void enableBeaconAction(String beaconId, int id, boolean enable) {
        if (mDataManager.enableBeaconAction(id, enable)) {
            mBeaconsAdapter.enableAction(beaconId, id, enable);
        } else {
            //TODO error
        }
    }

    @Override
    public void onRefresh() {
        loadBeacons();
    }

    private void hideLoadingViews() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showLoadingViews() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBeaconLongClick(int position) {
        mListBeacons.openContextMenu(position);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQ_UPDATED_ACTION_BEACON) {
            if (data != null && data.hasExtra(Constants.ARG_ACTION_BEACON)) {
                ActionBeacon actionBeacon = data.getParcelableExtra(Constants.ARG_ACTION_BEACON);
                if (actionBeacon != null) {
                    //TODO check if isDirty, now we store always even no changes
                    if (mDataManager.updateBeaconAction(actionBeacon)) {
                        mBeaconsAdapter.updateBeaconAction(actionBeacon);
                    } else {
                        //TODO error
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo
            menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_tracked_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        ContextMenuRecyclerView.RecyclerContextMenuInfo info = (ContextMenuRecyclerView
                .RecyclerContextMenuInfo) item
                .getMenuInfo();

        switch (item.getItemId()) {
            case R.id.action_add:
                newBeaconAction(mBeaconsAdapter.getBeacon(info.position).getId());
                return true;
            case R.id.action_delete:
                removeBeacon(mBeaconsAdapter.getBeacon(info.position).getId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class UndoSwipableCallback extends ItemTouchHelper.SimpleCallback {

        public UndoSwipableCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mBeaconsAdapter.removeBeacon(viewHolder.getAdapterPosition());
        }

    }

}