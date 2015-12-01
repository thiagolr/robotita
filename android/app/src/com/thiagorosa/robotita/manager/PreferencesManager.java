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

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static SharedPreferences systemSharedPreferences;

    private static final String PREFS_STRING_DEVICE = "device";

    public static boolean initPreferences(Context context) {
        if (systemSharedPreferences == null) {
            systemSharedPreferences = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        }
        return true;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    private static String getConfigString(Context context, String item, String defaultValue) {
        initPreferences(context);
        return systemSharedPreferences.getString(item, defaultValue);
    }

    private static void setConfigString(Context context, String item, String value) {
        initPreferences(context);
        SharedPreferences.Editor editor = systemSharedPreferences.edit();
        editor.putString(item, value);
        editor.commit();
    }

    public static String getDeviceMAC(Context context) {
        return getConfigString(context, PREFS_STRING_DEVICE, "");
    }

    public static void setDeviceMAC(Context context, String value) {
        setConfigString(context, PREFS_STRING_DEVICE, value);
    }

}
