/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import Combatants.Combatant;
import SpecialMoves.SpecialMove;

/**
 *
 * @author Austin Seto
 */
public abstract class HealthPotion extends Item {
    final int healAmount;

    public HealthPotion(String name, String description, int healAmount) {
        super(name, description);
        this.intendedForAlly = true;
        this.healAmount = healAmount;
        this.effect = new Heal();
    }
    
    public HealthPotion(String name, String description, int healAmount, int quantity) {
        this(name, description, healAmount);
        this.quantity = quantity;
    }
    
    private class Heal extends SpecialMove {
        private Heal() {
            super("Heal", "Restores 100 HP", healAmount, 0, -1);
        }

        @Override
        public Combatant usedOn(Combatant target, int stat, boolean exhaust) {
            if (target.currentHealth > target.getMaxHealth() - healAmount) {
                target.currentHealth = target.getMaxHealth();
            } else {
                target.currentHealth += healAmount;
            }
            return target;
        }
        
    }
}
