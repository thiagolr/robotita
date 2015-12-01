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

package com.thiagorosa.robotita.model;

public class Pin {

    // top motor ESC
    public static final int ESC_MOTOR_TOP = 2;

    // top motor SPDT relay 1
    public static final int RELAY_MOTOR_TOP1 = 3;

    // top motor SPDT relay 2
    public static final int RELAY_MOTOR_TOP2 = 4;

    // bottom motor ESC
    public static final int ESC_MOTOR_BOTTOM = 5;

    // bottom motor SPDT relay 1
    public static final int RELAY_MOTOR_BOTTOM1 = 6;

    // bottom motor SPDT relay 2
    public static final int RELAY_MOTOR_BOTTOM2 = 7;

    // servo to feed the balls
    public static final int SERVO_FEED = 8;

    // servo for horizontal movement
    public static final int SERVO_HORIZONTAL = 9;

    // servo for vertical movement
    public static final int SERVO_VERTICAL = 10;

    // servo for rotatio movement
    public static final int SERVO_ROTATION = 11;

    // relay to control agitator on/off
    public static final int RELAY_AGITATOR = 12;

}
