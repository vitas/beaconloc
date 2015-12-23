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

import android.content.Context;

import com.samebits.beacon.locator.data.DbStoreService;
import com.samebits.beacon.locator.data.StoreService;
import com.samebits.beacon.locator.injection.UserScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vitas on 20/12/15.
 */
@Module
public class DataModule {
    private Context mContext;

    public DataModule(Context context) {
        mContext = context;
    }

    @Provides
    @UserScope
    StoreService provideStoreService() {
        return new DbStoreService(mContext);
    }
}
