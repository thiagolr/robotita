/*
  Copyright (c) 2015 Thiago Lopes Rosa

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.thiagorosa.robotita;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

import com.thiagorosa.robotita.common.CustomFragment;
import com.thiagorosa.robotita.common.Logger;
import com.thiagorosa.robotita.manager.BluetoothManager;
import com.thiagorosa.robotita.model.Pin;

public class FragmentManualControl extends CustomFragment implements OnSeekBarChangeListener {

    private static final int SERVO_FEED_INITIAL_POSITION = 150;
    private static final int SERVO_FEED_INTERVAL = 5000;

    private Switch mRelayTop = null;
    private SeekBar mEscTop = null;
    private Switch mRelayBottom = null;
    private SeekBar mEscBottom = null;
    private SeekBar mServoFeed = null;
    private Switch mServoFeedAuto = null;
    private SeekBar mServoHorizontal = null;
    private SeekBar mServoVertical = null;
    private SeekBar mServoRotation = null;
    private Switch mRelayAgitator = null;

    private int mEscTopCurrent = 0;
    private int mEscBottomCurrent = 0;
    private int mServoFeedCurrent = 0;
    private int mServoHorizontalCurrent = 0;
    private int mServoVerticalCurrent = 0;
    private int mServoRotationCurrent = 0;

    private boolean isMotorTopRunning = false;
    private boolean isMotorBottomRunning = false;
    private boolean isFeeding = false;
    private int mFeedCounter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manual_control, null);

        if (!BluetoothManager.getInstance().isConnected()) {
            //getFragmentManager().popBackStack();
            //return null;
        }

        mRelayTop = (Switch) view.findViewById(R.id.relay_top);
        mRelayTop.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isMotorTopRunning) {
                    BluetoothManager.getInstance().write(Pin.RELAY_MOTOR_TOP1, isChecked ? 1 : 0);
                    BluetoothManager.getInstance().write(Pin.RELAY_MOTOR_TOP2, isChecked ? 0 : 1);
                    Logger.C("RELAY TOP MOTOR -> " + isChecked + "," + !isChecked);
                } else {
                    Logger.C("ERROR: TOP MOTOR IS RUNNING");
                }
            }
        });

        mRelayBottom = (Switch) view.findViewById(R.id.relay_bottom);
        mRelayBottom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isMotorBottomRunning) {
                    BluetoothManager.getInstance().write(Pin.RELAY_MOTOR_BOTTOM1, isChecked ? 1 : 0);
                    BluetoothManager.getInstance().write(Pin.RELAY_MOTOR_BOTTOM2, isChecked ? 0 : 1);
                    Logger.C("RELAY BOTTOM MOTOR -> " + isChecked + "," + !isChecked);
                } else {
                    Logger.C("ERROR: BOTTOM MOTOR IS RUNNING");
                }
            }
        });

        mEscTop = (SeekBar) view.findViewById(R.id.esc_top);
        mEscTop.setOnSeekBarChangeListener(this);
        mEscTop.setTag(Pin.ESC_MOTOR_TOP);

        mEscBottom = (SeekBar) view.findViewById(R.id.esc_bottom);
        mEscBottom.setOnSeekBarChangeListener(this);
        mEscBottom.setTag(Pin.ESC_MOTOR_BOTTOM);

        mServoFeed = (SeekBar) view.findViewById(R.id.servo_feed);
        mServoFeed.setOnSeekBarChangeListener(this);
        mServoFeed.setTag(Pin.SERVO_FEED);

        mServoFeedAuto = (Switch) view.findViewById(R.id.servo_feed_auto);
        mServoFeedAuto.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    isFeeding = true;
                    mServoFeedCurrent = SERVO_FEED_INITIAL_POSITION;
                    mServoFeed.setProgress(mServoFeedCurrent);
                    mServoFeed.setEnabled(false);
                    mFeedCounter = 0;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isFeeding) {
                                Logger.C("SERVO FEED AUTO " + ++mFeedCounter);

                                BluetoothManager.getInstance().servo(Pin.SERVO_FEED, 20);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                }
                                BluetoothManager.getInstance().servo(Pin.SERVO_FEED, SERVO_FEED_INITIAL_POSITION);

                                try {
                                    Thread.sleep(SERVO_FEED_INTERVAL);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }).start();
                } else {
                    isFeeding = false;
                    mServoFeedCurrent = SERVO_FEED_INITIAL_POSITION;
                    mServoFeed.setProgress(mServoFeedCurrent);
                    mServoFeed.setEnabled(true);
                }
            }
        });

        mServoHorizontal = (SeekBar) view.findViewById(R.id.servo_horizontal);
        mServoHorizontal.setOnSeekBarChangeListener(this);
        mServoHorizontal.setTag(Pin.SERVO_HORIZONTAL);

        mServoVertical = (SeekBar) view.findViewById(R.id.servo_vertical);
        mServoVertical.setOnSeekBarChangeListener(this);
        mServoVertical.setTag(Pin.SERVO_VERTICAL);

        mServoRotation = (SeekBar) view.findViewById(R.id.servo_rotation);
        mServoRotation.setOnSeekBarChangeListener(this);
        mServoRotation.setTag(Pin.SERVO_ROTATION);

        mRelayAgitator = (Switch) view.findViewById(R.id.servo_agitator);
        mRelayAgitator.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BluetoothManager.getInstance().write(Pin.RELAY_AGITATOR, isChecked ? 1 : 0);
                Logger.C("RELAY AGITATOR -> " + isChecked);
            }
        });

        BluetoothManager.getInstance().servo(Pin.ESC_MOTOR_TOP, 0);
        BluetoothManager.getInstance().servo(Pin.ESC_MOTOR_BOTTOM, 0);

        BluetoothManager.getInstance().servo(Pin.SERVO_FEED, SERVO_FEED_INITIAL_POSITION);
        BluetoothManager.getInstance().servo(Pin.SERVO_HORIZONTAL, 180 / 2);
        BluetoothManager.getInstance().servo(Pin.SERVO_VERTICAL, 180 / 2);
        BluetoothManager.getInstance().servo(Pin.SERVO_ROTATION, 180 / 2);
        BluetoothManager.getInstance().servo(Pin.RELAY_AGITATOR, 180 / 2);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setScreenTitle(getText(R.string.control_title).toString(), null);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int pin = (Integer) seekBar.getTag();
        int current = 0;

        switch (pin) {
        case Pin.ESC_MOTOR_TOP:
            current = mEscTopCurrent;
            if (mEscTopCurrent == 0) {
                mRelayTop.setEnabled(true);
                isMotorTopRunning = false;
            } else {
                mRelayTop.setEnabled(false);
                isMotorTopRunning = true;
            }
            break;
        case Pin.ESC_MOTOR_BOTTOM:
            current = mEscBottomCurrent;
            if (mEscBottomCurrent == 0) {
                mRelayBottom.setEnabled(true);
                isMotorBottomRunning = false;
            } else {
                mRelayBottom.setEnabled(false);
                isMotorBottomRunning = true;
            }
            break;
        case Pin.SERVO_FEED:
            current = mServoFeedCurrent;
            break;
        case Pin.SERVO_HORIZONTAL:
            current = mServoHorizontalCurrent;
            break;
        case Pin.SERVO_VERTICAL:
            current = mServoVerticalCurrent;
            break;
        case Pin.SERVO_ROTATION:
            current = mServoRotationCurrent;
            break;
        }

        BluetoothManager.getInstance().servo(pin, current);
        Logger.C("SERVO -> " + current);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int pin = (Integer) seekBar.getTag();

        switch (pin) {
        case Pin.ESC_MOTOR_TOP:
            mEscTopCurrent = progress;
            break;
        case Pin.ESC_MOTOR_BOTTOM:
            mEscBottomCurrent = progress;
            break;
        case Pin.SERVO_FEED:
            mServoFeedCurrent = progress;
            break;
        case Pin.SERVO_HORIZONTAL:
            mServoHorizontalCurrent = progress;
            break;
        case Pin.SERVO_VERTICAL:
            mServoVerticalCurrent = progress;
            break;
        case Pin.SERVO_ROTATION:
            mServoRotationCurrent = progress;
            break;
        }
    }

}