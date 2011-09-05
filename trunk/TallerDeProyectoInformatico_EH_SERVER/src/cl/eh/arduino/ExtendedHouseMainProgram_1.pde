/**
 * ExtendedHouse
 * Copyright (C) <2011>  <Cristian Estay - Fidel Castro>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * DEF: 
 * _buffer{
 * es la variable que almacena una "cadena" de entrada serial. la forma de operar el la siguiente:
 * [arg1 (dispositivo),arg2 (valor 1),arg3 (valor 2)....,argn(valor n)] -> n(argumento) = MAX_BUFFER - 1.
 * }
 * NOTA:{
 * los valores de HIGH = 1 y LOW = 0...integer de entrada [48 hasta 57] reservadas para interpretacion de numeros ascii
 * }
 */
#include <stdio.h>
#include <string.h>
#include <LiquidCrystal.h>
/*START CONSTANTES*/
const short MILLISECONDS_FOR_DISPLAY     = 1200;
const short SERIAL_DATA_RATE             = 9600;
const short MAX_BUFFER                   = 5;
const short DELAY                        = 30;
const short MAX_INTERRUPTOR_LENGUETA     = 4;
const int DISPLAY_PREC_CONST             = MILLISECONDS_FOR_DISPLAY / DELAY; // osea se imprime cada los milli segundos puesto arriba
const short MAX_TMP_SENSORS              = 1;
const short MAX_LUZ_SENSORS              = 1;
const short MAX_DETECTOR_MOVIMIENTO      = 1;
/*START PIN DEF*/
const short relay_shield_data            = 3;
const short relay_shield_latch           = 4;
const short relay_shield_clock           = 5;
/**
 * INTERRUPTORES, LENGUETA
 * INPUT pin 41 Reserved
 * INPUT pin 42 Reserved
 * INPUT pin 43 Reserved
 * INPUT pin 44 Reserved
 */
const int interruptor_lengueta[MAX_INTERRUPTOR_LENGUETA] = { //DigialInput
    40,41,42,43
};

const int detector_movimiento[MAX_DETECTOR_MOVIMIENTO] = {
    36
};
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
const int lcd_led16x2[6] = { //DigialOutput
    48,49,50,51,52,53
};
//nota... no se pueden pasar arreglos const a funciones
int sensor_tmp_analg[MAX_TMP_SENSORS] ={ // AnalogInput
    0
};

int sensor_luz_analg[MAX_LUZ_SENSORS] ={ // AnalogInput
    8
};
/*START VARIABLES DE ENTORNO*/
int _buffer[MAX_BUFFER];
int _relay_shield_value = B00000000;
int _interruptor_lengueta_estado[MAX_INTERRUPTOR_LENGUETA];
int _sensor_movimiento_estado[MAX_DETECTOR_MOVIMIENTO];
char _sprintf_buffer[50];
unsigned int _display_presicion;

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
    for(int i = 0; i < MAX_DETECTOR_MOVIMIENTO;  i++){
        pinMode(detector_movimiento[i],INPUT);
    }
    pinMode(relay_shield_data ,OUTPUT);
    pinMode(relay_shield_latch,OUTPUT);
    pinMode(relay_shield_clock,OUTPUT);
    /*--------------------------------------------*/
    inputBufferClear();
    clearVector(_interruptor_lengueta_estado,MAX_INTERRUPTOR_LENGUETA,HIGH);
    clearVector(_sensor_movimiento_estado,MAX_DETECTOR_MOVIMIENTO,LOW);
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
        for(int i = 0 ; i < serial_buffer_lenght ; i++){
            _buffer[i] = Serial.read();
        }
        //printBuffer(_buffer,serial_buffer_lenght);
        switch(_buffer[0]){//aqui se ubican to2 los tipos de input asociados a una letra...
        case 'r': // relee shield
            {  
                if(_buffer[1] != NULL){
                    setValueRelayShield(isAASCIINumber(_buffer[1]),&_relay_shield_value); // pin , val
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
    updateEstadoDetectoresDeMovimiento();
    if(++_display_presicion == DISPLAY_PREC_CONST){ //muestro, imprimo todo el asd imprimible... (to2 dentro del if se imprime cada 1 seg)
        delayMicroseconds(20);
        printTMPSensor(sensor_tmp_analg,0);
        delayMicroseconds(20);
        printLUZSensor(sensor_luz_analg,0);
        _display_presicion = 0;
        //SAKAR LUEGO !!!!!!
    }
    delay(DELAY);
}

/*------------------------interruptores lengueta---------------------------*/
void updateEstadoInterruptoresLengueta(){
    for(int i = 0 ; i < MAX_INTERRUPTOR_LENGUETA  ; i++){
        int val = !digitalRead(interruptor_lengueta[i]);
        if(_interruptor_lengueta_estado[i] != val){
            delayMicroseconds(20);
            printf("ILG-%d-%d;\n",i,val);
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
        delayMicroseconds(20);
        printf("ILG-%d-%d;\n",i,_interruptor_lengueta_estado[i]);
    }
}
/*----------------------detector de movimientos------------------*/
void updateEstadoDetectoresDeMovimiento(){
    for(int i= 0; i < MAX_DETECTOR_MOVIMIENTO ; i++){
        int val = digitalRead(detector_movimiento[i]);
        if(_sensor_movimiento_estado[i] != val){
            delayMicroseconds(20);
            printf("DDM-%d-%d;\n",i,val);
        }
        _sensor_movimiento_estado[i] = val;
    }
}

/*--------------------Sensor temperatura-------------------------*/
float getCTemperatureFromTMPSensor(int pin){
    return (((analogRead(pin) * .004882814) - .5 ) * 100);
}

void printTMPSensor(int array[],int numero_sensor){
    fprintf(&uartout, "TMP-%d-%d;\n",numero_sensor,(int)getCTemperatureFromTMPSensor(array[numero_sensor]));
}

/*----------------------Sensor luz------------------------*/
int getBrilloLUZSensor(int pin){
    return constrain(map(analogRead(pin),0,900,0,255),0,255);
}

void printLUZSensor(int array[],int numero_sensor){
    printf("LUZ-%d-%d;\n",numero_sensor,getBrilloLUZSensor(array[numero_sensor]));
}

/*--------------buffer control--------------*/
void inputBufferClear(){
    for(int i = 0; i < MAX_BUFFER; i++){
        _buffer[i] = NULL;
    }
}

void printBuffer(int buffer[],int length){
    Serial.print("-(");
    Serial.print(length);
    Serial.print(")-->");
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

/*----------------------UTILIDADES DE PROGRAMA----------------------------- */

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

static int uart_putchar(char c, FILE *stream){
    Serial.write(c) ;
    return 0 ;
}



















