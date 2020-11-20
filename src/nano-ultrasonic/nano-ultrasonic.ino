#include <Servo.h>
#include <NewPing.h>

// Servo Motor Settings
#define servoPin 9
#define NUM_ANGLES 13
#define servoSpeed 50
#define minimumDistance 10
int angleIndex = 0;
int angleStep = 1;
int sensorAngle[NUM_ANGLES] = {45, 55, 65, 75, 85, 95, 105, 115, 125, 135, 145, 155, 165};
Servo myservo;

// Ultrasonic Sensor Settings
#define trigPin 10
#define echoPin 11
#define MAX_DISTANCE 200
NewPing sonar(trigPin, echoPin, MAX_DISTANCE);

float distance;

// Data Transfer Pin from Arduino Nano to ModeMCU
const int nodemcu = 8;

void setup()
{
    Serial.begin(9600);

    myservo.attach(servoPin); // attaches the servo on pin 9 to the servo object
    myservo.write(90);

    pinMode(trigPin, OUTPUT);
    pinMode(echoPin, INPUT);
    pinMode(nodemcu, OUTPUT);
}

void loop()
{
    //Get distance from Ultrasonic Sensor
    distance = sonar.ping_cm();
    Serial.println(distance);

    // If the distance is less than 10 cm, it will send a signal to the NodeMCU
    if (distance <= minimumDistance)
    {
        Serial.println("high");
        digitalWrite(nodemcu, HIGH);
    }
    else
    {
        Serial.println("low");
        digitalWrite(nodemcu, LOW);
    }
    moveServo();
    delay(250);
}

void moveServo()
{
    // Moves the Servo by an increment of 10 degrees every 50 ms
    angleIndex += angleStep;

    if (angleIndex >= (NUM_ANGLES - 1))
        angleStep = -1;
    else if (angleIndex <= 0)
        angleStep = 1;

    myservo.write(sensorAngle[angleIndex]);
    delay(servoSpeed);
}
