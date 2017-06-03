/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Combatants;

import SpecialMoves.*;

/**
 *
 * @author setoa
 */
public class Knight extends Combatant {

    public Knight(int level, String name) {
        super(level);
        this.name = name;
        this.combatantClass = "Knight";
        this.maxHealth = new Statistic(250, 25);
        this.currentHealth = this.maxHealth.noModifiers(this.level);
        this.usesResource = false;

        this.physicalAttack = new Statistic(50, 1.5);
        this.physicalDefence = new Statistic(50, 5);

        this.magicalAttack = new Statistic(10, 0);
        this.magicalDefence = new Statistic(50, 5);

        this.speed = new Statistic(9, 0.5);

        this.specialMoves = new SpecialMove[1];
        this.specialMoves[0] = new Protect();

        this.AIBehaviour = "knight";
    }

    public Knight(int level, String name, int currentHealth) {
        this(level, name);
        this.currentHealth = currentHealth;
    }
}
