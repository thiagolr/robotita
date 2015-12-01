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

package com.thiagorosa.robotita.common;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.thiagorosa.robotita.ActivityMain;

public abstract class CustomFragment extends Fragment {

    public void setScreenTitle(String title, String subtitle) {
        if (getActivity() != null) {
            ((ActivityMain) getActivity()).setScreenTitle(title, subtitle);
        }
    }

    public ActionBar getSupportActionBar() {
        if (getActivity() != null) {
            return ((ActivityMain) getActivity()).getSupportActionBar();
        }
        return null;
    }

}
