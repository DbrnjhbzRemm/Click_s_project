#include <SoftwareSerial.h>
#include <DFPlayer_Mini_Mp3.h>
#include <Bounce2.h>
#include <iarduino_OLED_txt.h>

SoftwareSerial mySerial(10, 11); // RX, TX
Bounce bouncer = Bounce();
iarduino_OLED_txt myOLED(0x3C);
extern uint8_t MediumFont[];

int i = 0;
int track = 5;
int value = 10;
String var;
int critchance = 10;
int critplier = 2;

void  ft_variable(char *str, int value)
{
  if ((str[0] == 'v') && (str[1] == 'o') && (str[2] == 'l'))
  {
    mp3_set_volume (value);
  }
  if ((str[0] == 'b') && (str[1] == 't'))
  {
    myOLED.clrScr();
    i = value;
    myOLED.print(i);
  }
  if ((str[0] == 't') && (str[1] == 'r') && (str[2] == 'a'))
  {
    track = value;
  }
}

void  ft_parsing(String str)
{
  int c = 0;
  int result = 0;
  char *var2;

  var2 = malloc(6 * sizeof(char));

  while ((str[c] != 32)||(str[c] <= 48) && (str[c] >= 57))
  {
    var2[c] = str[c];
    c++;
  }
    c++;
  while ((str[c] >= 48) && (str[c] <= 57))
  {
    result = result * 10 + str[c] - 48;
    c++;
  }

  ft_variable(var2, result);
}

void setup ()
{
  Serial.begin (9600);

  mySerial.begin(9600);
  mp3_set_serial (mySerial);
  mp3_set_volume (value);

  pinMode(2 , INPUT);
  digitalWrite(2 , HIGH);
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
      if (random(1, 100) < critchance)
      {
          i += critplier;
          myOLED.print(i);
          mp3_play(3);
          Serial.println(i);
      }
      else
      {
          i += 1;
          myOLED.print(i);
          mp3_play(track);
          Serial.println(i);
      }
    }
  }
  if (Serial.available())
  {
      var = Serial.readString();
      ft_parsing(var);
  }
}
