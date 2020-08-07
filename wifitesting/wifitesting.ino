#include "FirebaseESP8266.h"


#include<ESP8266WiFi.h>
#include <DHT.h>
 
#define DHTTYPE DHT11 // using DHT11 sensor for temperature sensing
const int refresh = 1; // 3 seconds refresh temperature
const char* ssid = "HMT";
const char* password = "pranshu2@";
int ledPin_Waiting = 13;  // D7
int ledPin_Connected = 4; // D2
int DHT_Pin = 15; // D8

DHT dht(DHT_Pin,DHTTYPE);
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
}

int i = 0;
void loop() {
  // put your main code here, to run repeatedly:

  Serial.println(i);
  float temp_val = dht.readTemperature();
  float temp_val_f = dht.readTemperature(true);
  float humidity = dht.readHumidity();

  if(isnan(temp_val) || isnan(temp_val_f) || isnan(humidity))
  {
    Serial.println("Error in Reading DHT11"); 
  }
  else
  {
    Serial.print("Temperature : ");
    Serial.println(temp_val);
    Serial.print("Humidity : ");
    Serial.println(humidity);
    Serial.print("Temperature *F : ");
    Serial.println(temp_val_f);
  }
  delay(refresh * 1000);
  i++;
}
