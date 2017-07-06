/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Combatants;

/**
 *
 * @author setoa
 */
public class Statistic {

    //Variables
    private final int baseAmount;
    private final double amountPerLevel;
    private int modifiers;

    /**
     * Creates this statistic
     *
     * @param baseAmount Base amount of this statistic (amount at level zero)
     * @param scalingAmount Amount of statistic gained per level
     */
    public Statistic(int baseAmount, double scalingAmount) {
        this.baseAmount = baseAmount;
        this.amountPerLevel = scalingAmount;
        this.modifiers = 0;
    }

    /**
     * Returns the amount of this statistic with modifiers.
     *
     * @param level The level of the combatant this statistic is associated with
     * @return The amount of this statistic with modifiers
     */
    public int withModifiers(int level) {
        return (int) (this.baseAmount + (this.amountPerLevel * level) + this.modifiers);
    }

    /**
     * Returns the amount of this statistic without modifiers.
     *
     * @param level The level of the combatant this statistic is associated with
     * @return The amount of this statistic without modifiers
     */
    public int noModifiers(int level) {
        return (int) (this.baseAmount + (this.amountPerLevel * level));
    }

    /**
     * Modifies this statistic by the input amount. Negative to decrease,
     * positive to increase. Modifiers add up and last until removed.
     *
     * @param amount Amount to modify this statistic by
     */
    public void modify(int amount) {
        this.modifiers += amount;
    }

    /**
     * Resets and removes all modifiers
     */
    public void resetModifiers() {
        this.modifiers = 0;
    }
}
