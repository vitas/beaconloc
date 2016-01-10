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

package com.samebits.beacon.locator.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.samebits.beacon.locator.BeaconLocatorApp;
import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.util.Constants;
import com.samebits.beacon.locator.util.PreferencesUtil;


/**
 * Created by vitas on 19/10/15.
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public static int glCount = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
                return true;
            case R.id.action_settings:
                launchSettingsActivity();
                return true;
            case R.id.action_view_on_github:
                launchGitHubPage();
                return true;
            case R.id.action_help:
                launchHelpPage();
                return true;
            case R.id.action_donate:
                launchDonatePage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void launchGitHubPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/vitas/beaconloc"));
        startActivity(browserIntent);
    }

    private void launchHelpPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/vitas/beaconloc/wiki/Help"));
        startActivity(browserIntent);
    }

    private void launchDonatePage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/SameBits"));
        startActivity(browserIntent);
    }

    protected void launchSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, Constants.REQ_GLOBAL_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQ_GLOBAL_SETTING) {
            //TODO settings
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    protected Fragment checkFragmentInstance(int id, Object instanceClass) {

        Fragment fragment = getFragmentInstance(id);
        if (fragment != null && instanceClass.equals(fragment.getClass())) {
            return fragment;
        }
        return null;
    }

    protected Fragment getFragmentInstance(int id) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment fragment = fragmentManager.findFragmentById(id);
            if (fragment != null) {
                return fragment;
            }
        }
        return null;
    }


    @Override
    protected void onStart() {
        super.onStart();
        glCount++;
        if (glCount == 1) {
            BeaconLocatorApp.from(this).enableBackgroundScan(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        glCount--;
        if (glCount <= 0) {
            BeaconLocatorApp.from(this).enableBackgroundScan(PreferencesUtil.isBackgroundScan(this));
        }
    }
}