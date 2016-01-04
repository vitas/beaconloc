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

package com.samebits.beacon.locator.injection.module;

import android.app.Application;
import android.content.Context;

import com.samebits.beacon.locator.action.ActionExecutor;
import com.samebits.beacon.locator.data.DataManager;

import org.altbeacon.beacon.BeaconManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {
    protected final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.application;
    }


    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new DataManager(this.application);
    }

    @Provides
    @Singleton
    ActionExecutor provideActionExecutor() {
        return new ActionExecutor(this.application);
    }

    @Provides
    @Singleton
    public BeaconManager provideBeaconManager() {
        BeaconManager manager = BeaconManager.getInstanceForApplication(application);


        //manager.setDebug(true);
        return manager;
    }

}