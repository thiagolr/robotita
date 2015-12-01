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

package com.thiagorosa.robotita.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.thiagorosa.robotita.common.Logger;
import com.thiagorosa.robotita.model.Device;

public class BluetoothManager {

    private static final BluetoothManager INSTANCE = new BluetoothManager();

    // UUID
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final int REQUEST_ENABLE_BLUETOOTH = 1;

    // DEVICE
    private Device mSelectedDevice = null;
    private BluetoothDevice mDevice = null;
    private BluetoothSocket mDeviceSocket = null;
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private boolean isConnected = false;

    /*******************************************************************************************
     *******************************************************************************************/

    private BluetoothManager() {
    }

    public static BluetoothManager getInstance() {
        return INSTANCE;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public BluetoothAdapter getAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isSupported() {
        return getAdapter() != null;
    }

    public boolean isEnabled() {
        return getAdapter().isEnabled();
    }

    public void promptToEnable(Fragment fragment) {
        if (!isEnabled() && (fragment != null)) {
            fragment.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
        }
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public boolean isDiscovering() {
        return getAdapter().isDiscovering();
    }

    public void startDiscovery() {
        if (isDiscovering()) {
            cancelDiscovery();
        }
        getAdapter().startDiscovery();
    }

    public void cancelDiscovery() {
        getAdapter().cancelDiscovery();
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public boolean isConnected() {
        return isConnected;
    }

    public boolean connect(Device device) {
        cancelDiscovery();

        mDevice = getAdapter().getRemoteDevice(device.getMAC());
        mSelectedDevice = device;

        try {
            mDeviceSocket = mDevice.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            Logger.BT("ERROR: socket create failed (" + e.getMessage() + ")");
            return false;
        }

        try {
            mDeviceSocket.connect();
        } catch (IOException e) {
            Logger.BT("ERROR: open socket failed (" + e.getMessage() + ")");
            try {
                mDeviceSocket.close();
            } catch (IOException e2) {
            }
            return false;
        }

        try {
            mOutputStream = mDeviceSocket.getOutputStream();
        } catch (IOException e) {
            Logger.BT("ERROR: get output stream failed (" + e.getMessage() + ")");
            return false;
        }

        try {
            mInputStream = mDeviceSocket.getInputStream();
        } catch (IOException e) {
            Logger.BT("ERROR: get input stream failed (" + e.getMessage() + ")");
            return false;
        }

        isConnected = true;
        return true;
    }

    public void disconnect() {
        if (mDeviceSocket != null) {
            try {
                mDeviceSocket.close();
                mDeviceSocket = null;
            } catch (IOException e) {
                Logger.BT("ERROR: close socket failed (" + e.getMessage() + ")");
            }
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
                mOutputStream = null;
            } catch (IOException e) {
                Logger.BT("ERROR: close output stream failed (" + e.getMessage() + ")");
            }
        }
        if (mInputStream != null) {
            try {
                mInputStream.close();
                mInputStream = null;
            } catch (IOException e) {
                Logger.BT("ERROR: close input stream failed (" + e.getMessage() + ")");
            }
        }

        mSelectedDevice = null;
        isConnected = false;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public Device getSelectedDevice() {
        return mSelectedDevice;
    }

    public void setSelectedDevice(Device device) {
        mSelectedDevice = device;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public int read(int pin) {
        int value = -1;
        try {
            mOutputStream.write(0);
            mOutputStream.write(pin);
            mOutputStream.write(0);
            value = mInputStream.read();
            Logger.BT("READ: pin=" + pin + " value=" + value);

        } catch (IOException e) {
            Logger.BT("ERROR: failed to read (" + e.getMessage() + ")");
            return value;
        } catch (Exception e) {
            Logger.BT("ERROR: " + e.getMessage());
            return value;
        }
        return value;
    }

    public boolean write(int pin, int value) {
        try {
            Logger.BT("WRITE: pin=" + pin + " value=" + value);

            mOutputStream.write(1);
            mOutputStream.write(pin);
            mOutputStream.write(value);
        } catch (IOException e) {
            Logger.BT("ERROR: failed to write (" + e.getMessage() + ")");
            return false;
        } catch (Exception e) {
            Logger.BT("ERROR: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean servo(int pin, int value) {
        try {
            Logger.BT("SERVO: pin=" + pin + " value=" + value);

            mOutputStream.write(2);
            mOutputStream.write(pin);
            mOutputStream.write(value);
        } catch (IOException e) {
            Logger.BT("ERROR: failed to servo (" + e.getMessage() + ")");
            return false;
        } catch (Exception e) {
            Logger.BT("ERROR: " + e.getMessage());
            return false;
        }
        return true;
    }

}
