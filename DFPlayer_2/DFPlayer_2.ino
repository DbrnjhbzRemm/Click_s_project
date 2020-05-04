#include <SoftwareSerial.h>
#include <DFPlayer_Mini_Mp3.h>
#include <Bounce2.h>
#include <iarduino_OLED_txt.h>

SoftwareSerial mySerial(10, 11); // RX, TX
Bounce bouncer = Bounce();
iarduino_OLED_txt myOLED(0x3C);
extern uint8_t MediumFont[];

int i = 0;

void setup () 
{
    Serial.begin (9600);
  
    mySerial.begin(9600);
    mp3_set_serial (mySerial);    
    mp3_set_volume (10);
    
    pinMode(2 ,INPUT);
    digitalWrite(2 ,HIGH);
    bouncer.attach(2);
    bouncer.interval(5);
    
    myOLED.begin();
    myOLED.setFont(MediumFont);
}
void loop () 
{
    myOLED.setCursor(16,4);
    if (bouncer.update())
    {
        if (bouncer.read()==0)
        {
           i = i + 1;
           myOLED.print(i);
           mp3_play (3);
        }
    }
}
