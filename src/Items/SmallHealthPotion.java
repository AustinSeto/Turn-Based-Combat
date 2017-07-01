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
public class SmallHealthPotion extends HealthPotion {

    public SmallHealthPotion() {
        super("Small Health Potion", "Restores 100 HP", 100);
    }
    
    public SmallHealthPotion(int quantity) {
        super("Small Health Potion", "Restores 100 HP", 100, quantity);
    }
}
