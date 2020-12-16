class Position {
    /*
    This class allows us to store both a row and column number in a single object called Position.
    This makes it easier to iterate through a list, for example say, of 'free or safe spaces'.
     */

    // Will Store the row and column number of a position
    private int row, col;

    // Constructor that accepts and initializes row and column number
    public Position(int r, int c) {
        row = r;
        col = c;
    }

    // Returns the row number
    public int getRow() {
        return row;
    }

    // Returns the column number
    public int getCol() {
        return col;
    }


}
