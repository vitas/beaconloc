/*
 *
 *  Copyright (c) 2016 SameBits UG. All rights reserved.
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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.util.Constants;

import net.dinglisch.android.tasker.TaskerIntent;

/**
 *
 *
 */
public class TaskerDownloadFragment extends DialogFragment {
    private DownloadCanceledListener mListener;

    public static TaskerDownloadFragment getInstance() {
        final TaskerDownloadFragment fragment = new TaskerDownloadFragment();
        return fragment;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (DownloadCanceledListener) activity;
        } catch (final ClassCastException e) {
            Log.e(Constants.TAG, "Parent activity must implement DownloadCancelListener interface", e);
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.action_tasker_download)
                .setIcon(R.drawable.ic_action_tasker)
                .setMessage(R.string.action_tasker_message).setCancelable(false)
                .setPositiveButton(R.string.tasker_download, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Go to Google Play
                        final Intent intent = TaskerIntent.getTaskerInstallIntent(true);
                        startActivity(intent);
                    }
                }).setNeutralButton(R.string.tasker_website, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open link in the browser
                        final Intent intent = TaskerIntent.getTaskerInstallIntent(false);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mListener.onTaskerNotInstalled();
                    }
                }).create();
    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        mListener.onTaskerNotInstalled();
    }

    public interface DownloadCanceledListener {
        public void onTaskerNotInstalled();
    }
}
