#include <SoftwareSerial.h>
#include <DFPlayer_Mini_Mp3.h>

SoftwareSerial mySerial(10, 11); // виртуальные контакты RX и TX

void setup()  
{
  Serial.begin(57600);
  mySerial.begin(4800);
  mp3_set_serial (mySerial);    
  mp3_set_volume (25);
  delay (100);
  mp3_play ();
  delay (100);
}

void loop()
{
  mp3_next ();
  delay (1000);
}
