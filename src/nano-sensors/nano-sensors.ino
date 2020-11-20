#include <SoftwareSerial.h>
#include <ArduinoJson.h>
#include "DHT.h"

// Temperature and Humidity Sensor Settings
#define DHTPIN 4
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

// MQ7 Settings
int MQ7Pin = A0;
int MQ7;

// MQ3 Settings
int MQ3Pin = A1;
int MQ3;

SoftwareSerial s(5, 6);

void setup()
{
    s.begin(9600);
    Serial.begin(9600);

    dht.begin();
}

void loop()
{
    // Read Humidity
    float h = dht.readHumidity();
    // Read temperature as Celsius (the default)
    float tempC = dht.readTemperature();
    // Read temperature as Fahrenheit (isFahrenheit = true)
    float tempF = dht.readTemperature(true);

    if (isnan(h) || isnan(tempC) || isnan(tempF))
    {
        return;
    }
    Serial.print("Humidity: ");
    Serial.print(h);
    Serial.print(" %\t");
    Serial.print("Temperature: ");
    Serial.print(tempC);
    Serial.print(" *C ");
    Serial.print(tempF);
    Serial.println(" *F\t");

    MQ7 = analogRead(MQ7Pin);
    MQ3 = analogRead(MQ3Pin);

    Serial.println(MQ7);
    Serial.println(MQ3);

    // Convert the data into JSON format and send to NodeMCU
    const size_t capacity = JSON_OBJECT_SIZE(5);
    DynamicJsonDocument doc(capacity);
    doc["tempC"] = tempC;
    doc["tempF"] = tempF;
    doc["humidity"] = h;
    doc["MQ7"] = MQ7;
    doc["MQ3"] = MQ3;

    serializeJson(doc, s);

    delay(2000);
}
