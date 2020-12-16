public class Entity {
    /*
    This class is the parent class of all the entities or lifeforms in the game
     */

    // Store
    protected char type;

    public Entity(char type) {
        // 'Z' represents Zombie, 'H' represents Human, 'S' represents Supply Drop, 'W' represents Water
        this.type = type;
    }

    public Entity() {
        // ' ' Represents a blank entity or space
        type = ' ';
    }

    // Returns type
    public char getType()
    {
        return type;
    }


}
