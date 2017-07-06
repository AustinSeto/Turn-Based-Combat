/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Combatants;

/**
 *
 * @author Austin Seto
 */
public class TestCombatant extends Combatant {
    public TestCombatant(int maxHealth, int maxResource, int resourceRegen, int physicalAttack, int magicalAttack, int physicalDefence, int magicalDefence, int speed) {
        
        
        this.level = 1;
        
        this.maxHealth = new Statistic(maxHealth, 0);
        this.maxResource = new Statistic(maxResource, 0);
        
    }
}
