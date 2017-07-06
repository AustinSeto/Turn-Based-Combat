/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Items;

/**
 *
 * @author Austin Seto
 */
public class MediumHealthPotion extends HealthPotion {

    public MediumHealthPotion() {
        super("Medium Health Potion", "Heals 300 HP", 300);
    }
    
    public MediumHealthPotion(int quantity) {
        super("Medium Health Potion", "Heals 300 HP", 300, quantity);
    }
}
