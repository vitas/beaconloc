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

package com.samebits.beacon.locator.action;

import android.content.Context;
import android.net.Uri;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.NotificationAction;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vitas on 04/03/16.
 */
public class UrlAction extends NoneAction {

    public UrlAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        try {
            Uri.parse(param);

            if (param.startsWith("http://")) {
                new CallUrl<>(param).start();
            }

            if (param.startsWith("https://")) {
                new CallUrl<HttpsURLConnection>(param).start();
            }
        } catch (Exception e) {
            return context.getString(R.string.action_urlaction_error);
        }
        return super.execute(context);
    }

    private class CallUrl<T extends HttpURLConnection> extends Thread {

        final String url;

        public CallUrl(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                T urlConnection = (T) new URL(url).openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    readStream(in);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                // nothing to do.
            }
        }
    }

    private void readStream(InputStream in) throws IOException  {
        // nothing to do.
        in.close();
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "UrlAction, url: " + param;
    }
}
