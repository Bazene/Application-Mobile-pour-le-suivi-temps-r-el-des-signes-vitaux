// INCLUDES LIBRAIRIES
#include <SoftwareSerial.h>
#include <Wire.h> // pour la communication I2C
#include "MAX30105.h" // pour le capteur lui même
#include "heartRate.h" // qui contient des fonctions pour calculer la fréquence cardiaque

// OBJETS
MAX30105 particleSensor ; //instance de la classe MAX30105 qui sera utilisée pour interagir avec le capteur.
 
// VARIABLES
#define BT_RX 1 // Broche RX du module Bluetooth HC-05
#define BT_TX 0 // Broche TX du module Bluetooth HC-05
#define LM35 A1 //broche pour le capteur de la température

int LM35_value;
float pulse_sensor_value;
float temperature_enC; // Variable pour stocker la température en degrés Celsius

int count = 0; // Conteur du battement de la fréquence cardiaque
const byte RATE_SIZE = 4; //taille de la liste; Augmentez cette valeur pour obtenir une plus grande moyenne. 4 est une bonne valeur.
byte rates[RATE_SIZE]; //Array of heart rates
byte rateSpot = 0; //index pour suivre la position dans l'array
long lastBeat = 0; //Time at which the last beat occurred
long irValue ;
float beatsPerMinute; //pour stocker la fréquence cardiaque instantanée
int beatAvg; //la moyenne des fréquences cardiaques

SoftwareSerial bluetooth(BT_RX, BT_TX); // Bluetooth module will be connected here


//********************************************************************************************************************************
//PROGRAMME QUI S'EXECUTE EN PREMIER ET UNE SEULE FOIS
void setup() {
  //INITIALISATION
  
  Serial.begin(9600); //Initialise la communication série avec une vitesse de transmission de 9600 bauds
  bluetooth.begin(9600);
  
  //Tente d'initialiser le capteur MAX30102 avec la méthode begin de l'objet particleSensor
  //Use default I2C port, 400kHz speed
  if (!particleSensor.begin(Wire, I2C_SPEED_FAST)) {
    Serial.println("MAX30102wasnotfound"); 
    while (1);//Entre dans une boucle infinie, bloquant l'exécution de la suite du programme
  }
  
  particleSensor.setup(); //Configure sensor with default settings 
  particleSensor.setPulseAmplitudeRed(0x0A); //Turn Red LED to low to indicate sensor is running (Régle l'amplitude du signal du LED rouge du capteur. En l'occurrence, 0x0A (10 en décimal) indique une amplitude faible).
  particleSensor.setPulseAmplitudeGreen(0); //Turn off Green LED
}
//********************************************************************************************************************************


//********************************************************************************************************************************
void send_data() {
  // For serial Monitor
  Serial.print("Temp"); Serial.print(temperature_enC);  
  Serial.print(",IR"); Serial.print(irValue); Serial.print(",BPM"); Serial.print(beatsPerMinute); Serial.print(",Avg_BPM"); Serial.print(beatAvg); Serial.print("\n"); 

  // For Bluetooth Module
  //  bluetooth.print("Temp"); bluetooth.print(","); bluetooth.print(temperature_enC); bluetooth.write('\n');  // Ajoute un retour à la ligne pour indiquer la fin des données;
//  delay(10000);
}
//********************************************************************************************************************************


//********************************************************************************************************************************
void temperature_corporelle() {
  // LECTURE DE LA VALEUR DE LA TEMPERATURE 
  LM35_value = analogRead(LM35);// Lecture de la valeur analogique du capteur LM35
  float voltage = LM35_value * (5.0 / 1023.0); // Conversion de la valeur lue en tension (5V est la tension de référence de l'Arduino)
  temperature_enC = (voltage * 100.0); // Calcul de la température en degrés Celsius
}
//********************************************************************************************************************************


//********************************************************************************************************************************
// FONCTION POUR LE CALCUL DE LA FREQUENCE CARDIAQUE
void heart_rate() {
  irValue = particleSensor.getIR(); //mesure de la valeur infrarouge (irValue) à partir du capteur MAX30105 en utilisant la méthode getIR()
  
  if (checkForBeat(irValue) == true) { //vérifie s'il y a un battement cardiaque détecté
    long delta = millis() - lastBeat; //calcul du temps écoulé depuis le dernier battement cardiaque
    lastBeat = millis(); // le nouveau temps devient le temps du dernier battement cardiaque (millis donne le temps écoulé depuis le démarrage de la carte Arduino)
    
    beatsPerMinute = 60 / (delta / 1000.0); //calcul de la fréquence cardiaque instantanée, en prenant l'inverse de la période entre deux battements cardiaques, convertie en minutes.
    
    if (beatsPerMinute < 255 && beatsPerMinute > 20){ 
      rates[rateSpot++] = (byte)beatsPerMinute; //stockage de la fréquence cardiaque instantanée dans le tableau rates à l'index rateSpot, puis incrémente rateSpot
      rateSpot %= RATE_SIZE; //assure que l'index rateSpot reste dans les limites de l'array rates (Si rateSpot atteint la taille de l'array (RATE_SIZE), il est réinitialisé à zéro, créant ainsi une sorte de boucle ou une rotation dans l'array)

      //calcule également la moyenne des fréquences cardiaques si la fréquence cardiaque est comprise entre 20 et 255 battements par minute.
      beatAvg = 0; // initialise la moyenne des fréquences cardiaques
      for (byte x = 0 ; x < RATE_SIZE ; x++) {
        beatAvg += rates[x]; //on effectue la somme des éléments de la liste
      }
      beatAvg /= RATE_SIZE; // division par la taille de l'array afin d'avoir la valeur moyenne
    }
  }

}
//********************************************************************************************************************************


//********************************************************************************************************************************
// PROGRAMME QUI S'EXECUTE INDEFINIMENT APRES L'EXECUTION DU PROGRAMME SETUP()
void loop() {
  //CALCUL DE LA TEMPERATURE CORPORELLE
  temperature_corporelle();
  
  //CALCUL DE LA FREQUENCE CARDIAQUE
  heart_rate();

  //ENVOIE DES DONNEES (DISPLAY DATA)
    send_data();
}
//********************************************************************************************************************************
