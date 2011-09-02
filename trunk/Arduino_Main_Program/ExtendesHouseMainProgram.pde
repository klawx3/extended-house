/**
 * DEF: 
 * _buffer{
 * es la variable que almacena una "cadena" de entrada serial.
 * la forma de operar el la siguiente:
 * [arg1 (dispositivo),arg2 (valor 1),arg3 (valor 2)....,argn(valor n)] -> n(argumento) = MAX_BUFFER - 1.
 * }
 * 
 * 
 * 
 * 
 * NOTA:
 * {
 * los valores de HIGH = 1 y LOW = 0...
 * integer de entrada [48 hasta 57] reservadas para interpretacion de numeros ascii
 * }
 */
#include <stdio.h>
#include <string.h>
#include <LiquidCrystal.h>
/*START CONSTANTES*/
const int SERIAL_DATA_RATE = 9600;
const int MAX_BUFFER = 5;
const int DELAY      = 30;
const int MAX_INTERRUPTOR_LENGUETA = 4;
const int DISPLAY_PREC_CONST = 1000 / DELAY; // osea se imprime cada 1 seg cada sensor
/*START PIN DEF*/
const int relay_shield_data  = 3;
const int relay_shield_latch = 4;
const int relay_shield_clock = 5;
/**
 * INPUT pin 41 Reserved
 * INPUT pin 42 Reserved
 * INPUT pin 43 Reserved
 * INPUT pin 44 Reserved
 */
const int interruptor_lengueta[MAX_INTERRUPTOR_LENGUETA] = {
    40,41,42,43};
/**
 * LCD RS pin to digital pin 48
 * LCD Enable pin to digital pin 49
 * LCD D4 pin to digital pin 50
 * LCD D5 pin to digital pin 51
 * LCD D6 pin to digital pin 52
 * LCD D7 pin to digital pin 53
 * LCD R/W pin to ground
 * 10K resistor:
 * ends to +5V and ground
 * wiper to LCD VO pin (pin 3)
 */
const int lcd_led16x2[6] = {
    48,49,50,51,52,53};

const int sensor_tmp_anlg    = 0;
const int sensor_luz_anlg    = 8;
/*START VARIABLES DE ENTORNO*/
int _buffer[MAX_BUFFER];
int _relay_shield_value = B00000000;
int _interruptor_lengueta_estado[MAX_INTERRUPTOR_LENGUETA];
char _sprintf_buffer[50];
unsigned int _display_presicion;

static int uart_putchar(char c, FILE *stream){
    Serial.write(c) ;
    return 0 ;
}

/*START OBJECTOS*/
LiquidCrystal pantalla_lcd(lcd_led16x2[0], lcd_led16x2[1],
lcd_led16x2[2], lcd_led16x2[3], lcd_led16x2[4],
lcd_led16x2[5]);

static FILE uartout = {
    0};
/*---------SETUP-----------*/
void setup(){
    /*---------------pin modes---------------------*/
    for(int i = 0; i < MAX_INTERRUPTOR_LENGUETA ; i++){
        pinMode(interruptor_lengueta[i],INPUT);
    }
    for(int i = 0 ; i < 6 ; i++){
        pinMode(lcd_led16x2[i],OUTPUT);
    }
    pinMode(relay_shield_data ,OUTPUT);
    pinMode(relay_shield_latch,OUTPUT);
    pinMode(relay_shield_clock,OUTPUT);
    /*--------------------------------------------*/
    inputBufferClear();
    clearVector(_interruptor_lengueta_estado,MAX_INTERRUPTOR_LENGUETA,LOW);
    _relay_shield_value=~_relay_shield_value;
    shiftOutInput(_relay_shield_value,relay_shield_data,relay_shield_clock,relay_shield_latch);
    fdev_setup_stream(&uartout, uart_putchar, NULL, _FDEV_SETUP_WRITE);
    stdout = &uartout;
    pantalla_lcd.begin(16, 2);
    Serial.begin(SERIAL_DATA_RATE);
}
/*---------------------------------loop---------------------------------*/
void loop(){
        if(Serial.available() >= 2 && Serial.available() <= MAX_BUFFER){ // significa que tiene algo dentro
            int serial_buffer_lenght = Serial.available();
            printBuffer(_buffer,Serial.available());
            for(int i = 0 ; i < serial_buffer_lenght ; i++){
                _buffer[i] = Serial.read();
            }
            //printBuffer(_buffer,serial_buffer_lenght);/*despues borrar*/
            switch(_buffer[0]){//aqui se ubican to2 los tipos de input asociados a una letra...
            case 'r': // relee shield
                {  
                    if(_buffer[1] != NULL){

                        setValueRelayShield(isAASCIINumber(_buffer[1]),&_relay_shield_value); // pin , val
                        Serial.println(_relay_shield_value,BIN);
                    }
                }
            case 'd': // display
                {
                    if(_buffer[1] != NULL){
                        if(_buffer[1] == 'a'){ // muestra toda la info

                        }
                        else if(_buffer[1] == 'l'){ // muestra la info de los interruptores de lengueta
                            printEstadoIL();
                        }
                    }
                }

            }
            inputBufferClear();
            Serial.flush();
        }
    
    if(Serial.available() > 2){
        Serial.flush();
    }
    updateEstadoInterruptoresLengueta();

    if(++_display_presicion == DISPLAY_PREC_CONST){ //muestro, imprimo todo el asd imprimible... (to2 dentro del if se imprime cada 1 seg)
        printTMPSensor(sensor_tmp_anlg);
        printLUZSensor(sensor_luz_anlg);
        _display_presicion = 0;
    }
    delay(DELAY);
}

/*------------------------interruptores lengueta---------------------------*/
void updateEstadoInterruptoresLengueta(){
    for(int i = 0 ; i < MAX_INTERRUPTOR_LENGUETA  ; i++){
        int val = !digitalRead(interruptor_lengueta[i]);
        if(_interruptor_lengueta_estado[i] != val){
            printf("IL%d|%d;\n",i,val);
        }
        _interruptor_lengueta_estado[i] = val;// 1 significa que esta el circuito cerrado
    }
}
void clearVector(int vector[],int lenght,int val){
    for(int i = 0; i < lenght;i++){
        vector[i] = val;
    }
}

void printEstadoIL(){
    for(int i = 0 ; i < MAX_INTERRUPTOR_LENGUETA  ; i++){
        printf("IL%d|%d;\n",i,_interruptor_lengueta_estado[i]);
    }
}

/*--------------------Sensor temperatura-------------------------*/
float getCTemperatureFromTMPSensor(int pin){
    return (((analogRead(pin) * .004882814) - .5 ) * 100);
}

void printTMPSensor(int pin){
    Serial.print("TMP");
    Serial.print("|");
    Serial.print(getCTemperatureFromTMPSensor(pin));
    Serial.println(";");
}

/*----------------------Sensor luz------------------------*/
int getBrilloLUZSensor(int pin){
    return constrain(map(analogRead(pin),0,900,0,255),0,255);
}

void printLUZSensor(int pin){
    printf("LUZ|%d;\n",getBrilloLUZSensor(pin));
}

/*--------------buffer control--------------*/
void inputBufferClear(){
    for(int i = 0; i < MAX_BUFFER; i++){
        _buffer[i] = NULL;
    }
}

void printBuffer(int buffer[],int length){
    Serial.print("-");
    Serial.print(length);
    Serial.print("---->");
    for(int i = 0; i < length; i++){
        if(buffer[i] == NULL){
            break;
        }
        Serial.print(buffer[i]);
        Serial.print("-");
    }
    Serial.println("");
}
/*--------------------relay shield------------------------ */
void setValueRelayShield(int pin,int *relayValue){ // solo "swtchea" el estado de 1 pin
    *relayValue ^= (1 << pin);
    shiftOutInput(*relayValue,relay_shield_data,relay_shield_clock,relay_shield_latch);
}

/*------------------------------------------------------------- */

void shiftOutInput(int input,int data,int clock,int latch){
    digitalWrite(latch,LOW);
    shiftOut(data,clock,MSBFIRST,input);
    digitalWrite(latch,HIGH);
}

int isAASCIINumber(char number){
    int aux = number;
    if(aux >= 48 && aux<= 57){
        aux = aux - 48;
    }
    return aux;
}
















