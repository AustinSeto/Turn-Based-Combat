/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpecialMoves;

import Combatants.Combatant;

/**
 *
 * @author setoa
 */
public class Protect extends SpecialMove {

    public Protect() {
        super("Protect", "Reduces the damage an ally takes for three turns", 
                0, 0, -1);
        this.heal = true;
    }

    @Override
    public Combatant usedOn(Combatant target, int stat, boolean exhaust) {
        target.statusConditions[3].apply(3, 0);
        return target;
    }
}
