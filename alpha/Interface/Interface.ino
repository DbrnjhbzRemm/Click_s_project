#include <SoftwareSerial.h>
#include <DFPlayer_Mini_Mp3.h>
#include <Bounce2.h>
#include <iarduino_OLED_txt.h>

#define cursor_x 16
#define cursor_y 4
#define start_track 5
#define start_vol 10
#define start_crit_chance 10
#define start_crit_multiplier 2
#define start_crit_sound 7
#define button_pin 2
#define comand_setscore "sset"
#define comand_setvolume "svol"
#define comand_setclicktrack "strc"
#define comand_setcrittrack "ctrc"
#define comand_setcritmultiplier "cmul"
#define comand_setcritchance "ccha"

SoftwareSerial mySerial(10, 11); // RX, TX
Bounce bouncer = Bounce();
iarduino_OLED_txt myOLED(0x3C);
extern uint8_t MediumFont[];

int score;
int track;
String var;
int critchance;
int critplier;
int crit_sound;

void  comand(char *comand_str, int value)
// принимает строку команды и числовое значение и исходя из команды
// выбирает необходимое действие, передавая в нужную функцию значение
{
  if ((comand_str[0] == comand_setvolume[0]) && (comand_str[1] == comand_setvolume[1]) && (comand_str[2] == comand_setvolume[2]) && (comand_str[3] == comand_setvolume[3]))
  {
    mp3_set_volume(value);
  }
  if ((comand_str[0] == comand_setscore[0]) && (comand_str[1] == comand_setscore[1]) && (comand_str[2] == comand_setscore[2]) && (comand_str[3] == comand_setscore[3]))
  {
    myOLED.clrScr();
    score = value;
    myOLED.print(score);
  }
  if ((comand_str[0] == comand_setclicktrack[0]) && (comand_str[1] == comand_setclicktrack[1]) && (comand_str[2] == comand_setclicktrack[2]) && (comand_str[3] == comand_setclicktrack[3]))
  {
    track = value;
  }
  if ((comand_str[0] == comand_setcrittrack[0]) && (comand_str[1] == comand_setcrittrack[1]) && (comand_str[2] == comand_setcrittrack[2]) && (comand_str[3] == comand_setcrittrack[3]))
  {
    crit_sound = value;
  }
  if ((comand_str[0] == comand_setcritmultiplier[0]) && (comand_str[1] == comand_setcritmultiplier[1]) && (comand_str[2] == comand_setcritmultiplier[2]) && (comand_str[3] == comand_setcritmultiplier[3]))
  {
    critplier = value;
  }
  if ((comand_str[0] == comand_setcritchance[0]) && (comand_str[1] == comand_setcritchance[1]) && (comand_str[2] == comand_setcritchance[2]) && (comand_str[3] == comand_setcritchance[3]))
  {
    critchance = value;
  }
}

void  raw_parser(String comand_str)
//разделяет строку на массив символов с командой и числовое значение
{
  int c = 0;
  int result = 0;
  char *var2;

  var2 = malloc(4 * sizeof(char));

  while ((comand_str[c] != 32)||(comand_str[c] <= 48) && (comand_str[c] >= 57))
  {
    var2[c] = comand_str[c];
    c++;
  }
  c++;
  while ((comand_str[c] >= 48) && (comand_str[c] <= 57))
  {
    result = result * 10 + comand_str[c] - 48;
    c++;
  }

  comand(var2, result);
  free(var2);
}

void  setup()
{
  track = start_track;
  score = 0;
  critchance = start_crit_chance;
  critplier = start_crit_multiplier;
  crit_sound = start_crit_sound;

  Serial.begin (9600);

  mySerial.begin(9600);
  mp3_set_serial(mySerial);
  mp3_set_volume(start_vol);

  pinMode(button_pin , INPUT);
  digitalWrite(button_pin , HIGH);
  bouncer.attach(button_pin);
  bouncer.interval(5);

  myOLED.begin();
  myOLED.setFont(MediumFont);
}

void  loop()
{
  myOLED.setCursor(cursor_x, cursor_y);
  if (bouncer.update())
  {
    if (bouncer.read() == 0)
    {
      if (random(1, 100) < critchance)
      {
        score += critplier;
        myOLED.print(score);
        mp3_play(crit_sound);
        Serial.println(score);
      }
      else
      {
        score += 1;
        myOLED.print(score);
        mp3_play(track);
        Serial.println(score);
      }
    }
  }
  if (Serial.available())
  {
    var = Serial.readString();
    raw_parser(var);
  }
}
