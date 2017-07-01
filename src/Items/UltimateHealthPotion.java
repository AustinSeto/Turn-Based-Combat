/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

/**
 *
 * @author Austin Seto
 */
public class UltimateHealthPotion extends HealthPotion {

    public UltimateHealthPotion() {
        super("Ultimate Health Potion", "Heals all of a combatant's HP", Integer.MAX_VALUE);
    }
    
    public UltimateHealthPotion(int quantity) {
        super("Ultimate Health Potion", "Heals all of a combatant's HP", Integer.MAX_VALUE, quantity);
    }
}
