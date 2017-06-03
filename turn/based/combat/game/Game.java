/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turn.based.combat.game;

import Combatants.*;
import Items.*;
import SpecialMoves.SpecialMove;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author setoa
 */
public class Game extends JPanel implements ActionListener {

    //Required global objects and variables
    private final Timer timer;
    private Combatant[] playerParty;
    private Combatant[] enemyParty;
    private boolean playerTurn;
    private int combatantTurn;
    private int drawFrame = 0;

    //Whoever has this value of speed takes their turn
    private int speedOfCurrentTurn;

    private boolean turnFound = false;

    //Colours
    private final Color pale = new Color(220, 220, 150);
    private final Color beige = new Color(170, 170, 100);
    //Health bar colours
    private final Color green = new Color(70, 180, 70);
    private final Color darkRed = new Color(150, 0, 0);
    //Mana Bar colours
    private final Color lightBlue = new Color(0, 160, 255);
    private final Color darkBlue = new Color(0, 50, 85);
    //Fonts
    private final Font nameFont = new Font("Times New Roman", 1, 16);

    //Items
    Inventory inventory;

    //Menu variables
    private int highlightedSelection;
    private int menuDepth;
    public static final int BASE_MOVE_SELECTION = 0;
    public static final int SPECIAL_MOVE_SELECTION = 1;
    public static final int TARGET_SELECTION = 2;
    public static final int ITEM_SELECTION = 3;

    public static final int MOVE_ATTACK = 0;
    public static final int MOVE_SPECIAL_ATTACK = 1;
    public static final int MOVE_USE_ITEM = 2;
    public static final int MOVE_WAIT = 3;
    public static final int MOVE_RUN = 4;

    //Used for selecting target for attacks
    private boolean enemiesTargetted;
    private int moveSelected; //Negative for basic attacks, else refers to index of special move or item to use
    private boolean usingItem; //True if using item, else false

    private ActionLog actionLog;

    public Game() {
        this.setBackground(pale);
        this.setMinimumSize(new Dimension(1280, 720));
        this.setPreferredSize(new Dimension(1280, 720));

        playerParty = new Combatant[5];
        enemyParty = new Combatant[5];

        menuDepth = BASE_MOVE_SELECTION;
        highlightedSelection = MOVE_ATTACK;

        playerParty[0] = new Knight(10, "FN-2187");
        playerParty[1] = new Archer(25, "Archer");

        enemyParty[0] = new Knight(10, "TR-8R");

        inventory = new Inventory();
        inventory.item(0).modify(5);
        inventory.item(1).modify(5);
        inventory.item(2).modify(5);
        inventory.item(3).modify(5);

        timer = new Timer(20, this);
        timer.setActionCommand("timer tick");

        //Actions
        Action left, right, up, down, x, z;
        left = new LeftArrow("left", "left button");
        right = new RightArrow("right", "right arrow");
        up = new UpArrow("up", "up arrow");
        down = new DownArrow("down", "down arrow");
        x = new XButton("x", "x button");
        z = new ZButton("z", "z button");

        actionLog = new ActionLog(40, new Font("Arial", 0, 15));

        //Keybindings
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LeftArrow");
        this.getActionMap().put("LeftArrow", left);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RightArrow");
        this.getActionMap().put("RightArrow", right);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UpArrow");
        this.getActionMap().put("UpArrow", up);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DownArrow");
        this.getActionMap().put("DownArrow", down);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "XButton");
        this.getActionMap().put("XButton", x);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "ZButton");
        this.getActionMap().put("ZButton", z);

        this.requestFocus();
        speedOfCurrentTurn = highestSpeed();
        timer.start();
    }

    //Graphics methods
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draws boxes
        g.setColor(Color.BLACK);
        //Border box for base move selection
        if (menuDepth == BASE_MOVE_SELECTION) {
            g.drawRect(0, (this.getHeight() / 6) * 5, this.getWidth(), this.getHeight() - (this.getHeight() / 6));
        }
        //Other misc sections
        g.drawRect(0, 0, this.getWidth() / 4, this.getHeight());
        g.drawRect(this.getWidth() - (this.getWidth() / 4), 0, this.getWidth() / 4, this.getHeight());
        //Drawing parties of combatants
        drawParty(g, playerParty, true);
        drawParty(g, enemyParty, false);
        //Drawing move selection
        drawMoveSelection(g);
    }

    private void drawImage(Graphics g, String imageName, int x, int y) {
        BufferedImage image;
        image = null;
        try {
            image = ImageIO.read(new File("images\\" + imageName));
        } catch (IOException e) {
            System.out.println("Image with file name \"" + imageName + "\" does not exist!");
        }
        g.drawImage(image, x, y, null);
    }

    //Everything below is for menus
    private void drawMoveSelection(Graphics g) {
        if (playerTurn) {
            drawTurnIndicator(g);
            switch (menuDepth) {
                case BASE_MOVE_SELECTION:
                    drawBaseMoveSelection(g);
                    printActions(g);
                    break;
                case SPECIAL_MOVE_SELECTION:
                    drawSpecialMoveSelection(g, playerParty[combatantTurn]);
                    break;
                case ITEM_SELECTION:
                    drawInventory(g);
                    break;
                case TARGET_SELECTION:
                    drawTargetSelection(g);
                    printActions(g);
                    break;
            }
        }
    }

    private void drawBaseMoveSelection(Graphics g) {
        int iconDiameter = 90;
        int verticalSpacing = 5;
        int horizontalSpacing = ((this.getWidth() / 2) - 450) / 6;
        int moveIconsX = (this.getWidth() / 4);
        int iconX;
        int moveIconsSelectedY = ((this.getHeight() / 6) * 5);
        int moveIconsUnselectedY = (this.getHeight() - verticalSpacing) - iconDiameter;
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", 0, 18));
        switch (highlightedSelection) {
            case MOVE_ATTACK:
                iconX = moveIconsX + horizontalSpacing;
                drawImage(g, "Move Icons\\Move Icon - Attack.png",
                        iconX, moveIconsSelectedY + verticalSpacing);
                g.drawString("Attack", iconX, moveIconsSelectedY + iconDiameter + verticalSpacing + g.getFont().getSize());
                break;
            case MOVE_SPECIAL_ATTACK:
                iconX = moveIconsX + horizontalSpacing + (iconDiameter + horizontalSpacing);
                drawImage(g, "Move Icons\\Move Icon - Special Move.png",
                        iconX, moveIconsSelectedY + verticalSpacing);
                g.drawString("Special Move", iconX, moveIconsSelectedY + iconDiameter + verticalSpacing + g.getFont().getSize());
                break;
            case MOVE_USE_ITEM:
                iconX = moveIconsX + horizontalSpacing + (2 * (iconDiameter + horizontalSpacing));
                drawImage(g, "Move Icons\\Move Icon - Use Item.png",
                        iconX, moveIconsSelectedY + verticalSpacing);
                g.drawString("Use Item", iconX, moveIconsSelectedY + iconDiameter + verticalSpacing + g.getFont().getSize());
                break;
            case MOVE_WAIT:
                iconX = moveIconsX + horizontalSpacing + (3 * (iconDiameter + horizontalSpacing));
                drawImage(g, "Move Icons\\Move Icon - Wait.png",
                        iconX, moveIconsSelectedY + verticalSpacing);
                g.drawString("Wait", iconX, moveIconsSelectedY + iconDiameter + verticalSpacing + g.getFont().getSize());
                break;
            case MOVE_RUN:
                iconX = moveIconsX + horizontalSpacing + (4 * (iconDiameter + horizontalSpacing));
                drawImage(g, "Move Icons\\Move Icon - Run.png",
                        iconX, moveIconsSelectedY + verticalSpacing);
                g.drawString("Run", iconX, moveIconsSelectedY + iconDiameter + verticalSpacing + g.getFont().getSize());
                break;
            default:
        }
        if (highlightedSelection != MOVE_ATTACK) {
            drawImage(g, "Move Icons\\Move Icon - Attack.png", moveIconsX + horizontalSpacing, moveIconsUnselectedY);
        }
        if (highlightedSelection != MOVE_SPECIAL_ATTACK) {
            drawImage(g, "Move Icons\\Move Icon - Special Move.png",
                    moveIconsX + horizontalSpacing + (iconDiameter + horizontalSpacing), moveIconsUnselectedY);
        }
        if (highlightedSelection != MOVE_USE_ITEM) {
            drawImage(g, "Move Icons\\Move Icon - Use Item.png",
                    moveIconsX + horizontalSpacing + (2 * (iconDiameter + horizontalSpacing)), moveIconsUnselectedY);
        }
        if (highlightedSelection != MOVE_WAIT) {
            drawImage(g, "Move Icons\\Move Icon - Wait.png",
                    moveIconsX + horizontalSpacing + (3 * (iconDiameter + horizontalSpacing)), moveIconsUnselectedY);
        }
        if (highlightedSelection != MOVE_RUN) {
            drawImage(g, "Move Icons\\Move Icon - Run.png",
                    moveIconsX + horizontalSpacing + (4 * (iconDiameter + horizontalSpacing)), moveIconsUnselectedY);
        }
    }

    private void drawSpecialMoveSelection(Graphics g, Combatant combatant) {
        int x;
        int y;
        String resourceCostText;
        String scalingStat;
        //Background of special move selection
        g.setColor(beige);
        g.fillRect(this.getWidth() / 4, 0, this.getWidth() / 2, this.getHeight());
        //Dividing boxes
        g.setColor(Color.BLACK);
        g.drawRect(this.getWidth() / 4, 0, this.getWidth() / 2, this.getHeight());
        //Selection box
        if (combatant.numberOfSpecialMoves() > 0) {
            x = this.getWidth() / 4;
            g.setColor(Color.BLACK);
            for (int c = 0; c < combatant.specialMoves.length; c++) {
                y = (this.getHeight() / 12) * c;

                drawSpecialMove(g, combatant.specialMoves[c], x, y);
            }

            //Selection Box
            g.setColor(lightBlue);
            g.drawRect(this.getWidth() / 4, (this.getHeight() / 12) * highlightedSelection,
                    this.getWidth() / 2, this.getHeight() / 12);
        } else {
            //If no special moves, no selection box. Tell user.
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", 0, 30));
            g.drawString("This combatant has no special moves!", (this.getWidth() / 4) + 60, 50);
        }
    }

    private void drawSpecialMove(Graphics g, SpecialMove specialMove, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, this.getWidth() / 2, this.getHeight() / 12);

        //Name
        g.setFont(nameFont);
        g.drawString(specialMove.name, x + 5, y + nameFont.getSize());

        //Cost if applicable
        if (specialMove.resourceCost() <= 0) {
            g.drawString("Free to use", x + 565, y + g.getFont().getSize());
        } else {
            String resourceCostText;
            resourceCostText = String.format("Costs %d mana", specialMove.resourceCost());
            g.drawString(resourceCostText,
                    (this.getWidth() - (this.getWidth() / 4)) - (resourceCostText.length() * (g.getFont().getSize() / 2)),
                    y + g.getFont().getSize());
        }

        //Description of special move
        g.setFont(new Font("Arial", 0, 15));
        g.drawString(specialMove.description,
                x + 5, y + (g.getFont().getSize() * 2));

        //Scaling description
        String scalingStat;
        switch (specialMove.scalingStat()) {
            case SpecialMove.PHYSICAL_ATTACK_SCALING:
                scalingStat = "your physical attack";
                break;
            case SpecialMove.MAGICAL_ATTACK_SCALING:
                scalingStat = "your magical attack";
                break;
            case SpecialMove.MAX_HP_SCALING:
                scalingStat = "your max HP";
                break;
            case SpecialMove.CURRENT_HP_SCALING:
                scalingStat = "your current HP";
                break;
            case SpecialMove.MAX_RESOURCE_SCALING:
                scalingStat = "your max mana";
                break;
            case SpecialMove.CURRENT_RESOURCE_SCALING:
                scalingStat = "your current mana";
                break;
            case SpecialMove.PHYSICAL_DEFENCE_SCALING:
                scalingStat = "your physical defence";
                break;
            case SpecialMove.MAGICAL_DEFENCE_SCALING:
                scalingStat = "your magical defence";
                break;
            case SpecialMove.SPEED_SCALING:
                scalingStat = "your speed";
                break;
            case SpecialMove.TARGET_MAX_HP_SCALING:
                scalingStat = "the target's max HP";
                break;
            case SpecialMove.TARGET_CURRENT_HP_SCALING:
                scalingStat = "the target's current HP";
                break;
            default:
                scalingStat = "nothing";
        }
        String effect;
        if (specialMove.baseEffect() > 0 || specialMove.scalingRatio() > 0) {
            if (specialMove.isHeal()) {
                effect = "Heal = ";
            } else {
                effect = "Damage = ";
            }
            if (specialMove.baseEffect() > 0) {
                effect += specialMove.baseEffect();
                if (specialMove.scalingStat() >= 0) {
                    effect += " + ";
                }
            }
            if (specialMove.scalingStat() >= 0) {
                effect += String.format("%s multiplied by %s", scalingStat, specialMove.scalingRatio());
            }
            g.drawString(effect, x + 5, y + (g.getFont().getSize() * 3));
        }
    }

    private void drawTargetSelection(Graphics g) {
        int arrowX;
        int arrowY;
        int arrowWidth = 30;
        int arrowHeight = 20;
        int horizontalSpacing = 10;
        int partyLength;
        String imageName;
        if (enemiesTargetted) {
            partyLength = enemyParty.length;
            imageName = "Selection Arrow - Right.png";
            arrowX = (3 * (this.getWidth() / 4)) - arrowWidth - horizontalSpacing;
        } else {
            partyLength = playerParty.length;
            imageName = "Selection Arrow - Left.png";
            arrowX = (this.getWidth() / 4) + horizontalSpacing;
        }
        if (!usingItem && moveSelected >= 0) {
            if (playerParty[combatantTurn].specialMoves[moveSelected].isAoe()) {
                for (int c = 0; c < partyLength; c++) {
                    arrowY = ((this.getHeight() / 12) + ((this.getHeight() / 6) * c)) - (arrowHeight / 2);
                    drawImage(g, imageName, arrowX, arrowY);
                }
            } else {
                arrowY = ((this.getHeight() / 12) + ((this.getHeight() / 6) * highlightedSelection)) - (arrowHeight / 2);
                drawImage(g, imageName, arrowX, arrowY);
            }
        } else {
            arrowY = ((this.getHeight() / 12) + ((this.getHeight() / 6) * highlightedSelection)) - (arrowHeight / 2);
            drawImage(g, imageName, arrowX, arrowY);
        }
    }

    private void drawTurnIndicator(Graphics g) {
        if (playerTurn) {
            drawImage(g, "Turn Indicator\\" + (((drawFrame / 15) % 2) + 1) + ".png", 285, 5 + (combatantTurn * (this.getHeight() / 6)));
        }
    }

    private void printActions(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(actionLog.getFont());
        String[] log = actionLog.getLog();
        int y = ((this.getHeight() / 6) * 5) - 5;
        for (int c = log.length - 1; c >= 0; c--) {
            g.drawString(log[c], (this.getWidth() / 4) + 50, y);
            y -= (g.getFont().getSize() + 1);
        }
    }

    private void drawParty(Graphics g, Combatant[] party, boolean player) {
        int xValue;
        if (player) {
            xValue = 0;
        } else {
            xValue = this.getWidth() - (this.getWidth() / 4);
        }
        for (int c = 0; c < party.length; c++) {
            drawCombatant(g, party[c], player, xValue, c * (this.getHeight() / 6));
        }
    }

    private void drawCombatant(Graphics g, Combatant combatant, boolean player, int x, int y) {
        int barX = x + 100;
        int hpY = y + 40;
        int mpY = hpY + (this.getHeight() / 60);
        g.setColor(beige);
        g.fillRect(x, y, this.getWidth() / 4, this.getHeight() / 6);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, this.getWidth() / 4, this.getHeight() / 6);
        if (combatant != null) {
            //Name, Combatant Class, and level
            g.setColor(Color.BLACK);
            g.setFont(nameFont);
            g.drawString(combatant.name, x + 10, y + nameFont.getSize());
            g.drawString("Level " + combatant.getLevel() + " " + combatant.combatantClass,
                    x + 10, y + (g.getFont().getSize() * 2));
            //Health and resource bars
            g.setColor(darkRed);
            g.fillRect(barX, hpY, this.getWidth() / 8, this.getHeight() / 60);
            g.setColor(green);
            g.fillRect(barX, hpY, (int) ((this.getWidth() / 800.0)
                    * combatant.getHealthPercentage()), this.getHeight() / 60);
            if (player) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", 0, 10));
                g.drawString(combatant.currentHealth + "/" + combatant.getMaxHealth() + " HP",
                        x + 150, hpY + g.getFont().getSize());
            }
            if (combatant.usesResource()) {
                g.setColor(darkBlue);
                g.fillRect(barX, mpY, this.getWidth() / 8, this.getHeight() / 60);
                g.setColor(lightBlue);
                g.fillRect(barX, mpY, (int) ((this.getWidth() / 800.0)
                        * combatant.getResourcePercentage()), this.getHeight() / 60);
                if (player) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", 0, 10));
                    g.drawString(combatant.currentResource + "/" + combatant.getMaxResource() + " MP",
                            x + 150, mpY + g.getFont().getSize());
                }
            }
            //Status conditions
            drawStatusConditions(g, combatant, x + 100, y + 75);
        }
    }

    private void drawStatusConditions(Graphics g, Combatant combatant, int x, int y) {
        int statusConditionToDraw = (drawFrame / 75) % 6;
        String imageName;
        imageName = "Status Conditions\\" + combatant.statusConditions[statusConditionToDraw].name + ".png";
        drawImage(g, imageName, x, y);
        g.setFont(new Font("Arial", 0, 12));
        g.setColor(Color.BLACK);
        g.drawString(combatant.statusConditions[statusConditionToDraw].name, x + 35, y);
        if (combatant.statusConditions[statusConditionToDraw].isActive()) {
            g.drawString("Duration: " + combatant.statusConditions[statusConditionToDraw].duration() + " turns",
                    x + 35, y + g.getFont().getSize());
            if (statusConditionToDraw == 0 || statusConditionToDraw == 1 || statusConditionToDraw == 5) {
                g.drawString("Strength: " + combatant.statusConditions[statusConditionToDraw].strength() + "%",
                        x + 35, y + (g.getFont().getSize() * 2));
            }
        } else {
            g.drawString("Not in effect", x + 35, y + g.getFont().getSize());
        }
    }

    private void drawInventory(Graphics g) {
        //Background of item selection
        g.setColor(beige);
        g.fillRect(this.getWidth() / 4, 0, this.getWidth() / 2, this.getHeight());
        //Dividing boxes
        g.setColor(Color.BLACK);
        g.drawRect(this.getWidth() / 4, 0, this.getWidth() / 2, this.getHeight());

        //Where to start drawing the inventory
        int start;
        if (highlightedSelection < 12) {
            start = 0;
        } else {
            start = highlightedSelection - 12;
        }

        //Drawing inventory items, up to 12 at a time
        int y = 0;
        for (int c = start; c < start + 12; c++) {
            if (inventory.item(c) != null) {
                if (inventory.item(c).quantity() > 0) {
                    drawItem(g, inventory.item(c), this.getWidth() / 4, y);
                }
            }
            y += this.getHeight() / 12;
        }

        //Drawing Selection Box
        if (inventory.isEmpty()) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", 0, 30));
            g.drawString("Inventory is empty!", (this.getWidth() / 4) + 200, 50);
        } else {
            g.setColor(lightBlue);
            g.drawRect(this.getWidth() / 4, (this.getHeight() / 12) * (highlightedSelection - start),
                    this.getWidth() / 2, this.getHeight() / 12);
        }
    }

    private void drawItem(Graphics g, Item item, int x, int y) {
        g.setColor(Color.BLACK);
        int height = this.getHeight() / 12;
        int width = this.getWidth() / 2;
        g.drawRect(x, y, width, height);
        g.setFont(nameFont);

        //Writing title - includes quantity
        String title;
        title = String.format("%s %s", item.name, "X " + item.quantity());
        g.drawString(title,
                x + 5, y + nameFont.getSize());

        //Writing description
        g.setFont(new Font("Arial", 0, 15));
        g.drawString(item.description, x + 5, y + (nameFont.getSize() * 2));
    }

    //Combat related methods
    private int highestSpeed() {
        int highestSpeed = 0;
        for (int c = 0; c < playerParty.length; c++) {
            if (playerParty[c] != null) {
                if (playerParty[c].speed.withModifiers(playerParty[c].getLevel()) > highestSpeed) {
                    highestSpeed = playerParty[c].speed.withModifiers(playerParty[c].getLevel());
                }
            }
        }
        for (int c = 0; c < enemyParty.length; c++) {
            if (enemyParty[c] != null) {
                if (enemyParty[c].speed.withModifiers(enemyParty[c].getLevel()) > highestSpeed) {
                    highestSpeed = enemyParty[c].speed.withModifiers(enemyParty[c].getLevel());
                }
            }
        }
        return highestSpeed;
    }

    private void checkCombatants() {
        for (int c = 0; c < enemyParty.length; c++) {
            if (enemyParty[c] != null) {
                if (enemyParty[c].isDead()) {
                    actionLog.add(enemyParty[c].name + " has died!");
                    enemyParty[c] = null;
                }
            }
        }
        for (int c = 0; c < playerParty.length; c++) {
            if (playerParty[c] != null) {
                if (playerParty[c].isDead()) {
                    actionLog.add(playerParty[c].name + " has died!");
                    playerParty[c] = null;
                }
            }
        }
    }

    /**
     * Finds the squishiest combatant in an array of combatants
     *
     * @param party The array of combatants
     * @param damageType The damage type. Zero is physical damage, one is
     * magical damage, and anything else is true damage
     * @return The index of the squishiest combatant in the array
     */
    public int findSquishy(Combatant[] party, int damageType) {
        int index = 0;
        int highestEffectiveHealth = Integer.MAX_VALUE;
        for (int c = 0; c < party.length; c++) {
            if (party[c] != null) {
                if (effectiveHealth(party[c], damageType) < highestEffectiveHealth) {
                    highestEffectiveHealth = effectiveHealth(party[c], damageType);
                    index = c;
                }
            }
        }
        return index;
    }

    /**
     * Finds out which combatant in a party has the highest total offensive
     * stats. This is a sum of their physical attack, magical attack, and speed.
     *
     * @param party The party to search
     * @return The index of the "highest damage dealer"
     */
    public int findDamager(Combatant[] party) {
        int index = 0;
        int highestDamage = 0;
        int currentDamage = 0;
        for (int c = 0; c < party.length; c++) {
            currentDamage = 0;
            if (party[c] != null) {
                currentDamage += party[c].physicalAttack.withModifiers(party[c].getLevel());
                currentDamage += party[c].magicalAttack.withModifiers(party[c].getLevel());
                currentDamage += party[c].speed.withModifiers(party[c].getLevel());
            }
            if (currentDamage > highestDamage) {
                highestDamage = currentDamage;
                index = c;
            }
        }
        return index;
    }

    /**
     * Determines the effective health of a combatant. Effective health being
     * the amount of a given type of damage it would take to kill the combatant
     *
     * @param combatant The combatant to check the effective health of
     * @param damageType The damage type used to calculate effective health.
     * Zero is physical damage, one is magical damage, anything else is true
     * damage.
     * @return The effective health of the combatant
     */
    public int effectiveHealth(Combatant combatant, int damageType) {
        int effectiveHealth;
        switch (damageType) {
            case 0:
                effectiveHealth = (int) (combatant.currentHealth / combatant.physicalDamageMultiplier());
                break;
            case 1:
                effectiveHealth = (int) (combatant.currentHealth / combatant.magicalDamageMultiplier());
                break;
            default:
                effectiveHealth = combatant.currentHealth;
        }
        return effectiveHealth;
    }

    private void aiActions() {
        int targettedCombatant;
        switch (enemyParty[combatantTurn].AIBehaviour().toLowerCase()) {
            case "basic attack":
                targettedCombatant = findSquishy(playerParty, 0);
                playerParty[targettedCombatant]
                        = enemyParty[combatantTurn].basicAttack(playerParty[targettedCombatant]);
                actionLog.add(enemyParty[combatantTurn].name + " has basic attacked "
                        + playerParty[targettedCombatant].name);
                break;
            case "knight":
                targettedCombatant = findSquishy(enemyParty, 2);
                if (!enemyParty[targettedCombatant].statusConditions[3].isActive()) {
                    enemyParty[targettedCombatant]
                            = enemyParty[combatantTurn].specialMoves[0].usedOn(enemyParty[targettedCombatant],
                                    0, false);
                    actionLog.add(enemyParty[combatantTurn].name + " has used Protect on "
                            + enemyParty[targettedCombatant].name);
                } else {
                    targettedCombatant = findDamager(playerParty);
                    playerParty[targettedCombatant]
                            = enemyParty[combatantTurn].basicAttack(playerParty[targettedCombatant]);
                    actionLog.add(enemyParty[combatantTurn].name + " has basic attacked "
                            + playerParty[targettedCombatant].name);
                }
                break;
            default:
                actionLog.add(enemyParty[combatantTurn].name + " has waited.");
        }
        enemyParty[combatantTurn].endTurn();
        turnFound = false;
        checkCombatants();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand().toLowerCase()) {
            case "timer tick":
                topLoop:
                while (turnFound != true) {
                    for (int c = 0; c < playerParty.length; c++) {
                        if (playerParty[c] != null) {
                            if (playerParty[c].hasMoved == false
                                    && playerParty[c].speed.withModifiers(playerParty[c].getLevel())
                                    == speedOfCurrentTurn) {
                                turnFound = true;
                                playerTurn = true;
                                combatantTurn = c;
                                actionLog.add(String.format("%s has their turn!", playerParty[c].name));
                                break topLoop;
                            }
                        }
                    }
                    for (int c = 0; c < enemyParty.length; c++) {
                        if (enemyParty[c] != null) {
                            if (enemyParty[c].hasMoved == false
                                    && enemyParty[c].speed.withModifiers(enemyParty[c].getLevel())
                                    == speedOfCurrentTurn) {
                                turnFound = true;
                                playerTurn = false;
                                combatantTurn = c;
                                actionLog.add(String.format("%s has their turn!", enemyParty[c].name));
                                break topLoop;
                            }
                        }
                    }
                    if (turnFound == false) {
                        speedOfCurrentTurn -= 1;
                        if (speedOfCurrentTurn < 0) {
                            speedOfCurrentTurn = highestSpeed();
                            for (int c = 0; c < playerParty.length; c++) {
                                if (playerParty[c] != null) {
                                    playerParty[c].hasMoved = false;
                                }
                            }
                            for (int c = 0; c < enemyParty.length; c++) {
                                if (enemyParty[c] != null) {
                                    enemyParty[c].hasMoved = false;
                                }
                            }
                        }
                    }
                }
                if (playerTurn == false) {
                    aiActions();
                }
                drawFrame += 1;
                repaint();
                break;
            default:
                System.out.println(e.getActionCommand());
        }
    }

    //Actions
    private class LeftArrow extends AbstractAction {

        public LeftArrow(String name, String desc) {
            super(name);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playerTurn) {
                switch (menuDepth) {
                    case BASE_MOVE_SELECTION:
                        highlightedSelection -= 1;
                        if (highlightedSelection < 0) {
                            highlightedSelection = 4;
                        }
                        break;
                    case TARGET_SELECTION:
                        enemiesTargetted = false;
                        break;
                }
            }
        }
    }

    private class RightArrow extends AbstractAction {

        public RightArrow(String name, String desc) {
            super(name);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playerTurn) {
                switch (menuDepth) {
                    case BASE_MOVE_SELECTION:
                        highlightedSelection += 1;
                        if (highlightedSelection > 4) {
                            highlightedSelection = 0;
                        }
                        break;
                    case TARGET_SELECTION:
                        enemiesTargetted = true;
                        break;
                }
            }
        }
    }

    private class UpArrow extends AbstractAction {

        public UpArrow(String name, String desc) {
            super(name);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playerTurn) {
                switch (menuDepth) {
                    case SPECIAL_MOVE_SELECTION:
                        highlightedSelection -= 1;
                        if (highlightedSelection < 0) {
                            highlightedSelection = playerParty[combatantTurn].numberOfSpecialMoves() - 1;
                        }
                        break;
                    case TARGET_SELECTION:
                        highlightedSelection -= 1;
                        if (highlightedSelection < 0) {
                            highlightedSelection = 4;
                        }
                        break;
                    case ITEM_SELECTION:
                        highlightedSelection -= 1;
                        if (highlightedSelection < 0) {
                            highlightedSelection = inventory.size() - 1;
                        }
                        break;
                    default:
                }
            }
        }
    }

    private class DownArrow extends AbstractAction {

        public DownArrow(String name, String desc) {
            super(name);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playerTurn) {
                switch (menuDepth) {
                    case SPECIAL_MOVE_SELECTION:
                        highlightedSelection += 1;
                        if (highlightedSelection >= playerParty[combatantTurn].numberOfSpecialMoves()) {
                            highlightedSelection = 0;
                        }
                        break;
                    case TARGET_SELECTION:
                        highlightedSelection += 1;
                        if (highlightedSelection > 4) {
                            highlightedSelection = 0;
                        }
                        break;
                    case ITEM_SELECTION:
                        highlightedSelection += 1;
                        if (highlightedSelection >= inventory.size()) {
                            highlightedSelection = 0;
                        }
                        break;
                    default:
                }
            }
        }
    }

    //Cancel Button
    private class ZButton extends AbstractAction {

        public ZButton(String name, String desc) {
            super(name);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (menuDepth > BASE_MOVE_SELECTION) {
                menuDepth = BASE_MOVE_SELECTION;
                highlightedSelection = 0;
            }
        }
    }

    //Select Button
    private class XButton extends AbstractAction {

        public XButton(String name, String desc) {
            super(name);
            putValue(SHORT_DESCRIPTION, desc);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playerTurn) {
                switch (menuDepth) {
                    case BASE_MOVE_SELECTION:
                        selectAction();
                        break;
                    case SPECIAL_MOVE_SELECTION:
                        moveSelected = highlightedSelection;
                        usingItem = false;
                        menuDepth = TARGET_SELECTION;
                        highlightedSelection = 0;
                        if (playerParty[combatantTurn].specialMoves[moveSelected].isHeal()) {
                            enemiesTargetted = false;
                        } else {
                            enemiesTargetted = true;
                        }
                        break;
                    case ITEM_SELECTION:
                        moveSelected = highlightedSelection;
                        usingItem = true;
                        menuDepth = TARGET_SELECTION;
                        highlightedSelection = 0;
                        if (inventory.item(moveSelected).isIntendedForAlly()) {
                            enemiesTargetted = false;
                        } else {
                            enemiesTargetted = true;
                        }
                        break;
                    case TARGET_SELECTION:
                        if ((enemiesTargetted && enemyParty[highlightedSelection] == null)
                                || (enemiesTargetted == false && playerParty[highlightedSelection] == null)) {
                            //Checking to ensure valid target is selected
                            actionLog.add("No valid target selected!");
                        } else {
                            if (!usingItem && moveSelected >= 0) {
                                //Special move used
                                if (playerParty[combatantTurn].specialMoves[moveSelected].isAoe()) {
                                    attackMade(enemiesTargetted, -1, playerParty[combatantTurn], moveSelected);
                                } else {
                                    attackMade(enemiesTargetted, highlightedSelection, playerParty[combatantTurn], moveSelected);
                                }
                            } else if (usingItem) {
                                //Item used
                                attackMade(enemiesTargetted, highlightedSelection, null, moveSelected);
                            } else {
                                //Basic attack
                                attackMade(enemiesTargetted, highlightedSelection, playerParty[combatantTurn], -1);
                            }
                            actionMade();
                            checkCombatants();
                        }
                        break;
                }
            }
        }

        //Method for selecting action
        private void selectAction() {
            switch (highlightedSelection) {
                case MOVE_ATTACK:
                    highlightedSelection = 0;
                    moveSelected = -1;
                    usingItem = false;
                    enemiesTargetted = true;
                    menuDepth = TARGET_SELECTION;
                    moveSelected = -1;
                    break;
                case MOVE_SPECIAL_ATTACK:
                    highlightedSelection = 0;
                    menuDepth = SPECIAL_MOVE_SELECTION;
                    break;
                case MOVE_USE_ITEM:
                    highlightedSelection = 0;
                    menuDepth = ITEM_SELECTION;
                    break;
                case MOVE_WAIT:
                    actionLog.add(String.format("%s has waited.", playerParty[combatantTurn].name));
                    playerTurn = false;
                    playerParty[combatantTurn].endTurn();
                    turnFound = false;
                    highlightedSelection = 0;
                    break;
                case MOVE_RUN:
                    break;
            }
        }
    }

    //Method for making attacks
    private void attackMade(boolean enemiesTargetted, int target, Combatant attacker, int moveUsed) {
        String actionText;
        String targetName;
        //Which party are we targetting?
        Combatant[] partyTargetted;
        if (enemiesTargetted) {
            partyTargetted = enemyParty;
        } else {
            partyTargetted = playerParty;
        }
        if (moveUsed < 0) {
            //Basic attack
            targetName = partyTargetted[target].name;
            partyTargetted[target] = attacker.basicAttack(partyTargetted[target]);
            actionText = String.format("%s basic attacked %s", attacker.name, targetName);
        } else if (usingItem) {
            //Item used
            targetName = partyTargetted[target].name;
            partyTargetted[target] = inventory.item(moveUsed).usedOn(partyTargetted[target]);
            actionText = String.format("%s used on %s", inventory.item(moveUsed).name, targetName);
        } else {
            //Special Move used
            if (target < 0) {
                //AoE special move
                if (enemiesTargetted) {
                    targetName = "the enemy party";
                } else {
                    targetName = "your party";
                }
                for (int c = 0; c < partyTargetted.length; c++) {
                    if (partyTargetted[c] != null) {
                        partyTargetted[c] = attacker.usedSpecialMove(moveUsed, partyTargetted[c]);
                    }
                }
            } else {
                targetName = partyTargetted[target].name;
                partyTargetted[target] = attacker.usedSpecialMove(moveUsed, partyTargetted[target]);
            }
            actionText = String.format("%s used %s on %s", attacker.name, attacker.specialMoves[moveUsed].name, targetName);
        }
        actionLog.add(actionText);
        if (enemiesTargetted) {
            enemyParty = partyTargetted;
        } else {
            playerParty = partyTargetted;
        }
    }

    private void actionMade() {
        playerTurn = false;
        playerParty[combatantTurn].endTurn();
        turnFound = false;
        highlightedSelection = 0;
        menuDepth = BASE_MOVE_SELECTION;
    }
}
