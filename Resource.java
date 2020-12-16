abstract class Resource extends Entity
{
    /*
    This abstract class will Parent objects/classes of resources.
     */
    //Stores amount of resource
    protected int amount;

    Resource(char Type)
    {
        super(Type);
        amount = 20; // Will Start with an amount of 20
    }

    // Will Decrease Amount
    public void decAmount()
    {
        amount--;
    }

    // Will return Amount
    public int getAmount()
    {
        return amount;
    }


} // Resource Class

// This Class will represent the Supply Drops in the Game
class SupplyDrop extends Resource
{
    SupplyDrop()
    {
        super('S');
    }
}
// This class will represent the Water in the game
class Water extends Resource
{
    Water()
    {
        super('W');
    }
}
