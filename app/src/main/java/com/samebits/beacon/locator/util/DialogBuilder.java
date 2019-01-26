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

package com.samebits.beacon.locator.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.samebits.beacon.locator.R;

import static com.samebits.beacon.locator.util.PreferencesUtil.getSharedPreferences;


/**
 * Created by vitas on 18/10/15.
 */
public final class DialogBuilder {

    private DialogBuilder() {
    }

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Pair<AlertDialog, Boolean> createDoNotAskDialog(final Context context,
                                                                  final String prefix,
                                                                  String title,
                                                                  String message,
                                                                  int buttonId,
                                                                  final DialogInterface.OnClickListener clickListener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message);

        LayoutInflater adbInflater = LayoutInflater.from(context);

        final View dialogView = adbInflater.inflate(R.layout.popup_do_not_ask, null);
        alertDialog.setView(dialogView);

        final CheckBox dontShowAgain = dialogView.findViewById(R.id.skip);

        alertDialog.setPositiveButton(buttonId, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";
                if (dontShowAgain.isChecked())
                    checkBoxResult = "checked";
                SharedPreferences settings = getSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(prefix+"_skipMessage", checkBoxResult);
                // Commit the edits!
                editor.apply();

                if (clickListener != null) {
                    clickListener.onClick(dialog, which);
                }
            }
        });

        SharedPreferences settings = getSharedPreferences(context);
        String skipMessage = settings.getString(prefix+"_skipMessage", "NOT checked");

        return new Pair<>(alertDialog.create(), !skipMessage.equals("checked"));
    }

}
