/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.eh.util;

/**
 *
 * @author Usuario
 */
public class Arreglos {

    public static int getPositonOfMaxValue(long[] e) {
        int MAX = e.length;
        long _maxNumber = 0;
        int _maxIndex = -1;

        for (int i = 0; i < MAX; i++) {
            if (i == 0) {
                _maxNumber = e[i];
                _maxIndex = i;
            } else {
                if (e[i] > _maxNumber) {
                    _maxNumber = e[i];
                    _maxIndex = i;
                }
            }
        }
        return _maxIndex;
    }

    public static int getPositonOfMinValue(long[] e) {
        int MIN = e.length;
        long _minNumber = 0;
        int _minIndex = -1;

        for (int i = 0; i < MIN; i++) {
            if (i == 0) {
                _minNumber = e[i];
                _minIndex = i;
            } else {
                if (e[i] < _minNumber) {
                    _minNumber = e[i];
                    _minIndex = i;
                }
            }
        }
        return _minIndex;
    }
}
