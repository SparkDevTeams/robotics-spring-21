// Import required libraries
#include <ESP8266WiFi.h>
#define PubNub_BASE_CLIENT WiFiClient
#define PUBNUB_DEFINE_STRSPN_AND_STRNCASECMP
#include <PubNub.h>
#include <ArduinoJson.h>
#include <SimpleTimer.h>
#include "Secrets.h"

// WiFi parameters
const char *ssid = SSID_F;
const char *password = PASSWORD_F;

// PubNub Settings
const char *channel = "motors";
char stateBuffer[550];
WiFiClient *client;

// Motor Control Pins
const int IN1 = 14;
const int IN2 = 12;
const int IN3 = 13;
const int IN4 = 15;

// Sensor Pin Settings
int pinCount = 2;
int pins[] = {16, 5};

SimpleTimer timer;

void setup(void)
{
    // Start Serial
    Serial.begin(9600);

    // Connect to WiFi
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

    // Setup pins as output

    pinMode(IN1, OUTPUT);
    pinMode(IN2, OUTPUT);
    pinMode(IN3, OUTPUT);
    pinMode(IN4, OUTPUT);

    timer.setInterval(500);
}

void go()
{
    PubSubClient *pclient = PubNub.subscribe(channel);
    if (!pclient)
    {
        Serial.println("subscription error");
        delay(1000);
        return;
    }
    char buffer[64];
    size_t buflen = 0;
    while (pclient->wait_for_data())
    {
        buffer[buflen++] = pclient->read();
    }
    buffer[buflen] = 0;
    pclient->stop();
    //    Serial.println(buffer);

    if (buflen < 4)
    {
        Serial.println("Buffer length below 4");
        return;
    }

    // Parse
    const size_t bufferSize = JSON_OBJECT_SIZE(4) + 40;
    DynamicJsonDocument doc(bufferSize);

    deserializeJson(doc, buffer);
    JsonArray root = doc.as<JsonArray>();

    bool input1 = root[0]["Input1"]; // false
    bool input2 = root[0]["Input2"]; // false
    bool input3 = root[0]["Input3"]; // false
    bool input4 = root[0]["Input4"]; // false

    Serial.print("Input1: ");
    Serial.println(input1);
    Serial.print("Input2: ");
    Serial.println(input2);
    Serial.print("Input3: ");
    Serial.println(input3);
    Serial.print("Input4: ");
    Serial.println(input4);

    digitalWrite(IN1, input1);
    digitalWrite(IN2, input2);
    digitalWrite(IN3, input3);
    digitalWrite(IN4, input4);
}

void loop()
{
    if (timer.isReady())
    {
        go();
        timer.reset();
    }
}
