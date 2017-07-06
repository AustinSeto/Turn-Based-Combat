/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Combatants;

import src.SpecialMoves.*;

/**
 *
 * @author setoa
 */
public class Archer extends Combatant {

    public Archer(int level, String name) {
        super(level);
        this.name = name;
        this.combatantClass = "Archer";
        this.maxHealth = new Statistic(200, 20);
        this.currentHealth = this.maxHealth.noModifiers(this.level);
        this.usesResource = false;

        this.physicalAttack = new Statistic(80, 5);
        this.physicalDefence = new Statistic(20, 2);

        this.magicalAttack = new Statistic(50, 1);
        this.magicalDefence = new Statistic(15, 1);

        this.speed = new Statistic(10, 1);

        this.specialMoves = new SpecialMove[2];
        this.specialMoves[0] = new CrippleShot();
        this.specialMoves[1] = new FindWeakness();

        this.AIBehaviour = "basic attack";
    }

    public Archer(int level, String name, int currentHealth) {
        this(level, name);
        this.currentHealth = currentHealth;
    }
}
