#include <SoftwareSerial.h>
#include <DFPlayer_Mini_Mp3.h>
#include <Bounce2.h>
#include <iarduino_OLED_txt.h>

SoftwareSerial mySerial(10, 11); // RX, TX
Bounce bouncer = Bounce();
iarduino_OLED_txt myOLED(0x3C);
extern uint8_t MediumFont[];

int i = 0;
int track = 10;
int value = 0;
String var;
char *str;

//void lapki(char *var1);

void setup () 
{
    Serial.begin (9600);
    Serial.setTimeout(100);
  
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
    myOLED.setCursor(16, 4);
    if (bouncer.update())
    {
        if (bouncer.read() == 0)
        {
            i += 1;
            myOLED.print(i);
            mp3_play(track);
            Serial.println(i);
        }  
    }
    if (Serial.available())
    {
        Serial.println("Hello");
        var = Serial.readString();
        Serial.println(var);
        lapki(var);
    }      
}

void lapki(String var1)
{
  int a = 0;
        while (var1[a] != 32)
        {
            Serial.print("ok1");
            while ((var1[a] >= 97)&&(var1[a] <=122))
            {
                  str[a] = var1[a];
                  a++;
                  Serial.print("ok2");
            }
            a++;
        }
        while ((var1[a] >= 48)&&(var1[a] >= 57))
        {
              Serial.print("ok3");
              value = value * 10 + var1[a] - 48;
              a++;
        }
   //     Serial.println("error");
  //      Serial.println(*str);
  //      Serial.println(value);  
}
