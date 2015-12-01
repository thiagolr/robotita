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

#include <Servo.h> 

boolean DEBUG = false;

#define TYPE_READ 0
#define TYPE_WRITE 1
#define TYPE_SERVO 2

#define PIN_MOTOR_TOP_ESC 2
#define PIN_MOTOR_TOP_RELAY1 3
#define PIN_MOTOR_TOP_RELAY2 4
#define PIN_MOTOR_BOTTOM_ESC 5
#define PIN_MOTOR_BOTTOM_RELAY1 6
#define PIN_MOTOR_BOTTOM_RELAY2 7
#define PIN_SERVO_FEED 8
#define PIN_SERVO_HORIZONTAL 9
#define PIN_SERVO_VERTICAL 10
#define PIN_SERVO_ROTATION 11
#define PIN_SERVO_AGITATOR_RELAY 12
#define PIN_BLUETOOTH_TX 18
#define PIN_BLUETOOTH_RX 19

Servo escTop;
Servo escBottom;
Servo servoFeed;
Servo servoHorizontal;
Servo servoVertical;
Servo servoRotation;

byte type = 0;
byte pin = 0;
byte data = 0;

void setup() {  

  // setup the serial module
  Serial.begin(9600);  

  // setup the bluetooth module
  pinMode(PIN_BLUETOOTH_RX, INPUT_PULLUP);  
  Serial1.begin(9600);
  
  // setup the relay pins
  pinMode(PIN_MOTOR_TOP_RELAY1, OUTPUT);
  pinMode(PIN_MOTOR_TOP_RELAY2, OUTPUT);
  pinMode(PIN_MOTOR_BOTTOM_RELAY1, OUTPUT);
  pinMode(PIN_MOTOR_BOTTOM_RELAY2, OUTPUT);
  pinMode(PIN_SERVO_AGITATOR_RELAY, OUTPUT);    
  
  // setup the top esc
  escTop.attach(PIN_MOTOR_TOP_ESC);
  escTop.write(0);
  delay(15);
  
  // setup the bottom esc
  escBottom.attach(PIN_MOTOR_BOTTOM_ESC);
  escBottom.write(0);
  delay(15);
  
  // setup the feed servo
  servoFeed.attach(PIN_SERVO_FEED);
  servoFeed.write(150);
  delay(15);

  // setup the horizontal servo  
  servoHorizontal.attach(PIN_SERVO_HORIZONTAL);
  servoHorizontal.write(90);
  delay(15);

  // setup the verticall servo  
  servoVertical.attach(PIN_SERVO_VERTICAL);
  servoVertical.write(90);
  delay(15);

  // setup the rotation servo  
  servoRotation.attach(PIN_SERVO_ROTATION);
  servoRotation.write(90);
  delay(15);  
}  

void loop() {  

  while (Serial1.available() >= 3)  {

    type = Serial1.read();

    switch (type) {

    case TYPE_READ:
      pin = Serial1.read();
      Serial1.read();      
      data = digitalRead(pin);
      Serial1.write(data);
      if (DEBUG) {
        Serial.print("PIN_");
        Serial.print(pin);    
        Serial.print(" -> ");
        Serial.println(data);    
      }
      break;

    case TYPE_WRITE:
      pin = Serial1.read();
      data = Serial1.read();
      digitalWrite(pin, data == 1 ? HIGH : LOW);
      if (DEBUG) {
        Serial.print("PIN_");
        Serial.print(pin);    
        Serial.print(" <- ");
        Serial.println(data);    
      }
      break;
      
    case TYPE_SERVO:
      pin = Serial1.read();
      data = Serial1.read();
      
      Servo servo;
      switch (pin) {
        case PIN_MOTOR_TOP_ESC:
          servo = escTop;
          break;  
        case PIN_MOTOR_BOTTOM_ESC:
          servo = escBottom;
          break;  
        case PIN_SERVO_FEED:
          servo = servoFeed;
          break;            
        case PIN_SERVO_HORIZONTAL:
          servo = servoHorizontal;
          break;    
        case PIN_SERVO_VERTICAL:
          servo = servoVertical;
          break;    
        case PIN_SERVO_ROTATION:
          servo = servoRotation;
          break;
      }
      
      servo.write(data);       
      
      if (DEBUG) {
        Serial.print("SERVO_");
        Serial.print(pin);    
        Serial.print(" <- ");
        Serial.println(data);    
      }
      break;

    }

  }

  delay(100);
}  

