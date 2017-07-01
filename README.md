# Turn-Based-Combat
An unnamed single player turn based combat game

## Background

This was the final project I (Austin Seto) made for grade 12 introduction to computer science in high school (course code ICS4U) in Ontario, Canada. It was developed over the course of a semester and comprised the majority of my grade for the course alongside small exercises. 

Implemented is the basis for the combat portions of the game. There is only a single fight between two knights (one AI controlled, one player controlled) and a player controlled archer. To get a different set of fighters, one must change the combatant objects created at runtime. Each side can have up to five fighters. The enemy is given a basic AI that follows a given play pattern and does basic analysis on potential targets.

## Description

The game has turn based combat in a similar vein to Pokemon. Each character type has a set of attributes that describes how tough they are, how hard they hit, how fast they are etc. Different character types have different values for their attributes which increase at different rates as they level up. 

Turn order is determined by a character's speed attribute. Characters with a higher speed attribute will move before characters with a lower speed attribute. On a character's turn, they can make a basic attack, a special attack (with special effects), use an item, or skip their turn. A 'run' option is shown, but does nothing currently as there is only a combat portion of the game.

Characters can suffer from status conditions, each of which does a different thing. Burn and poison damage the character each time their turn starts. Exhaust reduces the damage of a character's attacks. Regen heals a character each time their turn stats. Resist and vulnerable decrease or increase the damage a character takes respectively. Each turn, the remaining duration on all a character's status conditions decreases by one until it hits zero, at which point they are no longer affected by it. 

Input is made with the keyboard. The arrow keys can be used to select what action is taken, and the targets of the action. The Z key is used to cancel a selection, while the X key makes selections. 

There are no animations of the fight. Instead, the events of the fight are printed to the centre of the window where the animations would be. 
