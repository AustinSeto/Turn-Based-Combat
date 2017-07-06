/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Game;

import java.awt.Font;

/**
 *
 * @author Austin Seto
 */
public class ActionLog {

    String[] actionLog;
    private Font font;
    private int start;
    private int length;

    /**
     * Constructor
     *
     * @param size The size of the action log
     */
    public ActionLog(int size) {
        start = 0;
        length = 0;
        actionLog = new String[size];
    }

    /**
     * Constructor
     *
     * @param size The size of the action log
     * @param font What font the action log will store for itself
     */
    public ActionLog(int size, Font font) {
        this(size);
        this.font = font;
    }

    public int end() {
        return trueIndex(start + length);
    }

    private int trueIndex(int givenIndex) {
        return givenIndex % actionLog.length;
    }

    private String item(int index) {
        return (actionLog[trueIndex(index)]);
    }

    /**
     * Adds an item to the action log
     *
     * @param item The item to add to the log
     */
    public void add(String item) {
        if (length >= actionLog.length - 1) {
            remove();
        }
        actionLog[end()] = item;
        length += 1;
    }

    public void remove() {
        start += 1;
        length -= 1;
    }

    public String[] getLog() {
        String[] log = new String[length];
        for (int c = 0; c < log.length; c++) {
            log[c] = item(start + c);
        }
        return log;
    }

    public void setFont(Font newFont) {
        font = newFont;
    }

    public Font getFont() {
        return this.font;
    }
}
