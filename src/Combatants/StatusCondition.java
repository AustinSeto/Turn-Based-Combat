/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Combatants;

/**
 *
 * @author setoa
 */
public class StatusCondition {

    private int duration;
    private int strength;
    private final boolean overwrite;
    public final String name;

    /**
     * Basic Constructor. Creates this status condition. However it will be
     * inactive upon creation.
     *
     * @param name Name of status condition
     * @param overwrite Whether or not to completely replace duration with new
     * duration when reapplied. If false, status condition will only change if
     * reapplied with a duration longer than the current remaining duration.
     */
    public StatusCondition(String name, boolean overwrite) {
        this.overwrite = overwrite;
        this.duration = 0;
        this.strength = 0;
        this.name = name;
    }

    /**
     * Detailed Constructor. Creates this status condition as already active
     * with given duration and strength.
     *
     * @param name Name of status condition
     * @param overwrite
     * @param duration
     * @param strength
     */
    public StatusCondition(String name, boolean overwrite, int duration, int strength) {
        this(name, overwrite);
        this.duration = duration;
        this.strength = strength;
    }

    /**
     * Returns whether this status condition is active or not. Status condition
     * is active if both duration and strength are above zero.
     *
     * @return Boolean stating whether or not this status condition is active
     */
    public boolean isActive() {
        return duration > 0 && strength > 0;
    }

    /**
     * Returns strength of this status condition if it is active.
     *
     * @return The strength of this status condition
     */
    public int strength() {
        if (this.isActive()) {
            return strength;
        } else {
            return 0;
        }
    }

    /**
     * Returns the remaining duration on this status condition
     *
     * @return The remaining duration on this status condition
     */
    public int duration() {
        if (duration > 0) {
            return duration;
        } else {
            return 0;
        }
    }

    /**
     * Applies this status condition or reapplies it
     *
     * @param newDuration New duration for this status condition
     * @param newStrength New strength for this status condition
     */
    public void apply(int newDuration, int newStrength) {
        if (overwrite == false && newDuration > duration) {
            duration = newDuration;
            strength = 1;
        } else if (overwrite) {
            duration = newDuration;
            strength = newStrength;
        }
    }

    /**
     * Removes or cleanses this status condition
     */
    public void remove() {
        duration = 0;
        strength = 0;
    }

    /**
     * Reduces the duration of this status condition by the given amount.
     *
     * @param reductionAmount Amount to reduce the duration of this status
     * condition by.
     */
    public void reduceDuration(int reductionAmount) {
        duration -= reductionAmount;
    }
}
