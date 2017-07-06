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
public class Wizard extends Combatant {

    public Wizard(int level, String name) {
        super(level);
        this.name = name;
        this.combatantClass = "Wizard";
        this.maxHealth = new Statistic(200, 20);
        this.currentHealth = this.maxHealth.noModifiers(this.level);
        this.maxResource = new Statistic(100, 10);
        this.currentResource = this.maxResource.noModifiers(this.level);
        this.resourceRegen = new Statistic(10, 1);
        this.usesResource = true;

        this.physicalAttack = new Statistic(50, 1);
        this.physicalDefence = new Statistic(15, 1);

        this.magicalAttack = new Statistic(75, 5);
        this.magicalDefence = new Statistic(20, 1);

        this.speed = new Statistic(10, 1);
    }

    public Wizard(int level, String name, int currentHealth, int currentResource) {
        this(level, name);
        this.currentHealth = currentHealth;
        this.currentResource = currentResource;
    }
}
