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
public class LargeHealthPotion extends HealthPotion {

    public LargeHealthPotion() {
        super("Large Health Potion", "Heals 750 HP", 750);
    }
    
    public LargeHealthPotion(int quantity) {
        super("Large Health Potion", "Heals 750 HP", 750, quantity);
    }
}
