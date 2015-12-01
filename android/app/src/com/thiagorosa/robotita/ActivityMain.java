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

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.thiagorosa.robotita.manager.BluetoothManager;
import com.thiagorosa.robotita.manager.PreferencesManager;

public class ActivityMain extends AppCompatActivity {

    protected Toolbar mToolbar = null;

    @Override
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.theme_secondary));
        }

        if (savedInstanceState == null) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = getSupportFragmentManager().getBackStackEntryAt(0);
                getSupportFragmentManager().popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            Fragment fragment = new FragmentMain();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
            transaction.replace(R.id.fragment, fragment, "fragment");
            transaction.addToBackStack("main");
            transaction.commitAllowingStateLoss();

            if (!TextUtils.isEmpty(PreferencesManager.getDeviceMAC(getApplicationContext()))) {
                Bundle args = new Bundle();
                args.putString(FragmentDeviceList.EXTRA_MAC, PreferencesManager.getDeviceMAC(getApplicationContext()));

                Fragment fragmentDevice = new FragmentDeviceList();
                fragmentDevice.setArguments(args);
                FragmentTransaction transactionDevice = getSupportFragmentManager().beginTransaction();
                transactionDevice.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transactionDevice.replace(R.id.fragment, fragmentDevice, "fragment");
                transactionDevice.addToBackStack("device");
                transactionDevice.commitAllowingStateLoss();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothManager.getInstance().disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        if (item.getItemId() == android.R.id.home) {
            try {
                getSupportFragmentManager().popBackStack();
            } catch (Exception e) {
            }
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            try {
                getSupportFragmentManager().popBackStack();
            } catch (Exception e) {
            }
        } else {
            finish();
        }
    }

    public void setScreenTitle(String title, String subtitle) {
        if (getSupportActionBar() != null) {
            if (title != null) {
                getSupportActionBar().setTitle(title);
            }
            if (subtitle != null) {
                getSupportActionBar().setSubtitle(subtitle);
            }
        }
    }

}