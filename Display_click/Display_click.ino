#include <Bounce2.h>
#include <iarduino_OLED_txt.h>

Bounce bouncer = Bounce();
iarduino_OLED_txt myOLED(0x3C);
extern uint8_t MediumFont[];
 
int i = 0;

void setup()
{
 pinMode(2 ,INPUT); // кнопка на пине 2
 digitalWrite(2 ,HIGH); // подключаем встроенный подтягивающий резистор
 bouncer .attach(2); // устанавливаем кнопку
 bouncer .interval(5); // устанавливаем параметр stable interval = 5 мс
 myOLED.begin();
 myOLED.setFont(MediumFont);
}
 
void loop()
{
 myOLED.setCursor(16,4);
 if (bouncer.update())
 {
  if (bouncer.read()==0)
  {
   i = i + 1;
   myOLED.print(i);
  }
 }
}
