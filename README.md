# SparkDev Robotics

<!--- 
## Team

**Lead:** [Fernando Barranco](https://github.com/FJBarranco)\
**Co-lead:** [Franklin Abreu](https://github.com/frahz)\
**Team-Member:** [John Marcial](https://github.com/Junzino)\
**Team-member:** [Militza Mercado-Rogers](https://github.com/MilRog)\
**Team-Member:** Anthony Velazquez\
**Team-Member:** [Javier Fernandez](https://github.com/theogcrafted)\
**Team-Member:** Natalie Rodriguez
-->

## Project Summary

The purpose of this project is to build a Search-and-Rescue Robot that can monitor the conditions of the location where it is deployed and detect the gases in the nearby area. The robot should be able to provide a live video feed of the area where it's at and the sensor data to an android app that is connected to the robot. There will also be a drone that will provide a bird's eye view over the location, it should be able to follow the ground robot by itself with the use of tracking algorithms, and it should be able to land and connect with the ground robot with the use of mechanical claws that will hold it in place.

## Programming Languages

* Python: The programming language use on the Raspberry Pi for the camera and communication with the server.
* Java/Kotlin: The programming languange used for the Android app that will be used to read the sensor data and control the robot's movement.
* Arduino/C++: The programming language used for the microcontroller that will read the sensor data and send it to the server.

## Software

* [Arduino IDE](https://www.arduino.cc/en/software)
* [Android Studio](https://developer.android.com/studio)
* [Trello](https://trello.com/)
* [Pubnub](https://www.pubnub.com/)
* [VS Code](https://code.visualstudio.com/)
* [Google Sketchup](https://www.sketchup.com/)

## Hardware

* [Raspberry Pi 4](https://www.raspberrypi.org/products/raspberry-pi-4-model-b/)
* [Drone](https://www.contixo.com/f30-drone)
* 3D Printer
* Microcontrollers
  * 2x [ESP8266 NodeMCU](https://www.amazon.com/HiLetgo-Internet-Development-Wireless-Micropython/dp/B010O1G1ES)
  * 2x [Arduino Nano](https://www.theengineeringprojects.com/2018/06/introduction-to-arduino-nano.html)
* Sensors
  * [MQ7](https://www.sparkfun.com/products/9403)
  * [MQ3](https://wiki.seeedstudio.com/Grove-Gas_Sensor-MQ3/)
  * [Temperature/Humidity](https://wiki.seeedstudio.com/Grove-TemperatureAndHumidity_Sensor/)

## Notes

### Microcontrollers API keys

Make sure to have a Secrets.h file in your Arduino libraries folder that looks like this:

```c

#define SSID_F "xxxxxxxx"
#define PASSWORD_F "xxxxxxxx"
#define PUB_KEY "pub-c-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
#define SUB_KEY "sub-c-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"

```

1. Go to your Arduino Library folder.
2. Inside that folder make a new folder called "Secrets".
3. Go inside this new folder and make a new file called ```Secrets.h```.
4. Open this file in a text editor and fill in the the values for each key.

### Android App API Keys

How to hide API keys in Android Studio:

1. Make a file called ```apikey.properties``` in the root directory of the Android app.
2. Add these values to the file:

```c

PUB_KEY="pub-c-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
SUB_KEY="sub-c-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"

```
