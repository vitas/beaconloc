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

package com.samebits.beacon.locator.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.samebits.beacon.locator.R;
import com.samebits.beacon.locator.model.DetectedBeacon;
import com.samebits.beacon.locator.util.AngleLowpassFilter;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RadarScanView extends View implements SensorEventListener {
    private final static int RADAR_RADIS_VISION_METERS = 15;
    private static String mMetricDisplayFormat = "%.0fm";
    private static String mEnglishDisplayFormat = "%.0fft";
    private static float METER_PER_FEET = 0.3048f;
    private static float FEET_PER_METER = 3.28084f;

    private float mDistanceRatio = 1.0f;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mOrientation = new float[3];
    private AngleLowpassFilter angleLowpassFilter = new AngleLowpassFilter();
    private float mLast_bearing;
    private Context mContext;
    private WindowManager mWindowManager;
    private Map<String, DetectedBeacon> mBeacons = new LinkedHashMap();
    private boolean mHaveDetected = false;
    private TextView mInfoView;
    private Rect mTextBounds = new Rect();
    private Paint mGridPaint;
    private Paint mErasePaint;
    private Bitmap mBlip;
    private boolean mUseMetric;
    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint0;
    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint1;
    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint2;
    /**
     * Used to draw a beacon
     */
    private Paint mBeaconPaint;
    /**
     * Time in millis when the most recent sweep began
     */
    private long mSweepTime;
    /**
     * True if the sweep has not yet intersected the blip
     */
    private boolean mSweepBefore;
    /**
     * Time in millis when the sweep last crossed the blip
     */
    private long mBlipTime;

    public RadarScanView(Context context) {
        this(context, null);
    }

    public RadarScanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarScanView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        // Paint used for the rings and ring text
        mGridPaint = new Paint();
        mGridPaint.setColor(getResources().getColor(R.color.hn_orange_lighter));

        mGridPaint.setAntiAlias(true);
        mGridPaint.setStyle(Style.STROKE);
        mGridPaint.setStrokeWidth(2.0f);
        mGridPaint.setTextSize(16.0f);
        mGridPaint.setTextAlign(Align.CENTER);

        // Paint used to erase the rectangle behind the ring text
        mErasePaint = new Paint();
        mErasePaint.setColor(getResources().getColor(R.color.white));
        //mErasePaint.setColor(getResources().getColor(R.color.hn_orange_lighter));

        mErasePaint.setStyle(Style.FILL);

        mBeaconPaint = new Paint();
        mBeaconPaint.setColor(getResources().getColor(R.color.white));
        mBeaconPaint.setAntiAlias(true);
        mBeaconPaint.setStyle(Style.FILL_AND_STROKE);

        // Outer ring of the sweep
        mSweepPaint0 = new Paint();
        mSweepPaint0.setColor(ContextCompat.getColor(context, R.color.hn_orange));
        mSweepPaint0.setAntiAlias(true);
        mSweepPaint0.setStyle(Style.STROKE);
        mSweepPaint0.setStrokeWidth(3f);

        // Middle ring of the sweep
        mSweepPaint1 = new Paint();
        mSweepPaint1.setColor(ContextCompat.getColor(context, R.color.hn_orange));

        mSweepPaint1.setAntiAlias(true);
        mSweepPaint1.setStyle(Style.STROKE);
        mSweepPaint1.setStrokeWidth(3f);

        // Inner ring of the sweep
        mSweepPaint2 = new Paint();
        mSweepPaint2.setColor(ContextCompat.getColor(context, R.color.hn_orange));

        mSweepPaint2.setAntiAlias(true);
        mSweepPaint2.setStyle(Style.STROKE);
        mSweepPaint2.setStrokeWidth(3f);

        mBlip = ((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.ic_location_on_black_24dp)).getBitmap();

    }

    /**
     * Sets the view that we will use to report distance
     *
     * @param t The text view used to report distance
     */
    public void setDistanceView(TextView t) {
        mInfoView = t;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = center - 8;

        // Draw the rings
        final Paint gridPaint = mGridPaint;
        canvas.drawCircle(center, center, radius, gridPaint);
        canvas.drawCircle(center, center, radius * 3 / 4, gridPaint);
        canvas.drawCircle(center, center, radius >> 1, gridPaint);
        canvas.drawCircle(center, center, radius >> 2, gridPaint);

        int blipRadius = (int) (mDistanceRatio * radius);

        final long now = SystemClock.uptimeMillis();
        if (mSweepTime > 0) {
            // Draw the sweep. Radius is determined by how long ago it started
            long sweepDifference = now - mSweepTime;
            if (sweepDifference < 512L) {
                int sweepRadius = (int) (((radius + 6) * sweepDifference) >> 9);
                canvas.drawCircle(center, center, sweepRadius, mSweepPaint0);
                canvas.drawCircle(center, center, sweepRadius - 2, mSweepPaint1);
                canvas.drawCircle(center, center, sweepRadius - 4, mSweepPaint2);

                // Note when the sweep has passed the blip
                boolean before = sweepRadius < blipRadius;
                if (!before && mSweepBefore) {
                    mSweepBefore = false;
                    mBlipTime = now;
                }
            } else {
                mSweepTime = now + 1000;
                mSweepBefore = true;
            }
            postInvalidate();
        }

        // Draw horizontal and vertical lines
        canvas.drawLine(center, center - (radius >> 2) + 6, center, center - radius - 6, gridPaint);
        canvas.drawLine(center, center + (radius >> 2) - 6, center, center + radius + 6, gridPaint);
        canvas.drawLine(center - (radius >> 2) + 6, center, center - radius - 6, center, gridPaint);
        canvas.drawLine(center + (radius >> 2) - 6, center, center + radius + 6, center, gridPaint);

        // Draw X in the center of the screen
        canvas.drawLine(center - 4, center - 4, center + 4, center + 4, gridPaint);
        canvas.drawLine(center - 4, center + 4, center + 4, center - 4, gridPaint);

        if (mHaveDetected) {

            // Draw the blip. Alpha is based on how long ago the sweep crossed the blip
            long blipDifference = now - mBlipTime;
            gridPaint.setAlpha(255 - (int) ((128 * blipDifference) >> 10));

            double bearingToTarget = mLast_bearing;
            double drawingAngle = Math.toRadians(bearingToTarget) - (Math.PI / 2);
            float cos = (float) Math.cos(drawingAngle);
            float sin = (float) Math.sin(drawingAngle);

            addText(canvas, getRatioDistanceText(0.25f), center, center + (radius >> 2));
            addText(canvas, getRatioDistanceText(0.5f), center, center + (radius >> 1));
            addText(canvas, getRatioDistanceText(0.75f), center, center + radius * 3 / 4);
            addText(canvas, getRatioDistanceText(1.0f), center, center + radius);

            for (Map.Entry<String, DetectedBeacon> entry : mBeacons.entrySet()) {
                //String key = entry.getKey();
                DetectedBeacon dBeacon = entry.getValue();
                System.out.println("value: " + dBeacon);

                // drawing the beacon
                if (((System.currentTimeMillis() - dBeacon.getTimeLastSeen()) / 1000 < 5)) {
                    canvas.drawBitmap(mBlip, center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                            center + (sin * distanceToPix(dBeacon.getDistance())) - 8, gridPaint);
                }
            }

            gridPaint.setAlpha(255);
        }
    }

    private String getRatioDistanceText(float ringRation) {
        return new DecimalFormat("##0.00").format(RADAR_RADIS_VISION_METERS * mDistanceRatio * ringRation);
    }

    /**
     * max radar range is 15 meters
     *
     * @param distance
     * @return distance in px
     */
    private float distanceToPix(double distance) {
        int center = getWidth() / 2;
        int radius = center - 8;
        return Math.round((radius * distance) / RADAR_RADIS_VISION_METERS * mDistanceRatio);
    }

    private void addText(Canvas canvas, String str, int x, int y) {
        mGridPaint.getTextBounds(str, 0, str.length(), mTextBounds);
        mTextBounds.offset(x - (mTextBounds.width() >> 1), y);
        mTextBounds.inset(-2, -2);
        canvas.drawRect(mTextBounds, mErasePaint);
        canvas.drawText(str, x, y, mGridPaint);
    }


    /**
     * Update state to reflect whether we are using metric or standard units.
     *
     * @param useMetric True if the display should use metric units
     */
    public void setUseMetric(boolean useMetric) {
        mUseMetric = useMetric;
        if (mHaveDetected) {
            // TODO
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        calcBearing(event);
    }

    private synchronized void calcBearing(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {

            /* Create rotation Matrix */
            float[] rotationMatrix = new float[9];
            if (SensorManager.getRotationMatrix(rotationMatrix, null,
                    mLastAccelerometer, mLastMagnetometer)) {
                SensorManager.getOrientation(rotationMatrix, mOrientation);

                float azimuthInRadians = mOrientation[0];

                angleLowpassFilter.add(azimuthInRadians);

                mLast_bearing = (float) (Math.toDegrees(angleLowpassFilter.average()) + 360) % 360;

                postInvalidate();

                //Log.d(Constants.TAG, "orientation bearing: " + mLast_bearing);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void insertBeacons(Collection<Beacon> beacons) {
        Iterator<Beacon> iterator = beacons.iterator();
        while (iterator.hasNext()) {
            DetectedBeacon dBeacon = new DetectedBeacon(iterator.next());
            dBeacon.setTimeLastSeen(System.currentTimeMillis());
            this.mBeacons.put(dBeacon.getId(), dBeacon);
        }
    }

    public void onDetectedBeacons(final Collection<Beacon> beacons) {

        insertBeacons(beacons);

        updateDistances();

        updateBeaconsInfo(beacons);

        invalidate();
    }

    private void updateBeaconsInfo(final Collection<Beacon> beacons) {
        mInfoView.setText(String.format(getResources().getString(R.string.text_scanner_found_beacons_size), beacons.size()));
    }

    /**
     * update on radar
     */
    private void updateDistances() {
        if (!mHaveDetected) {
            mHaveDetected = true;
        }
    }

    private void updateDistanceText(double distanceM) {
        String displayDistance;
        if (mUseMetric) {
            displayDistance = String.format(mMetricDisplayFormat, distanceM);
        } else {
            displayDistance = String.format(mEnglishDisplayFormat, distanceM * FEET_PER_METER);
        }
        mInfoView.setText(displayDistance);
    }

    /**
     * Turn on the sweep animation starting with the next draw
     */
    public void startSweep() {
        mInfoView.setText(R.string.text_scanning);
        mSweepTime = SystemClock.uptimeMillis();
        mSweepBefore = true;
    }

    /**
     * Turn off the sweep animation
     */
    public void stopSweep() {
        mSweepTime = 0L;
        mInfoView.setText("");
    }

}
