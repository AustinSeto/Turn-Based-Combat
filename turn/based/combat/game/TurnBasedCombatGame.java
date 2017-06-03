/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turn.based.combat.game;

import java.awt.Dimension;
import javax.swing.*;

/**
 *
 * @author Austin Seto
 */
public class TurnBasedCombatGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame game = new JFrame("Scrub Wrecker");
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setContentPane(new Game());
        game.setMinimumSize(new Dimension(1280 + 16, 720 + 38));
        game.setPreferredSize(new Dimension(1280 + 16, 720 + 38));
        game.setResizable(false);
        game.pack();
        game.setVisible(true);
    }

}
