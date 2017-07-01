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
 * @author setoa
 */
public abstract class Item {

    public final String name;
    public final String description;
    SpecialMove effect;
    int quantity;
    boolean intendedForAlly;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.quantity = 0;
    }

    public boolean isIntendedForAlly() {
        return intendedForAlly;
    }
    
    public int quantity() {
        return quantity;
    }
    
    /**
     * Modifies quantity of this item
     *
     * @param amount Amount to modify quantity by
     */
    public void modify(int amount) {
        quantity += amount;
    }

    public Combatant usedOn(Combatant target) {
        target = effect.usedOn(target, -1, false);
        quantity -= 1;
        return target;
    }
}
