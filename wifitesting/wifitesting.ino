
#include <FirebaseArduino.h>   
#include<ESP8266WiFi.h>
#include <DHT.h>
//#include<ESP8266HTTPClient.h>

#define FIREBASE_HOST "https://electronicclient-2ffc7.firebaseio.com"
#define FIREBASE_AUTH "ZPWHrsWezLc5y3RdKmNvAt7QA9VdYo9JbRaF0h4v" 
#define DHTTYPE DHT11 // using DHT11 sensor for temperature sensing
const int refresh = 1; // 3 seconds refresh temperature
const char* ssid = "HMT";
const char* password = "pranshu2@";
int ledPin_Waiting = 13;  // D7
int ledPin_Connected = 4; // D2
int Moisture_Pin = 8;
int DHT_Pin = 15; // D8
int sensor_state = 0;
boolean water_operation;
int last_state = 0;
DHT dht(DHT_Pin,DHTTYPE);

const int relay = 5; // relay pin


void setup() {
  // put your setup code here, to run once:
  
  pinMode(ledPin_Waiting,OUTPUT);
  pinMode(ledPin_Connected,OUTPUT);
  Serial.begin(115200);
  Serial.println();
  Serial.print("Wifi connecting to...");
  Serial.println(ssid);
  digitalWrite(ledPin_Waiting,HIGH);
  WiFi.begin(ssid,password);
  Serial.println();
  Serial.print("Connecting...");
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  digitalWrite(ledPin_Waiting,LOW);
  digitalWrite(ledPin_Connected,HIGH);
  
  Serial.println("Wifi Connection Successful!");
  Serial.print("NODEMCU IP Address : ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST,FIREBASE_AUTH);
}

int i = 0;
void loop()
{
  // put your main code here, to run repeatedly:


  delay(1000);
  Firebase.stream("Temperature");
  float temp_val = dht.readTemperature();
  float humidity = dht.readHumidity();
 

  if(isnan(temp_val)  || isnan(humidity))
  {
    Serial.println("Error in Reading DHT11"); 
  }
  else
  {
    Serial.print("Temperature : ");
    Serial.println(temp_val);
    Serial.print("Humidity : ");
    Serial.println(humidity);
    Firebase.pushString("temperature",String(temp_val));
    Firebase.pushString("humidity",String(humidity));
  }

  water_operation = Firebase.getBool("start");

  if(water_operation == 1){
    // water the plant as said by user
    digitalWrite(relay,HIGH);
  }else{
    // stop watering the plant
    digitalWrite(relay,LOW);
  }


  sensor_state = analogRead(8);

  if(sensor_state <= 25){
    Firebase.setBool("Water",true);
  }
  
  delay(refresh * 1000);
  
}
