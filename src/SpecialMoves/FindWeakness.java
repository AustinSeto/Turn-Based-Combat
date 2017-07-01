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
public class FindWeakness extends SpecialMove {

    public FindWeakness() {
        super("Find Weakness", "Pinpoints weak spots on the target, increasing the damage they take for one turn.", 
                0, 0, -1);
    }

    @Override
    public Combatant usedOn(Combatant target, int stat, boolean exhaust) {
        target.statusConditions[2].apply(1, 0);
        return target;
    }

}
