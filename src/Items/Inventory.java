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
public class Inventory {

    Item[] items;

    public Inventory() {
        items = new Item[4];
        
        addNewUniqueItem(new SmallHealthPotion());
        addNewUniqueItem(new MediumHealthPotion());
        addNewUniqueItem(new LargeHealthPotion());
        addNewUniqueItem(new UltimateHealthPotion());
    }
    
    /**
     * Increases quantity of item at index by quantity given
     *
     * @param index Index of item to modify quantity of
     * @param quantity How much to modify the quantity of said item
     */
    public void addItem(int index, int quantity) {
        items[index].quantity += quantity;
    }

    private void addNewUniqueItem(Item item) {
        boolean spotFound = false;
        int index;

        for (index = 0; index < items.length; index++) {
            if (items[index] == null) {
                spotFound = true;
                items[index] = item;
                break;
            }
        }

        //If no spot found, extend array
        if (!spotFound) {
            Item[] newInventory = new Item[items.length + 1];
            System.arraycopy(items, 0, newInventory, 0, items.length);
            newInventory[newInventory.length - 1] = item;
            items = newInventory;
        }
    }

    /**
     * Returns an item
     *
     * @param index Index of the item to return
     * @return An item
     */
    public Item item(int index) {
        if (index < items.length) {
            return items[index];
        } else {
            return null;
        }
    }
    
    public boolean isEmpty() {
        boolean empty = true;
        for (int c = 0; c < items.length; c ++) {
            if (items[c].quantity() > 0) {
                empty = false;
            }
        }
        return empty;
    }

    /**
     * Returns the size of the inventory. Size is the number of unique items
     * with a quantity greater than zero.
     *
     * @return Number of unique items with a quantity > 0
     */
    public int size() {
        int size = 0;
        for (int c = 0; c < items.length; c++) {
            if (items[c].quantity > 0) {
                size += 1;
            }
        }
        return size;
    }
}
