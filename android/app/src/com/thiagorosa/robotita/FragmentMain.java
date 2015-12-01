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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.thiagorosa.robotita.common.CustomFragment;
import com.thiagorosa.robotita.manager.BluetoothManager;
import com.thiagorosa.robotita.manager.PreferencesManager;

public class FragmentMain extends CustomFragment {

    private CardView mSelectDevice = null;
    private CardView mManualControl = null;
    private CardView mDisconnect = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, null);

        mSelectDevice = (CardView) view.findViewById(R.id.select_device);
        mSelectDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentControl = new FragmentDeviceList();
                FragmentTransaction transactionControl = getFragmentManager().beginTransaction();
                transactionControl.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transactionControl.replace(R.id.fragment, fragmentControl, "fragment");
                transactionControl.addToBackStack("control");
                transactionControl.commitAllowingStateLoss();
            }
        });

        mManualControl = (CardView) view.findViewById(R.id.manual_control);
        mManualControl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentControl = new FragmentManualControl();
                FragmentTransaction transactionControl = getFragmentManager().beginTransaction();
                transactionControl.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transactionControl.replace(R.id.fragment, fragmentControl, "fragment");
                transactionControl.addToBackStack("control");
                transactionControl.commitAllowingStateLoss();
            }
        });

        mDisconnect = (CardView) view.findViewById(R.id.disconnect);
        mDisconnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setDeviceMAC(getActivity(), "");
                BluetoothManager.getInstance().disconnect();
                setScreenTitle(null, BluetoothManager.getInstance().isConnected() ? getText(R.string.device_connected).toString() : getText(R.string.device_not_connected).toString());
                mDisconnect.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        setScreenTitle(getText(R.string.app_title).toString(), BluetoothManager.getInstance().isConnected() ? getText(R.string.device_connected).toString() : getText(R.string.device_not_connected)
                .toString());

        if (BluetoothManager.getInstance().isConnected()) {
            mDisconnect.setVisibility(View.VISIBLE);
        } else {
            mDisconnect.setVisibility(View.GONE);
        }

    }

}
