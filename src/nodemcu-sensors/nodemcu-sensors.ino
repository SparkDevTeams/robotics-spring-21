#include <SoftwareSerial.h>
#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#define PubNub_BASE_CLIENT WiFiClient
#define PUBNUB_DEFINE_STRSPN_AND_STRNCASECMP
#include <PubNub.h>
#include "Secrets.h"

// WiFi parameters
const char *ssid = SSID_F;
const char *password = PASSWORD_F;

// PubNub Settings
const char *channel = "sensors";
char stateBuffer[550];
WiFiClient *client;

SoftwareSerial s(12, 14);

void setup()
{
    s.begin(9600);
    Serial.begin(9600);

    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        Serial.print(".");
    }
    Serial.println("WiFi connected");

    // Print the IP address
    Serial.println(WiFi.localIP());

    PubNub.begin(PUB_KEY, SUB_KEY);
}

void report(double humidity, double tempC, double tempF, double MQ7, double MQ3)
{
    StaticJsonDocument<500> doc;
    JsonObject root = doc.to<JsonObject>();
    root["humidity"] = humidity;
    root["tempC"] = tempC;
    root["tempF"] = tempF;
    root["MQ7"] = MQ7;
    root["MQ3"] = MQ3;

    serializeJson(doc, stateBuffer, sizeof(stateBuffer));
    client = PubNub.publish(channel, stateBuffer);
    if (!client)
    {
        Serial.println("publishing error");
        return;
    }
    client->stop();
    Serial.println("Reported!");
}

void loop()
{
    // Acquire the data from the Arduino Nano, read it, and report it to the PubNub Server
    const size_t capacity = JSON_OBJECT_SIZE(5) + 40;
    DynamicJsonDocument doc(capacity);
    deserializeJson(doc, s);

    int MQ7 = doc["MQ7"];
    int MQ3 = doc["MQ3"];
    float h = doc["humidity"];
    float tempC = doc["tempC"];
    float tempF = doc["tempF"];

    if (MQ7 == 0)
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
    Serial.print("mq7: ");
    Serial.println(MQ7);
    Serial.print("mq3: ");
    Serial.println(MQ3);
    report(h, tempC, tempF, MQ7, MQ3);
}
