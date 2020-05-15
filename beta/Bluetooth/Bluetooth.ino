int incomingByte = 0;   // для хранения полученного байта
 
void setup() {
    Serial.begin(9600); // устанавливаем последовательное соединение
}
 
void loop() {
    if (Serial.available() > 0) {  //если есть доступные данные
        // считываем байт
        incomingByte = Serial.read();
 
        // отсылаем то, что получили
        Serial.print("I received: ");
        Serial.println(1);
        Serial.println(incomingByte, DEC);
    }
}
