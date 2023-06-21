package rushhour.model;


/**
 * This class maintains a position consisting of row and col
 */
public class Position {
    private int row;
    private int col;

    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    @Override
    public String toString(){
        return "row, col: " + this.row + "," + this.col;
    }
    
    @Override
    public int hashCode(){
        return (int)Math.pow(this.row,this.col);
    }

    /**
     * This checks if the row and column are the same. If so, they are equal.
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Position){
            Position other = (Position)obj;
            return this.row == other.row && this.col == other.col;
        }else{
            return false;
        }
    }
}
