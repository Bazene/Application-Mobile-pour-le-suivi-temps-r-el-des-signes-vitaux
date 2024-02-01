// Mesure de la température en utilisant le LM35

int LM35 = A1; //broche pour le capteur de la température
int LM35_value;
float temperature_enC; // Variable pour stocker la température en degrés Celsius


void setup() {
  Serial.begin(9600);
}

void loop() {
  LM35_value = analogRead(LM35);// Lecture de la valeur analogique du capteur LM35
  Serial.print("Valeur lue par le capteur : ");
  Serial.println(LM35_value);

  
      float voltage = LM35_value * (5.0 / 1023.0); // Conversion de la valeur lue en tension (5V est la tension de référence de l'Arduino)
      temperature_enC = (voltage * 100.0); // Calcul de la température en degrés Celsius
    
      Serial.print("Temperature: "); 
      Serial.print(temperature_enC);
      Serial.println(" celc"); 

  delay(3000);
}
