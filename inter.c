#include <SoftwareSerial.h>
#include <DFPlayer_Mini_Mp3.h>
#include <Bounce2.h>
#include <iarduino_OLED_txt.h>

#define brightness 1
#define bright_difference 0.2
#define default_red_pin 9
#define default_green_pin 10
#define default_blue_pin 11
#define cursor_x 16
#define cursor_y 4
#define start_perclick 1
#define start_track 9
#define start_vol 25
#define start_crit_chance 30
#define start_crit_combo 70
#define start_crit_multiplier 2
#define start_combo_multiplier 2
#define start_crit_sound 3
#define start_combo_sound 6
#define button_pin 2
#define comand_setperclick "spcl"
#define comand_setscore "sset"
#define comand_setvolume "svol"
#define comand_setclicktrack "strc"
#define comand_setredcolor "sred"
#define comand_setgreencolor "sgrn"
#define comand_setbluecolor "sblu"
#define comand_setdifferencebright "sdbr"
#define comand_setbrightness "sbrt"
#define comand_setcrittrack "ctrc"
#define comand_setcritmultiplier "cmul"
#define comand_setcritchance "ccha"
#define comand_setcombotrack "cotr"
#define comand_setcombomultiplier "comu"
#define comand_setcombochance "coch"

SoftwareSerial mySerial(5, 6); // RX, TX
Bounce bouncer = Bounce();
iarduino_OLED_txt myOLED(0x3C);
extern uint8_t MediumFont[];

float bright;
float difBright;
int red;
int green;
int blue;
int score;
int track;
String var;
int critchance;
int critcombo;
int critplier;
int comboplier;
int crit_sound;
int combo_sound;
int perclick;

void	comand(char *comand_str, int value)
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
	if ((comand_str[0] == comand_setperclick[0]) && (comand_str[1] == comand_setperclick[1]) && (comand_str[2] == comand_setperclick[2]) && (comand_str[3] == comand_setperclick[3]))
	{
		perclick = value;
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
	if ((comand_str[0] == comand_setcombotrack[0]) && (comand_str[1] == comand_setcombotrack[1]) && (comand_str[2] == comand_setcombotrack[2]) && (comand_str[3] == comand_setcombotrack[3]))
	{
		combo_sound = value;
	}
	if ((comand_str[0] == comand_setcombomultiplier[0]) && (comand_str[1] == comand_setcombomultiplier[1]) && (comand_str[2] == comand_setcombomultiplier[2]) && (comand_str[3] == comand_setcombomultiplier[3]))
	{
		comboplier = value;
	}
	if ((comand_str[0] == comand_setcombochance[0]) && (comand_str[1] == comand_setcombochance[1]) && (comand_str[2] == comand_setcombochance[2]) && (comand_str[3] == comand_setcombochance[3]))
	{
		critcombo = value;
	}
	if ((comand_str[0] == comand_setredcolor[0]) && (comand_str[1] == comand_setredcolor[1]) && (comand_str[2] == comand_setredcolor[2]) && (comand_str[3] == comand_setredcolor[3]))
	{
		red = value;
	}
	if ((comand_str[0] == comand_setgreencolor[0]) && (comand_str[1] == comand_setgreencolor[1]) && (comand_str[2] == comand_setgreencolor[2]) && (comand_str[3] == comand_setgreencolor[3]))
	{
		green = value;
	}
	if ((comand_str[0] == comand_setbluecolor[0]) && (comand_str[1] == comand_setbluecolor[1]) && (comand_str[2] == comand_setbluecolor[2]) && (comand_str[3] == comand_setbluecolor[3]))
	{
		blue = value;
	}
	if ((comand_str[0] == comand_setdifferencebright[0]) && (comand_str[1] == comand_setdifferencebright[1]) && (comand_str[2] == comand_setdifferencebright[2]) && (comand_str[3] == comand_setdifferencebright[3]))
	{
		difBright = value;
	}
	if ((comand_str[0] == comand_setbrightness[0]) && (comand_str[1] == comand_setbrightness[1]) && (comand_str[2] == comand_setbrightness[2]) && (comand_str[3] == comand_setbrightness[3]))
	{
		bright = value;
	}
}

void	raw_parser(String comand_str)
//разделяет строку на массив символов с командой и числовое значение
{
	int c = 0;
	int result = 0;
	char *var2;

	var2 = (char*)malloc(4 * sizeof(char));

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

void setRGB(int r, int g, int b)
{
	analogWrite(default_red_pin, r);
	analogWrite(default_green_pin, g);
	analogWrite(default_blue_pin, b);
}

void	setup()
{
	bright = brightness;
	difBright = bright_difference;
	red = 0;
	green = 0;
	blue = 255;
	perclick = start_perclick;
	track = start_track;
	score = 0;
	critchance = start_crit_chance;
	critcombo = start_crit_combo;
	critplier = start_crit_multiplier;
	comboplier = start_combo_multiplier;
	crit_sound = start_crit_sound;
	combo_sound = start_combo_sound;

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

	pinMode(default_red_pin, OUTPUT);
	pinMode(default_green_pin, OUTPUT);
	pinMode(default_blue_pin, OUTPUT);
}

void	loop()
{
	myOLED.setCursor(cursor_x, cursor_y);
	setRGB(red*difBright*bright, green*difBright*bright, blue*difBright*bright);
	if (bouncer.update())
	{
		if (bouncer.read() == 0)
		{
			setRGB(red*bright, green*bright, blue*bright);
			delay(100);
			
			if ((score%10 == 0) && (random(1, 100) < critcombo))
			{
				score += comboplier;
				myOLED.print(score);
				mp3_play(combo_sound);
				Serial.println(score);
			}
			else if (random(1, 100) < critchance)
			{
				score += critplier;
				myOLED.print(score);
				mp3_play(crit_sound);
				Serial.println(score);
			}
			else
			{
				score += perclick;
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