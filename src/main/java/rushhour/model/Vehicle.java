package rushhour.model;

public class Vehicle {
    private char symbol;
    private Position back;
    private Position front;
    private boolean isVertical;


    public Vehicle(char symbol, Position back, Position front){
        this.symbol = symbol;
        this.back = back;
        this.front = front;
    }

    public Vehicle(Vehicle other){
        this(other.getChar(), other.getBack(), other.getFront());
    }

    public char getChar(){
        return this.symbol;
    }

    public Position getBack(){
        return this.back;
    }

    public Position getFront(){
        return this.front;
    }

    /**
     * checks if vertical
     * @return boolean for ifVertical
     */
    public boolean isVert(){

        if(this.getFront().getRow() == this.getBack().getRow()){
            isVertical = false;
        }
        else{
            isVertical = true;
        }
        return isVertical;
    }

    /**
     * updates back and front position based on given move
     * @param dir
     * @throws RushHourException if invalid move
     */
    public void move(Direction dir) throws RushHourException{
        if(dir == Direction.UP){
            this.back = new Position(this.back.getRow()-1, this.back.getCol());
            this.front = new Position(this.front.getRow()-1, this.front.getCol());
        }
        if(dir == Direction.DOWN){
            this.back = new Position(this.back.getRow()+1, this.back.getCol());
            this.front = new Position(this.front.getRow()+1, this.front.getCol());
        }
        if(dir == Direction.LEFT){
            this.back = new Position(this.back.getRow(), this.back.getCol()-1);
            this.front = new Position(this.front.getRow(), this.front.getCol()-1);
        }
        if(dir == Direction.RIGHT){
            this.back = new Position(this.back.getRow(), this.back.getCol()+1);
            this.front = new Position(this.front.getRow(), this.front.getCol()+1);
        }
        
    }

    @Override
    public String toString(){
        return this.symbol +" : "+ this.front + " " + this.back;
    }
}
