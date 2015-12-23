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

import java.util.ArrayDeque;

/**
 * thanks
 * http://stackoverflow.com/questions/4699417/android-compass-orientation-on-unreliable-low-pass-filter
 */
public class AngleLowpassFilter {

    private final int LENGTH = 10;
    private float sumSin, sumCos;

    private ArrayDeque<Float> queue = new ArrayDeque<>();

    public void add(float radians) {

        sumSin += (float) Math.sin(radians);
        sumCos += (float) Math.cos(radians);

        queue.add(radians);

        if (queue.size() > LENGTH) {

            float old = queue.poll();
            sumSin -= Math.sin(old);
            sumCos -= Math.cos(old);
        }
    }

    public float average() {
        int size = queue.size();
        return (float) Math.atan2(sumSin / size, sumCos / size);
    }
}