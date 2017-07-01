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
public class CrippleShot extends SpecialMove {

    public CrippleShot() {
        super("Crippling Shot", "A crippling shot that reduces the damage of the target's next attack.", 
                0, 0.4, 0);
    }

    @Override
    public Combatant usedOn(Combatant target, int stat, boolean exhaust) {
        int damage;
        damage = (int) (baseEffect + (scalingEffect * stat));
        damage = (int) (damage * target.physicalDamageMultiplier());
        if (exhaust) {
            damage *= 0.5;
        }
        target.currentHealth -= damage;
        target.statusConditions[4].apply(1, 1);
        return target;
    }
}
