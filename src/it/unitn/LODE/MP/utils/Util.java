/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.LODE.MP.utils;

import java.awt.Toolkit;

/**
 *
 * @author ronchet
 */
public class Util {

    public static void sleep(float sec) {
        try {
            Thread.sleep((int) (sec * 1000));
        } catch (InterruptedException ex) {
        }

    }

    public static void sleep_msec(float msec) {
        try {
            Thread.sleep((int) (msec));
        } catch (InterruptedException ex) {
        }

    }

    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }
}
