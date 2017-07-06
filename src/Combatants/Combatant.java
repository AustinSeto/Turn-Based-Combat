/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Combatants;

import src.SpecialMoves.SpecialMove;

/**
 * Abstract parent class for all combatants
 *
 * @author Austin Seto
 */
public abstract class Combatant {

    //Level, name
    int level;
    public String name;
    public String combatantClass;

    //Has this combatant moved?
    public boolean hasMoved;

    //Statistics
    public int currentHealth;
    Statistic maxHealth;
    boolean usesResource;
    public int currentResource;
    Statistic maxResource;
    Statistic resourceRegen;
    public Statistic physicalAttack;
    public Statistic physicalDefence;
    public Statistic magicalAttack;
    public Statistic magicalDefence;
    public Statistic speed;

    //Status Condtions
    /**
     * 0 = poison, 1 = burn, 2 = vulnerable, 3 = resist, 4 = exhaust, 5 =
     * regeneration
     */
    public StatusCondition[] statusConditions = new StatusCondition[6];

    //Special Moves
    public SpecialMove[] specialMoves;
    String AIBehaviour;

    //Below are the constructors
    /**
     * Super basic constructor, only sets proper overwrites on status conditions
     */
    public Combatant() {
        name = "Unnamed Combatant";
        combatantClass = "Unknown Combatant";

        hasMoved = false;
        statusConditions[0] = new StatusCondition("Poison", true);
        statusConditions[1] = new StatusCondition("Burn", true);
        statusConditions[2] = new StatusCondition("Vulnerable", false);
        statusConditions[3] = new StatusCondition("Resist", false);
        statusConditions[4] = new StatusCondition("Exhaust", false);
        statusConditions[5] = new StatusCondition("Regen", true);

        level = 1;
        maxHealth = new Statistic(100, 1);
        currentHealth = maxHealth.noModifiers(level);
        usesResource = true;
        maxResource = new Statistic(100, 1);
        currentResource = maxResource.noModifiers(level);
        resourceRegen = new Statistic(5, 1);
        physicalAttack = new Statistic(10, 1);
        physicalDefence = new Statistic(10, 1);
        magicalAttack = new Statistic(10, 10);
        magicalDefence = new Statistic(10, 1);
        speed = new Statistic(10, 1);

        AIBehaviour = "basic attack";

        specialMoves = new SpecialMove[0];
    }

    /**
     * Basic Constructor, creates this combatant with given level
     *
     * @param level The level of this combatant
     */
    public Combatant(int level) {
        this();
        this.level = level;
    }

    //Below are functions to retrieve statistics
    /**
     * Gets current level
     *
     * @return This combatant's level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Returns whether or not this combatant is dead (has zero or less health)
     *
     * @return True if this combatant is dead, false otherwise
     */
    public boolean isDead() {
        return currentHealth < 1;
    }

    /**
     * Gets max health
     *
     * @return Maximum health amount
     */
    public int getMaxHealth() {
        return maxHealth.noModifiers(level);
    }

    /**
     * Gets percentage representing how much health is left
     *
     * @return Percentage of max health remaining
     */
    public double getHealthPercentage() {
        return ((double) (currentHealth) / (double) (this.getMaxHealth())) * 100.0;
    }

    /**
     *
     * @return If this Combatant uses a resource
     */
    public boolean usesResource() {
        return usesResource;
    }

    /**
     * Gets current resource amount
     *
     * @return Current resource amount, will be negative one if no resource used
     */
    public int getCurrentResource() {
        if (this.usesResource) {
            return currentResource;
        } else {
            return -1;
        }
    }

    /**
     * Gets maximum resource amount
     *
     * @return Maximum resource amount, will be negative one if no resource used
     */
    public int getMaxResource() {
        if (this.usesResource) {
            return maxResource.noModifiers(level);
        } else {
            return -1;
        }
    }

    /**
     * Gets percentage representing how much resource is left
     *
     * @return Percentage of max resource available, zero if no resource used
     */
    public double getResourcePercentage() {
        if (this.usesResource) {
            return ((double) (currentResource) / (double) (this.getMaxResource())) * 100.0;
        } else {
            return 0;
        }
    }

    /**
     * Returns the amount by which physical damage to this combatant should be
     * multiplied due to its physical defence.
     *
     * This is determined with the following formula:
     *
     * physicalDamageMultiplier = 100/(100+physicalDefence) if physical defence
     * is greater than or equal to zero
     *
     * physicalDamageMultiplier = (physicalDefence-100)/(-100) if physical
     * defence is less than zero
     *
     * Final value is multiplied by 0.5 if the resist status condition is active
     * and multiplied by 2.0 if the vulnerable status condition is active.
     *
     * @return The amount by which physical damage hitting this combatant should
     * be multiplied by
     */
    public double physicalDamageMultiplier() {
        double physicalDamageMultiplier;
        int physicalDefences;
        physicalDefences = this.physicalDefence.withModifiers(level);
        if (physicalDefences >= 0) {
            physicalDamageMultiplier = 100.0 / (100.0 + physicalDefences);
        } else {
            physicalDamageMultiplier = (physicalDefences - 100.0) / (-100.0);
        }
        if (statusConditions[2].isActive()) {
            //Vulnerability
            physicalDamageMultiplier *= 2.0;
        }
        if (statusConditions[3].isActive()) {
            //Resist
            physicalDamageMultiplier *= 0.5;
        }
        return physicalDamageMultiplier;
    }

    /**
     * Returns the amount by which magical damage to this combatant should be
     * multiplied due to its magical defence.
     *
     * This is determined with the following formula:
     *
     * magicalDamageMultiplier = 2^(-magicalDefence/100)
     *
     * @return The amount by which magical damage hitting this combatant should
     * be multiplied by
     */
    public double magicalDamageMultiplier() {
        double magicalDamageMultiplier;
        int magicDefence;
        magicDefence = this.magicalDefence.withModifiers(level);
        if (magicDefence >= 0) {
            magicalDamageMultiplier = 100.0 / (100.0 + magicDefence);
        } else {
            magicalDamageMultiplier = (magicDefence - 100.0) / (-100.0);
        }
        if (statusConditions[2].isActive()) {
            //Vulnerability
            magicalDamageMultiplier *= 2.0;
        }
        if (statusConditions[3].isActive()) {
            //Resist
            magicalDamageMultiplier *= 0.5;
        }
        return magicalDamageMultiplier;
    }

    //Below are combat based functions
    /**
     * Function to be called when turn is over. Performs all end of turn
     * functions including but not limited to updating status effects.
     */
    public void endTurn() {
        StatusCondition poison = statusConditions[0];
        StatusCondition burn = statusConditions[1];
        StatusCondition regen = statusConditions[5];
        //Health and resource regeneration
        if (100 - this.getHealthPercentage() < regen.strength()) {
            currentHealth = this.getMaxHealth();
        } else {
            currentHealth += regen.strength() * (this.getMaxHealth() / 100);
        }
        if (this.usesResource) {
            if (currentResource + resourceRegen.withModifiers(level) >= maxResource.noModifiers(level)) {
                currentResource = maxResource.withModifiers(level);
            } else {
                currentResource += resourceRegen.withModifiers(level);
            }
        }
        //Poison and Burn Damage
        currentHealth -= (poison.strength() * (this.getMaxHealth() / 100))
                * this.magicalDamageMultiplier();
        currentHealth -= (burn.strength() * (this.getMaxHealth() / 100))
                * this.physicalDamageMultiplier();
        //Reduce duration of status conditions
        for (int c = 0; c < statusConditions.length; c++) {
            statusConditions[c].reduceDuration(1);
        }
        //This combatant has moved, its turn is complete
        hasMoved = true;
    }

    /**
     * What this combatant does when the AI obtains control of it.
     *
     * @return A string that describes what this combatant does. Possible
     * strings: basic attack, physical special attack, magical special attack,
     * buff, debuff
     */
    public String AIBehaviour() {
        return AIBehaviour;
    }

    /**
     * Performs a basic attack on the given target. The returned Combatant is
     * the target after having taken damage. Basic attack deal physical damage
     * and have zero base damage, but scale with physicalAttack at a ratio of 1
     * physicalAttack = 1 damage.
     *
     * @param target The target of the basic attack.
     * @return The target after having taken damage
     */
    public Combatant basicAttack(Combatant target) {
        int damage;
        damage = (int) (this.physicalAttack.withModifiers(this.level) * target.physicalDamageMultiplier());
        if (statusConditions[4].isActive()) {
            damage *= 0.5;
        }
        target.currentHealth -= damage;
        return target;
    }

    /**
     * How many special moves this combatant has
     *
     * @return The number of special moves this combatant has
     */
    public int numberOfSpecialMoves() {
        return specialMoves.length;
    }

    /**
     * Uses a special move on the target. Select special move using name.
     *
     * @param specialMove The index of the special move to use
     * @param target The target of the special move
     * @return The target after the special move was used on the target
     */
    public Combatant usedSpecialMove(int specialMove, Combatant target) {
        boolean exhausted;
        exhausted = this.statusConditions[4].isActive();
        switch (this.specialMoves[specialMove].scalingStat()) {
            case SpecialMove.PHYSICAL_ATTACK_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.physicalAttack.withModifiers(level), exhausted);
                break;
            case SpecialMove.MAGICAL_ATTACK_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.magicalAttack.withModifiers(level), exhausted);
                break;
            case SpecialMove.MAX_HP_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.maxHealth.noModifiers(level), exhausted);
                break;
            case SpecialMove.CURRENT_HP_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.currentHealth, exhausted);
                break;
            case SpecialMove.MAX_RESOURCE_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.maxResource.noModifiers(level), exhausted);
                break;
            case SpecialMove.CURRENT_RESOURCE_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.currentResource, exhausted);
                break;
            case SpecialMove.PHYSICAL_DEFENCE_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.physicalDefence.withModifiers(level), exhausted);
                break;
            case SpecialMove.MAGICAL_DEFENCE_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.magicalDefence.withModifiers(level), exhausted);
                break;
            case SpecialMove.SPEED_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        this.speed.withModifiers(level), exhausted);
                break;
            case SpecialMove.TARGET_MAX_HP_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        target.maxHealth.noModifiers(level), exhausted);
                break;
            case SpecialMove.TARGET_CURRENT_HP_SCALING:
                target = this.specialMoves[specialMove].usedOn(target,
                        target.currentHealth, exhausted);
                break;
            default:
                target = this.specialMoves[specialMove].usedOn(target, 0, exhausted);
        }
        if (this.usesResource() && specialMoves[specialMove].resourceCost() > 0) {
            this.currentResource -= specialMoves[specialMove].resourceCost();
        }
        return target;
    }
}
