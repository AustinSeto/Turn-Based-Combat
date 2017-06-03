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
public abstract class SpecialMove {

    public final String name;
    public final String description;

    //Normal Effects or variables
    final int baseEffect;
    final double scalingEffect;
    final int stat;
    boolean heal;
    boolean aoe;
    private int resourceCost;

    //Constants that refer to scaling stats
    public static final int PHYSICAL_ATTACK_SCALING = 0;
    public static final int MAGICAL_ATTACK_SCALING = 1;
    public static final int MAX_HP_SCALING = 2;
    public static final int CURRENT_HP_SCALING = 3;
    public static final int MAX_RESOURCE_SCALING = 4;
    public static final int CURRENT_RESOURCE_SCALING = 5;
    public static final int PHYSICAL_DEFENCE_SCALING = 6;
    public static final int MAGICAL_DEFENCE_SCALING = 7;
    public static final int SPEED_SCALING = 8;
    public static final int TARGET_MAX_HP_SCALING = 9;
    public static final int TARGET_CURRENT_HP_SCALING = 10;

    public static final int NO_SCALING = -1;

    /**
     * Creates this special move. Basic constructor.
     *
     * @param name The name of this SpecialMove
     * @param description The description of what this SpecialMove does
     * @param baseEffect The base amount of damage or healing this special move
     * would do
     * @param scalingEffect The amount of damage or healing this move gains per
     * point of relevant Statistic
     * @param stat The Statistic that this SpecialMove scales off of magical
     * damage. Anything else is true damage.
     */
    public SpecialMove(String name, String description, int baseEffect, double scalingEffect,
            int stat) {
        this.name = name;
        this.description = description;
        this.baseEffect = baseEffect;
        this.scalingEffect = scalingEffect;
        this.stat = stat;
        this.resourceCost = 0;
        this.heal = false;
    }

    /**
     * Creates this special move with the given resource cost.
     *
     * @param name The name of this SpecialMove
     * @param description A description of what this SpecialMove does
     * @param baseEffect The base amount of damage or healing this special move
     * would do
     * @param scalingEffect The amount of damage or healing this move gains per
     * point of relevant Statistic
     * @param stat The Statistic that this SpecialMove scales off of
     * @param cost The amount of resource this SpecialMove costs
     */
    public SpecialMove(String name, String description, int baseEffect, double scalingEffect,
            int stat, int cost) {
        this(name, description, baseEffect, scalingEffect, stat);
        this.resourceCost = cost;
    }

    public boolean isHeal() {
        return heal;
    }

    public boolean isAoe() {
        return this.aoe;
    }

    public int baseEffect() {
        return baseEffect;
    }

    /**
     * @return How much the effect of this special move increases per point of
     * the relevant scaling stat
     */
    public double scalingRatio() {
        return scalingEffect;
    }

    /**
     * What stat this special move scales off of
     *
     * @return 0 = Physical Attack, 1 = Magical Attack, 2 = Max HP, 3 = Current
     * HP, 4 = Max Resource, 5 = Current Resource, 6 = Physical Defence, 7 =
     * Magical Defence, 8 = Speed, 9 = Target Max HP, 10 = Target Current HP,
     * anything else = no scaling
     */
    public int scalingStat() {
        return stat;
    }

    /**
     * How much does this special move cost?
     *
     * @return The resource cost of this special move
     */
    public int resourceCost() {
        return resourceCost;
    }

    /**
     * Uses this special move
     *
     * @param target The target of this special move
     * @param stat The statistic this special move scales off of
     * @param exhaust Whether or not the user is exhausted
     * @return The target after the special move has been used on them
     */
    public abstract Combatant usedOn(Combatant target, int stat, boolean exhaust);

}
