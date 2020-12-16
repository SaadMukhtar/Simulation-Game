import java.util.Random;

/*
    This class will parent the humans and zombie class/objects in the game. It will contain the attributes
    and actions they have in common.
 */
abstract class Person extends Entity {

    // Attributes
    protected char food;
    protected int hunger, thirst, count = 0;
    protected String lastMove = null;

    // Constructor that will initiliaze attributes
    Person(char Type) {
        super(Type);
        // Set Hunger and thirst
        hunger = 200;
        thirst = 200;

            // Set Count (for graphics)
            count = 2;

            // Set Direction of Last Move (for Graphics)
            if (Math.random() < 0.25)
                lastMove = "Up";
            else if (Math.random() < 0.25)
                lastMove = "Down";
            else if (Math.random() < 0.5)
                lastMove = "Left";
            else
                lastMove = "Right";

    }

    // Returns hunger level
    public int getHunger() {
        return hunger;
    }

    // Returns thirst level
    public int getThirst() {
        return thirst;
    }

    // Increases hunger
    public void incHunger() {
        if(hunger <= 200)
        hunger++;
    }

    // Increases thirst
    public void incThirst() {
        if(thirst <= 200)
        thirst++;
    }

    // Decrease Hunger
    public void decHunger() {
        hunger--;
    }

    // Decrease Thirst
    public void decThirst() {
        thirst--;
    }

    // Return Food type
    public char getFood()
    {
        return food;
    }

    // Set Last move
    public void setLast(String s)
    {
        if (lastMove.equals(s))
            incCount();
        else
            count = 2;

        lastMove = s;
    }

    // Will return the last move
    public String getLastMove()
    {
        return lastMove;
    }

    // Will return the count
    public int getCount()
    { return count; }

    // Will Increase the count
    public void incCount()
    {
        count++;
        if (count > 4)
            count = 1;
    }


}

// Will represent Humans in the game
class Human extends Person {
    // Human Attributes
    private int age;
    private char gender;

    // Will Create a Human with Child Attributes
    public Human() {
        super('H');
        Random r = new Random();
        food = 'S';
        age = -50;

        // Given random Gender
        if (Math.random() < 0.5)
            gender = 'M';
        else
            gender = 'F';
    }

    // Copy Constructor for Human
    public Human(Human h) {
        super('H');
        food = 'S';
        hunger = h.hunger;
        thirst = h.thirst;
        age = h.age;
        gender = h.gender;

        count = h.count;
    } // Copy Constructor

    // WIll Generate Humans of Random Ages
    public Human(char h)
    {
        super('H');
        Random r = new Random();
        food = 'S';
        age = r.nextInt(151) - 100;

        if (Math.random() < 0.5)
            gender = 'M';
        else
            gender = 'F';

    }

    // Increase Age
    public void incAge()
    {
        age++;
    }

    // Returns Age
    public int getAge()
    {
        return age;
    }

    // Returns Gender
    public char getGender()
    {
        return gender;
    }



} // Human Class

class Zombie extends Person {

    public Zombie() {
        super('Z');
        food = 'H';
    }

    public Zombie(Zombie z) {
        super('Z');
        food = 'H';
        hunger = z.hunger;
        thirst = z.thirst;
        count = z.count;
    }


} // Zombie Class