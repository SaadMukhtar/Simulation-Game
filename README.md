# Simulation-Game

This is a Simulation Game made as my culminating project for my ICS4U Course in my final year of highschool.

It consists of two types of characters fighting for survival; Humans and Zombies.
Men and Women age and repopulate, surviving off food from chests and water from ponds. They can die from thirst, hunger, old age, or can get killed by a Zombie.
Zombies survive by eating Humans and can die from thirst.
Both groups tend to move in groups, and try to battle for survival.

Begin the Simulation by pressing the 'Simulate' button!
Press the 'Clear' button to customize and create your own playing field using the 'God Controls'!



![Simulation-Gameplay](https://github.com/SaadMukhtar/Simulation-Game/blob/master/Simulation-Gameplay.png)

## Instructions
- Download Java files and images and keep them in one folder.
- Run Simulation.java and enter source path for the folder
- Enjoy!

## More Details:

Human Character:
- If there is a zombie nearby, there is a 90% chance of a fight occuring
- 80% Chance to move to a free space
- If there is another adult nearby, a baby is produced in a free space nearby

Zombie Character:
- If there is a human around, 90% chance the zombie and human will fight
-  90% Chance that they will move to a free space, if there is one

Natural Disasters:
- Each entity has a 5% chance it will be flooded
- Each entity has a 30% chance it will be killed
- Each Zombie has a 30% chance of being cured
- Each Human has a 30% Chance it will get the virus and become a zombie
- Each water entity has a 30% chance it will get removed
- Each Supply Drop has a 30% chance it will get removed
- Each Blank Entity has a 5% chance of becoming a supply drop

Sliders:
- Human Birth Rate: Set the rate at which Humans repopulate
- Zombies Birth Rate: Set the rate at which Zombies repopulate
- Simulation Speed: Set the speed of the simulation

Laws:
- Revive Resources: Will Allow for For Resources Replenished Automatically
- Revive Humans: Will Allow for For Humans to be Revived Automatically
- Revive Zombies: Will Allow for For Zombies to be Revived Automatically
- Population Limit: Will Set the Desired Cap on the Population Limit According to the Slider

God Controls (Work Upon Clicking the Map):
- Add Humans: Adds Humans of Random Age and Gender on wherever clicked
- Add Zombies: Adds Zombies wherever clicked
- Flood: Adds Water wherever clicked.
- Add Supply Drops: Adds chests wherever clicked
- Remove Humans: Removes humans wherever clicked
- Remove Zombies:  Removes zombies wherever clicked
- Remove Water:  Removes water wherever clicked
- Remove Supply Drop: Removes chests wherever clicked
- Lightning: Removes human wherever clicked
- Mini Earthequake: Clears any entity wherever clicked
- Insert Cure: Cures the zombie, turning it to a human, if present wherever clicked
- Insert Virus: Infects a human, turning it into a zombie, if present wherever clicked
- Add Buildings: Add's Tombstone to act as Barriers in the map


