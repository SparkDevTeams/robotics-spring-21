// define output pins for motors
int motor1pin1 = 4;
int motor1pin2 = 3;

// defin input control pins
int Claw_Grab = 11;
int Claw_Release = 12;

void setup()
{

    Serial.begin(9600);

    // Set output pins
    pinMode(motor1pin1, OUTPUT);
    pinMode(motor1pin2, OUTPUT);

    pinMode(Claw_Grab, INPUT);
    pinMode(Claw_Release, INPUT);

    digitalWrite(Claw_Grab, LOW);
    digitalWrite(Claw_Release, LOW);
}

void Grab()
{
    Serial.println("Grabbing");
    digitalWrite(motor1pin1, HIGH);
    digitalWrite(motor1pin2, LOW);
    delay(1500);
}

void Release()
{
    Serial.println("Releasing");
    digitalWrite(motor1pin1, LOW);
    digitalWrite(motor1pin2, HIGH);
    delay(1500);
}

void Stop()
{
    digitalWrite(motor1pin1, LOW);
    digitalWrite(motor1pin2, LOW);
}

void loop()
{
    bool grab = digitalRead(Claw_Grab);
    bool _release = digitalRead(Claw_Release);

    if (grab == HIGH)
    {
        Grab();
    }

    if (_release == HIGH)
    {
        Release();
    }

    if (grab == LOW || _release == LOW)
    {
        Stop();
    }

    delay(1000);
}
