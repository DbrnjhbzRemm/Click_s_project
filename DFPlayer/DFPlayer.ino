#include "Arduino.h"
#include "SoftwareSerial.h"
#include "DFRobotDFPlayerMini.h"

SoftwareSerial mySoftwareSerial(10, 11); // RX, TX
DFRobotDFPlayerMini myDFPlayer;

void setup()
{
  mySoftwareSerial.begin(9600);
  Serial.begin(115200);
  myDFPlayer.begin(mySoftwareSerial);  //Use softwareSerial to communicate with mp3.
  myDFPlayer.volume(10);  //Set volume value. From 0 to 30
  //Play the first mp3
}

void loop()
{
    myDFPlayer.play(1);
   // delay(90000);
  //  myDFPlayer.next();  //Play next mp3 every 3 second.
}
