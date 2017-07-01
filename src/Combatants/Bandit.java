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
public class Bandit extends Combatant {

    public Bandit(int level, String name) {
        super(level);
        this.name = name;
        this.combatantClass = "Bandit";
        this.maxHealth = new Statistic(300, 15);
        this.currentHealth = this.maxHealth.noModifiers(level);
        this.usesResource = false;

        this.physicalAttack = new Statistic(60, 3);
        this.physicalDefence = new Statistic(20, 3);

        this.magicalAttack = new Statistic(20, 1);
        this.magicalDefence = new Statistic(20, 3);

        this.speed = new Statistic(9, 0.75);

        this.AIBehaviour = "basic attack";
    }

    public Bandit(int level, String name, int currentHealth) {
        this(level, name);
        this.currentHealth = currentHealth;
    }
}
