import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;

class Map {

    /*
    This Class contains tha map or the array with all the lifeforms. It creates the array and takes care of all
    actions taking place.
     */

    // 2D Array that will SupplyDrop all the Entities
    private Entity grid[][];

    // For Universal Use
    int r, c, totalHumans, totalZombies, totalWater, totalSupplyDrops, birthH, birthZ, picSize = 25;
    int shift = 0; // To help with Graphics
    // Will Store Limits
    int humanCap, zombieCap, resourceMin;
    boolean zombies, humans, SupplyDrops, water, freeSpace, mate, zombieMate;
    boolean allowReviveHuman = true, allowReviveZombie = true, allowReviveResource= true, populationCap = true;
    // Source for pictures depending on user
    String source = "";


    /* Constructor:
       - Will Construct a 2D array filled with random Entities
       - Will Also initialize the array based on the width and height of the JFrame */
    public Map(int width, int height) {


        // Calculate the number of Entities that will fit vertically and horizontally
        // Each Entity (Picture) is picSize px by picSize px
        int column = width / picSize;
        int rows = height / picSize;

        grid = new Entity[rows][column]; // Initialize 2D Array

        // Iterate through 2D Array and fill with Random Entities
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                // If the position is not already filled with an Entity
                if (!(grid[row][col] instanceof Entity)) {

                    if (Random() < 3) // 3% Chance that it is a SupplyDrop
                        grid[row][col] = new SupplyDrop();
                    else if (Random() < 5) // 5% Chance that it is Water
                    {
                        grid[row][col] = new Water();
                        if (Random() < 90) // 90% Chance the element to the underneath is also Water
                        {
                            if (row + 1 < grid.length) // Checks if such a position is possible on the array
                                grid[row + 1][col] = new Water();
                        }
                        if (Random() < 50) // 50% Chance the element to the underneath and one unit to the right, is also Water
                        {
                            if (row + 1 < grid.length && col + 1 < grid[0].length) // Checks if such a position is possible on the array
                                grid[row + 1][col + 1] = new Water();
                        }
                        if (Random() < 90) // 90% Chance the element to the right is also Water
                        {
                            if (col + 1 < grid[0].length) // Checks if such a position is possible on the array
                                grid[row][col + 1] = new Water();
                        }
                    } else if (Random() < 5) // 5% Chance that it is a Zombie
                        grid[row][col] = new Zombie();
                    else if (Random() < 20) // 20% Chance that it is a Human
                        grid[row][col] = new Human('R');
                    else if (Random() < 3) // 3% Chance for Barrier
                        grid[row][col] = new Barrier();
                    else // It is just a blank Entity or ' ' or space
                        grid[row][col] = new Entity();
                }

            } // Columns
        } // Rows


    } // Grid Constructor

    /* Constructor:
       - Will Construct a 2D array filled with blank Entities
       - Will Also initialize the array based on the width and height of the JFrame */
    public Map(int width, int height, char type) {

        // Calculate the number of Entities that will fit vertically and horizontally
        // Each Entity (Picture) is picSize px by picSize px
        int column = width / picSize;
        int rows = height / picSize;
        grid = new Entity[rows][column]; // Initialize 2D Array

        // Iterate through 2D Array
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[0].length; col++)
                grid[row][col] = new Entity(); // Set as Blank Entity
    }


    // - Displays Entities on the JFrame
    public void show(Graphics g) throws IOException {
        // Images
        BufferedImage SupplyDrop = null;
        BufferedImage water = null;
        BufferedImage img = null;
        BufferedImage grass = null;
        BufferedImage building = null;

        // Load Images based on File Path
        try {
            SupplyDrop = ImageIO.read(new File(source + "Chest.png"));
            water = ImageIO.read(new File(source + "TempWater3.png"));
            building = ImageIO.read(new File(source + "Tombstones.png"));
            grass = ImageIO.read(new File(source + "grassTemp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Display the floor
        g.drawImage(grass, 0, 0, grid[0].length*picSize, grid.length*picSize, null);
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                g.drawImage(grass, col * picSize, row * picSize + shift, picSize, picSize, null);
            }
        }



        // Iterate through 2D Array of Entities
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                if (grid[row][col] instanceof Person) {
                    // Use the getImage method to load the correct image
                    img = getImage(new Position(row, col));
                    g.drawImage(img, col * picSize, row * picSize + shift, picSize, picSize, null);

                } else {
                    if (grid[row][col] instanceof Water) {
                        g.drawImage(water, col * picSize, row * picSize + shift, picSize, picSize, null);
                    } else if (grid[row][col] instanceof SupplyDrop) {
                        g.drawImage(SupplyDrop, col * picSize, row * picSize + shift, picSize, picSize, null);
                    }
                    else if (grid[row][col] instanceof Barrier) {
                        g.drawImage(building, col * picSize, row * picSize + shift, picSize, picSize, null);
                    }

                }

                // Draw the image

            } // Column Loop
        } // Row Loop

    } // Show Method

    /*
    - Will Iterate through 2D array of Entities
    - Will set the current row and column of the Entity in the loop to instance variables for other methods to use
    - Will call the live method which determine the actions of the Entity at position 'r' and 'c'
     */
    public void advance(int hBirth, int zBirth, boolean revHuman, boolean revZombie, boolean reviveResource, boolean popCap) {
        birthH = hBirth;
        birthZ = zBirth;
        allowReviveHuman = revHuman;
        allowReviveZombie = revZombie;
        allowReviveResource = reviveResource;
        populationCap = popCap;
        humanCap = grid.length*grid[0].length/100*8;
        zombieCap = grid.length*grid[0].length/100*8/4;
        resourceMin = humanCap/9;

        // Iterate through 2D array of Entities
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                // Set the position of the current Entity as Instance Variables
                r = row;
                c = col;
                // Call the live method to determine what to do with grid[r][c] or that Entity
                live();
            }
        }
    } //advance() method

    public void live() {

        // Will be set to True depending on if the boolean's Entity is near them
        zombies = false;
        humans = false;
        SupplyDrops = false;
        water = false;
        freeSpace = false;
        mate = false; // Will set to true, if the Entity's mate is near them
        zombieMate = true; // Will set to False, if the Zombie is not allowed to mate

        //-------------------------------------------------------------------------------------------------------------
        // SURROUNDING CHECK -------------------------------------------------------------------------------------------
        // Check the surroundings of the current Entity and set the appropriate booleans true

        if (surroundedBy('Z', new Position(r, c))) // If the Entity is surrounded by a Zombie
            zombies = true;
        if (surroundedBy('H', new Position(r, c))) // If the Entity is surrounded by a Human
        {
            humans = true;

            if (grid[r][c] instanceof Human) // If the Entity is a Human
                mate = mateCheck(); // Check if the opposite gender of age is around
        }
        if (surroundedBy('S', new Position(r, c))) // If the Entity is surrounded by a SupplyDrop
            SupplyDrops = true;
        if (surroundedBy('W', new Position(r, c))) // If the Entity is surrounded by Water
            water = true;
        if (surroundedBy(' ', new Position(r, c))) // If the Entity is surrounded by a free space
            freeSpace = true;

        //--------------------------------------------------------------------------------------------------------------
        // COUNT ENTITIES-----------------------------------------------------------------------------------------------
        // Call the count method to count the number of each Entity on the array or Map
        totalHumans = count('H');
        totalZombies = count('Z');
        totalSupplyDrops = count('S');
        totalWater = count('W');

        //--------------------------------------------------------------------------------------------------------------
        // UPDATES
        // Perform UPDATES on each health/attributes based on surroundings

        // PERSON TYPE ENTITY -----------------------------------------------------------------------------------------
        if (grid[r][c] instanceof Person) {
            // Water
            if (water) // If there is water nearby the person
                increaseThirstDecreaseWater(); // Call the Method to Increase Thirst and Decrease the Water Amount of the Person
            else
                ((Person) grid[r][c]).decThirst(); // Decrease thirst of the Person

            // HUMAN TYPE ENTITY---------------------------------------------------------------------------------------
            if (grid[r][c] instanceof Human) {

                // Age
                ((Human) grid[r][c]).incAge(); // Increment Age


                // Hunger
                if (SupplyDrops) // If there is a SupplyDrop nearby the human
                    increaseHungerDecreaseFood(); // Call the method to Increase Hunger and Decrease the Food Amount
                else
                    ((Human) grid[r][c]).decHunger(); // Decrease thirst
            }
            // ZOMBIE TYPE ENTITY---------------------------------------------------------------------------------------
            else if (grid[r][c] instanceof Zombie) {
                if (!humans) // If there are no humans nearby, decrease Hunger
                    ((Zombie) grid[r][c]).decHunger();
            }
        }
        //--------------------------------------------------------------------------------------------------------------
        //EVENTS AND ACTIONS
        // Now perform the appropriate actions (ex. die, run, kill), given the type of entity

        if (populationCap) {
            // Prevent Humans from mating if population is too high
            if (totalHumans > humanCap)
                mate = false;
            // Prevent Zombies from populating if Zombie population is too high
            if (totalZombies > zombieCap)
                zombieMate = false;
        }

        if ( (totalWater < resourceMin || totalSupplyDrops < resourceMin) && allowReviveResource)
            reviveResources();
        if ( (totalHumans <= 10) && allowReviveHuman)
            reviveHumans();
        if ( (totalZombies <= 10) && allowReviveZombie)
            reviveZombies();


        //ALL TYPES EXCEPT SPACE TYPE-----------------------------------------------------------------------------------
        if (naturalDeathCheck() && !(grid[r][c].getType() == ' ')) // Call method to check for Natural Death
            grid[r][c] = new Entity(); // 'Kill' the Entity

        //HUMAN TYPE---------------------------------------------------------------------------------------------------
        if (grid[r][c] instanceof Human) {

            // If there are zombies nearby
            if (zombies && Random() <= 90) {
                // 90% Chance that they will fight
                // Send in a Position of a Random Zombie around the human (preferably the one that they are facing)
                fight(chooseRandomPos(surroundPosOf('Z', new Position(r, c)), new Position(r, c)));
            }
            /*
            If....
            - The random number is below the birth rate
            - Mating is allowed
            - Free space available (for child)
            - Human is Over 18 (Legal Age to have kids)
            THEN
            - Have a Baby!
             */
            else if (Random() <= birthH && mate && freeSpace && ((Human) grid[r][c]).getAge() > 18) {
                // Choose a random free position to birth the baby, around the Human
                Position birth = chooseRandomPos(surroundPosOf(' ', new Position(r, c)), new Position(r, c));
                // Create child in chosen location
                grid[birth.getRow()][birth.getCol()] = new Human();
            }
            // 80% Chance that they will move, if there is free space around them
            else if (Random() <= 80 && freeSpace) {
                // Choose a random free position to move to
                Position move = chooseRandomPos(surroundPosOf(' ', new Position(r, c)), new Position(r, c));
                // Set the Humans's last move (for Graphics)
                setLastMove(move, new Position(r, c));
                // Move the Human to the free space
                grid[move.getRow()][move.getCol()] = new Human(((Human) grid[r][c]));
                grid[r][c] = new Entity();
            }

        } // Instanceof Human

        //ZOMBIE TYPE---------------------------------------------------------------------------------------------------
        else if (grid[r][c] instanceof Zombie) {

            // 90% Chance the zombie and human will fight, if there is a human around
            if (humans && Random() <= 90) {
                fight(new Position(r, c));
            }
            // 90% Chance that they will move to a free space, if there is one
            else if (freeSpace && Random() <= 90) {
                // Choose a random free position
                Position move = chooseRandomPos(surroundPosOf(' ', new Position(r, c)), new Position(r, c));
                // Record the last move of the zombie (for Graphics)
                setLastMove(move, new Position(r, c));
                // Move the Zombie
                grid[move.getRow()][move.getCol()] = new Zombie(((Zombie) grid[r][c]));
                grid[r][c] = new Entity();
            }

        } // instanceof Zombie

    }// Live Method

    /*
    - Will Return a Position, given an ArrayList of Positions
    - If there is a Position in the ArrayList, that is in the direction the current object is facing
        it will return that

     ArrayList<Position> list --> is the list of positions
     Position current --> is the Position of the 'current' object
     */
    public Position chooseRandomPos(ArrayList<Position> list, Position current) {

        int rowDiff, colDiff, rCurrent, cCurrent;
        String newMove = "";

        // Get the row and column number of the current object
        rCurrent = current.getRow();
        cCurrent = current.getCol();

        // Iterate through the Positions in the list
        for (Position pos : list) {
            // Find the difference in the row's and column's
            rowDiff = rCurrent - pos.getRow();
            colDiff = cCurrent - pos.getCol();

            // Find the direction of the position in relation to the current position
            if (rowDiff == 1)
                newMove = "Up";
            else if (colDiff == 1)
                newMove = "Left";
            else if (colDiff == -1)
                newMove = "Right";
            else if (rowDiff == -1)
                newMove = "Down";

            // Very Buggy - MAY COMMENT ***************************
            // If the Direction matches the lastMove of the current object, return that position
            if ((((Person) grid[rCurrent][cCurrent]).getLastMove()).equals(newMove))
                return pos;

        } //Loop

        // If no position's direction matches the current object's last move, return a random position from the list
        // By Calling the overloaded method
        return chooseRandomPos(list);

    } // chooseRandomPos method

    // Will Choose and return a Random Position given an ArrayList of Positions
    public Position chooseRandomPos(ArrayList<Position> list) {

        Random ran = new Random(); // Create a Random Object
        int choose = ran.nextInt(list.size()); // Choose a random index, from the ArrayList

        // Return the chosen position
        return list.get(choose);
    } // chooseRandomPos method


    // Returns the Positions around the given Position
    public ArrayList<Position> surroundPos(Position p) {
        // ArrayList will SupplyDrop Positions around given Position
        ArrayList<Position> newList = new ArrayList<Position>();
        int row, col;

        // Get the row and column of the given Position
        row = p.getRow();
        col = p.getCol();

        // Check if the position is possible on the array, then add to an ArrayList
        if ((row - 1) > -1) // Above
            newList.add(new Position(row - 1, col));
        if ((row + 1) < grid.length) // Below
            newList.add(new Position(row + 1, col));
        if ((col - 1) > -1) // Left
            newList.add(new Position(row, col - 1));
        if ((col + 1) < grid[0].length) // Right
            newList.add(new Position(row, col + 1));

        newList.trimToSize(); // Trim down Arraylist
        return newList;
    } //SurroundPos Method

    // Returns the positions that are of type 'form' around the given position
    public ArrayList<Position> surroundPosOf(char form, Position p) {

        // Will SupplyDrop Positions of Entities type 'form'
        ArrayList<Position> list = new ArrayList<Position>();

        // Get an ArrayList of all the Positions of the Entities that surround p
        ArrayList<Position> surroundings = surroundPos(p);

        // Loop through the positions of the Entities that surround Position p
        for (Position pos : surroundings) {
            // If the Entity Matches the given form, add to the new ArrayList
            if (grid[pos.getRow()][pos.getCol()].getType() == form)
                list.add(pos);

        }

        // Trim and return ArrayList
        list.trimToSize();
        return list;
    }

    // Returns true, if the Position given is surrounded by the given type
    public boolean surroundedBy(char form, Position p) {
        // Get the positions of the surroundings of Position p
        ArrayList<Position> surroundings = surroundPos(p);

        // Loop through the positions of the surrounding objects
        for (Position pos : surroundings) {
            // If the surrounding object matches the form or type
            if (grid[pos.getRow()][pos.getCol()].getType() == form)
                return true; // Return true

        }
        // If none of the surrounding objects are of the given type or form
        return false;
    }

    // Method is called upon Interaction of a a Zombie and Human
    public void fight(Position ps) // Is Given Position of zombie
    {
        // Get Row and Column Number of Zombie
        int row = ps.getRow();
        int col = ps.getCol();

        // Choose a random Human around the zombie (preferably the one that they are facing)
        // kill is chosen Human's position
        Position kill = chooseRandomPos(surroundPosOf('H', new Position(row, col)), ps);


        if (Random() <= 5) // 5% Chance the human will cure the zombie
        {
            grid[row][col] = new Human();
        } else if (Random() <= 5) // 5% chance the human will kill the zombie and take over its position
        {
            // Record move (for graphics)
            setLastMove(ps, kill);
            // Human takes over Zombie's Position
            grid[row][col] = new Human((Human) grid[kill.getRow()][kill.getCol()]);
            grid[kill.getRow()][kill.getCol()] = new Entity();

        } else if (Random() <= 50) // 50% chance the human will run away
        {
            // Will SupplyDrop free spaces that are safe from zombies
            ArrayList<Position> freeSafeSpaces = new ArrayList<Position>();
            // Will SupplyDrop final position the human will move to
            Position freeSpaceToRun;
            // SupplyDrop the positions of the free spaces around the human, that it can run to
            ArrayList<Position> freeSpaces = surroundPosOf(' ', kill);

            // Loop through the free spaces, and record the ones that are safe
            for (Position p : freeSpaces) {
                if (!(surroundedBy('Z', p))) // Check if position is safe
                    freeSafeSpaces.add(p); // Add to Safe Positions ArrayList
            }

            // If there are any safe spaces around the human
            if (surroundedBy(' ', kill)) {

                // 60% chance it will run to a safe space if there is one
                if (Random() <= 60 && (!freeSafeSpaces.isEmpty()))
                    freeSpaceToRun = chooseRandomPos(freeSafeSpaces, kill);
                else // If there is no safe spaces, choose any random free space
                    freeSpaceToRun = chooseRandomPos(freeSpaces, kill);

                // Record the last move (for graphics)
                setLastMove(freeSpaceToRun, kill);
                // Move the human to the space chosen
                grid[freeSpaceToRun.getRow()][freeSpaceToRun.getCol()] = new Human((Human) grid[kill.getRow()][kill.getCol()]);
                grid[kill.getRow()][kill.getCol()] = new Entity();
            }
        } // Human Run away
        else // Zombie kills human
        {
            // Increase Zombie Hunger
            ((Zombie) grid[row][col]).incHunger();

            // 50% chance it will turn the human to a zombie
            if (Random() <= birthZ && zombieMate) {
                grid[kill.getRow()][kill.getCol()] = new Zombie();
            } else // 50% chance the human will get killed and the zombie will take over its position
            {
                // Record the last move of the zombie (for graphics)
                setLastMove(kill, ps);
                // Zombie moves into Human's spot
                grid[kill.getRow()][kill.getCol()] = new Zombie((Zombie) grid[row][col]);
                grid[row][col] = new Entity();
            }

        } // Zombie Kills Human

    } // Kill Method

    // Will Return true, if the opposite gender is around the position 'r' and 'c'
    public boolean mateCheck() {
        // Get the positions of the surroundings Humans (around 'r' and 'c')
        ArrayList<Position> humanSurround = surroundPosOf('H', new Position(r, c));

        // Loop through the positions of the surrounding entities
        for (Position pos : humanSurround) {
            // Return true if the opposite gender is around
            if (((Human) grid[r][c]).getGender() != ((Human) grid[pos.getRow()][pos.getCol()]).getGender() && ((Human) grid[pos.getRow()][pos.getCol()]).getAge() >= 18)
                return true;
        }
        // If there are no humans of the opposite gender around, return false
        return false;
    }

    // Checks if any of the attributes have reached their limit, causing natural death
    public boolean naturalDeathCheck() {
        // If the entity is a Person
        if (grid[r][c] instanceof Person) {
            // Check their if their thirst and hunger level is below 1, if so, return true (they die)
            if (((Person) grid[r][c]).getThirst() < 1 || ((Person) grid[r][c]).getHunger() < 1) {

                return true;
            }
            if (grid[r][c] instanceof Human)  // If the Person is a Human
            {
                // Check if they are over 100 years old
                if (((Human) grid[r][c]).getAge() > 100) {
                    return true; // Return true if they are
                }
            }
        }
        // If the entity is a resource (SupplyDrop or water)
        else if (grid[r][c] instanceof Resource) {
            // If the resource ammount is less than 1
            if (((Resource) grid[r][c]).getAmount() < 1) {
                return true; // the resource is used up and 'dies'
            }
        }

        // If none of the above factors are true, return false
        return false;
    }

    // Increases the hunger of the current Person, and decreases the amount of Food
    public void increaseHungerDecreaseFood() {
        ((Person) grid[r][c]).incHunger(); // Increase Hunger
        // Choose a random food entity around the person (and get it's position)
        Position food = chooseRandomPos(surroundPosOf((((Person) grid[r][c]).getFood()), new Position(r, c)));
        // Decrease the chosen food's amount of food/resource
        ((Resource) grid[food.getRow()][food.getCol()]).decAmount();
    }

    // Increases the hunger of the current Person, and decreases the amount of Food
    public void increaseThirstDecreaseWater() {
        ((Person) grid[r][c]).incThirst(); // Increase Thirst
        // Choose a random water entity around the person (and get it's position)
        Position waterPos = chooseRandomPos(surroundPosOf('W', new Position(r, c)), new Position(r, c));
        // Decrease the chosen water entity's amount of resource/water
        ((Resource) grid[waterPos.getRow()][waterPos.getCol()]).decAmount();
    }

    // Returns the number of Entities that are of type 'form' on the Map or array
    public int count(char form) {
        int count = 0;
        //Iterate through array of Entities
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col].getType() == form) // If the Entity's form matches the given form
                    count++; // Increase count
            }
        }
        return count; // Return count
    }

    // Records the direction of the given current object's last move (helps with graphics)
    public void setLastMove(Position freePos, Position current) {
        int rowDiff, colDiff, rCurrent, cCurrent, rFree, cFree;
        String newMove = "";

        // Get the row and column the current object is at
        rCurrent = current.getRow();
        cCurrent = current.getCol();

        // Get the row and column the current object wants to move to
        rFree = freePos.getRow();
        cFree = freePos.getCol();

        // Subtract the rows and columns to find their difference
        rowDiff = rCurrent - rFree;
        colDiff = cCurrent - cFree;


        // With the differences, determine the direction the current Entity wants to move, and set as 'newMove'
        if (colDiff == -1)
            newMove = "Right";
        else if (rowDiff == -1)
            newMove = "Down";
        else if (rowDiff == 1)
            newMove = "Up";
        else if (colDiff == 1)
            newMove = "Left";

        // Set the Entity's Last Move
        ((Person) grid[rCurrent][cCurrent]).setLast(newMove);

    } // Set Last Move Method

    // Each entity has a 5% chance it will be flooded
    public void flood() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (Random() < 5)
                    grid[row][col] = new Water();
            }

        }
    } // Flood Method

    // Each entity has a 30% chance it will be killed
    public void earthquake() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (Random() < 30)
                    grid[row][col] = new Entity();
            }
        }
    } // Flood Method

    // Each Zombie has a 30% chance of being cured
    public void cure() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] instanceof Zombie) {
                    if (Random() < 30)
                        grid[row][col] = new Human('R');
                }
            }
        }
    } // Cure

    // Each Human has a 30% Chance it will get the virus and become a zombie
    public void virus() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] instanceof Human) {
                    if (Random() < 30)
                        grid[row][col] = new Zombie();
                }
            }
        }
    } // Virus Method

    // Each water entity has a 30% chance it will get removed
    public void drought() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] instanceof Water) {
                    if (Random() < 30)
                        grid[row][col] = new Entity();
                }
            }
        }
    } // Drought

    // Each Supply Drop has a 30% chance it will get removed
    public void famine() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] instanceof SupplyDrop) {
                    if (Random() < 30)
                        grid[row][col] = new Entity();
                }
            }
        }

    }

    // Each Blank Entity has a 5% chance of becoming a supply drop
    public void supplyDrop() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col].getType() == ' ') {
                    if (Random() < 5)
                        grid[row][col] = new SupplyDrop();
                }
            }
        }
    } // Supply Drop

    // Revives Resources in the map
    public void reviveResources() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                    if (Random() < 5) // 5% Chance that it is a SupplyDrop
                        grid[row][col] = new SupplyDrop();
                    else if (Random() < 5) // 5% Chance that it is Water
                    {
                        grid[row][col] = new Water();
                        if (Random() < 90) // picSize% Chance the element to the underneath is also Water
                        {
                            if (row + 1 < grid.length) // Checks if such a position is possible on the array
                                grid[row + 1][col] = new Water();
                        }
                        if (Random() < 50) // picSize% Chance the element to the underneath and one unit to the right, is also Water
                        {
                            if (row + 1 < grid.length && col + 1 < grid[0].length) // Checks if such a position is possible on the array
                                grid[row + 1][col + 1] = new Water();
                        }
                        if (Random() < 90) // picSize% Chance the element to the right is also Water
                        {
                            if (col + 1 < grid[0].length) // Checks if such a position is possible on the array
                                grid[row][col + 1] = new Water();
                        }
                    }


            }
        }
    } // revive Resource Method

    // Revives Humans on the map in the empty spaces
    public void reviveHumans()
    {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                if (grid[row][col].getType() == ' ') {
                    if (Random() < 10) // 10% Chance that it is Water
                    {
                        grid[row][col] = new Human('R');
                    }
                }

            }
        }
    } // revive Humans

    // Revives zombies on the map, at the empty spaces
    public void reviveZombies()
    {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                if (grid[row][col].getType() == ' ') {
                    if (Random() < 3)
                    {
                        grid[row][col] = new Zombie();
                    }
                }

            }
        }
    } // revive Humans





    // Get's the Image of the Person based on their attributes
    public BufferedImage getImage(Position pos) throws IOException {
        BufferedImage img = null;

        // Get the row and column
        int row = pos.getRow();
        int col = pos.getCol();
        int count = 0;

        // Get the count (number of times they have went in the same direction) of the Entity if they are a person
        if (grid[row][col] instanceof Person)
            count = ((Person) grid[row][col]).getCount();

        // If the are a Human
        if (grid[row][col] instanceof Human) {

            // Male Human
            if (((Human) grid[row][col]).getGender() == 'M') {
                // Under 18
                if (((Human) grid[row][col]).getAge() < 18) {
                    // Get Image based on Direction and Count
                    if (((Human) grid[row][col]).getLastMove().equals("Up")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "BB1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "BB2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "BB3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "BB2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Down")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "BF1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "BF2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "BF3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "BF2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Left")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "BL1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "BL2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "BL3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "BL2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Right")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "BR1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "BR2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "BR3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "BR2.png"));
                        }
                    } // else if
                } // Under 18
                else { // Over 18
                    // get image based on direction and count
                    if (((Human) grid[row][col]).getLastMove().equals("Up")) {

                        if (count == 1)
                            img = ImageIO.read(new File(source + "MB1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "MB2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "MB3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "MB2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Down")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "MF1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "MF2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "MF3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "MF2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Left")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "ML1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "ML2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "ML3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "ML2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Right")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "MR1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "MR2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "MR3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "MR2.png"));
                        }
                    } // else if

                } // over 18

            } // M type
            // Female Type
            else if (((Human) grid[row][col]).getGender() == 'F') {
                if (((Human) grid[row][col]).getAge() < 18) { // Under 18
                    // get image based on direction and count
                    if (((Human) grid[row][col]).getLastMove().equals("Up")) {

                        if (count == 1)
                            img = ImageIO.read(new File(source + "GB1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "GB2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "GB3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "GB2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Down")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "GF1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "GF2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "GF3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "GF2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Left")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "GL1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "GL2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "GL3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "GL2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Right")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "GR1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "GR2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "GR3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "GR2.png"));
                        }
                    }
                } // Under 18
                else { // Over 18
                    // get image based on direction and count
                    if (((Human) grid[row][col]).getLastMove().equals("Up")) {

                        if (count == 1)
                            img = ImageIO.read(new File(source + "WB1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "WB2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "WB3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "WB2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Down")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "WF1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "WF2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "WF3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "WF2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Left")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "WL1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "WL2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "WL3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "WL2.png"));
                        }
                    } else if (((Human) grid[row][col]).getLastMove().equals("Right")) {
                        if (count == 1)
                            img = ImageIO.read(new File(source + "WR1.png"));
                        else if (count == 2)
                            img = ImageIO.read(new File(source + "WR2.png"));
                        else if (count == 3)
                            img = ImageIO.read(new File(source + "WR3.png"));
                        else if (count == 4) {
                            img = ImageIO.read(new File(source + "WR2.png"));
                        }
                    } // Last Else if

                } // Over 18

            } // F type

        } // human
        else if (grid[row][col] instanceof Zombie) { // Zombie
            // get image based on direction and count
            if (((Zombie) grid[row][col]).getLastMove().equals("Up")) {

                if (count == 1)
                    img = ImageIO.read(new File(source + "ZB1.png"));
                else if (count == 2)
                    img = ImageIO.read(new File(source + "ZB2.png"));
                else if (count == 3)
                    img = ImageIO.read(new File(source + "ZB3.png"));
                else if (count == 4) {
                    img = ImageIO.read(new File(source + "ZB2.png"));
                }
            } else if (((Zombie) grid[row][col]).getLastMove().equals("Down")) {
                if (count == 1)
                    img = ImageIO.read(new File(source + "ZF1.png"));
                else if (count == 2)
                    img = ImageIO.read(new File(source + "ZF2.png"));
                else if (count == 3)
                    img = ImageIO.read(new File(source + "ZF3.png"));
                else if (count == 4) {
                    img = ImageIO.read(new File(source + "ZF2.png"));
                }
            } else if (((Zombie) grid[row][col]).getLastMove().equals("Left")) {
                if (count == 1)
                    img = ImageIO.read(new File(source + "ZL1.png"));
                else if (count == 2)
                    img = ImageIO.read(new File(source + "ZL2.png"));
                else if (count == 3)
                    img = ImageIO.read(new File(source + "ZL3.png"));
                else if (count == 4) {
                    img = ImageIO.read(new File(source + "ZL2.png"));
                }
            } else if (((Zombie) grid[row][col]).getLastMove().equals("Right")) {
                if (count == 1)
                    img = ImageIO.read(new File(source + "ZR1.png"));
                else if (count == 2)
                    img = ImageIO.read(new File(source + "ZR2.png"));
                else if (count == 3)
                    img = ImageIO.read(new File(source + "ZR3.png"));
                else if (count == 4) {
                    img = ImageIO.read(new File(source + "ZR2.png"));
                }
            }
        } // Zombie


        return img;
    } // getImage


    // Return random number from 1 to 100
    public int Random() {
        Random ran = new Random();
        return ran.nextInt(101);
    }

    // Return the grid
    public Entity[][] getGrid()
    {
        return grid;
    } // Get Grid

    // Set the grid
    public void setGrid(Entity[][] array)
    {
        for (int row = 0; row < array.length; row++)
        {
            for (int col = 0; col < array[0].length; col++)
            {
                grid[row][col] = array[row][col];
            }
        }

    } // Set Grid


} // Grid