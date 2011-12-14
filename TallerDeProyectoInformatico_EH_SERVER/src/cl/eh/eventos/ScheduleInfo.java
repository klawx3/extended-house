/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.eventos;

/**
 *
 * @author Usuario
 */
public class ScheduleInfo {
    
    public static final int ON = 2;
    public static final int OFF = 3;
    public static final int SWITCH = 4;
    
    private long millisLeft;
    private char arduinoSignal; // relee
    private int arduinoSignalDispNumber;
    private int estadACambiar;
    private boolean isGoingToBeErased;

    public ScheduleInfo(long millisLeft, char arduinoSignal, int arduinoSignalDispNumber, int estadACambiar, boolean isGoingToBeErased) {
        this.millisLeft = millisLeft;
        this.arduinoSignal = arduinoSignal;
        this.arduinoSignalDispNumber = arduinoSignalDispNumber;
        this.estadACambiar = estadACambiar;
        this.isGoingToBeErased = isGoingToBeErased;
    }

    public int getArduinoSignalDispNumber() {
        return arduinoSignalDispNumber;
    }
    
    public char getArduinoSignal() {
        return arduinoSignal;
    }

    public int getEstadACambiar() {
        return estadACambiar;
    }

    public boolean isIsGoingToBeErased() {
        return isGoingToBeErased;
    }

    public long getMillisLeft() {
        return millisLeft;
    }
    

    
}
