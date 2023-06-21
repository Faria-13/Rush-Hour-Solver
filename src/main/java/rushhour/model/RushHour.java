package rushhour.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import backtracker.Backtracker;

public class RushHour {
    private int BOARD_DIM = 6;
    private char RED_SYMBOL = 'R';
    private char EMPTY_SYMBOL = '-';
    private Position EXIT_POS = new Position(2,5);
    private char[][] board;
    private Set<Vehicle> vehicles;
    private int moveCount;

    private RushHourObserver observer;

    private HashMap<Character, Vehicle> VehicleInfo;

    /**
     * goes through given file if it exists 
     * gets the postition of back and front from file
     * with this information creates a new veichle and adds it ot veichles set
     * then populates board - places veichles on board
     * @param filename
     * @throws IOException
     */
    public RushHour(String filename) throws IOException{
        VehicleInfo = new HashMap<>();
        moveCount = 0;
        board = new char[BOARD_DIM][BOARD_DIM];
        vehicles = new HashSet<>();
        File file = new File(filename);
        FileReader freader = new FileReader(file);
        BufferedReader reader = new BufferedReader(freader);
        String line = reader.readLine();
        while(line != null){
            String[] tokens = line.split(",");
            //System.out.println(Arrays.toString(tokens));
            char symbol = tokens[0].charAt(0); 
            Position back = new Position(Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]));
            Position front = new Position(Integer.valueOf(tokens[3]), Integer.valueOf(tokens[4]));
            Vehicle eldwin = new Vehicle(symbol, back, front);
            vehicles.add(eldwin);
            line = reader.readLine();    
        }
        //System.out.println("Constructor vehicles test: " + vehicles);

        reader.close();
        populateBoard();
    }
    public RushHour(RushHour other){
        this.board = Arrays.copyOf(other.board, BOARD_DIM);
        this.vehicles = new HashSet<>(other.vehicles);
        this.moveCount = other.moveCount;
        this.observer = null;
        this.VehicleInfo = new HashMap<>(other.VehicleInfo);
        //this.populateBoard();
    }


    public void registerObserver(RushHourObserver observer){
        this.observer = observer;
    }

    public HashMap<Character,Vehicle> getVehicleInfo(){
        return this.VehicleInfo;
    }

    /**
     * gets column and row or front and back of each veichle and adds them to the board
     * fills in space between front and back as veichle
     * adds empty string where there is no veichle
     */
    private void populateBoard(){
        //fill in vehicles in board
        for(Vehicle vehicle: vehicles){
            if(vehicle.isVert() == false){
                VehicleInfo.put(vehicle.getChar(), vehicle);
                int diff = vehicle.getFront().getCol() - vehicle.getBack().getCol();
                board[vehicle.getFront().getRow()][vehicle.getFront().getCol()] = vehicle.getChar();
                board[vehicle.getBack().getRow()][vehicle.getBack().getCol()] = vehicle.getChar();
                if(diff>1){
                    for(int i = 1; i<diff;i++){
                        board[vehicle.getFront().getRow()][vehicle.getFront().getCol()-i] = vehicle.getChar();
                    }
                }
            }else{
                VehicleInfo.put(vehicle.getChar(), vehicle);
                int diff = vehicle.getFront().getRow() - vehicle.getBack().getRow();
                board[vehicle.getFront().getRow()][vehicle.getFront().getCol()] = vehicle.getChar();
                board[vehicle.getBack().getRow()][vehicle.getBack().getCol()] = vehicle.getChar();
                if(diff>1){
                    for(int i = 1; i<diff;i++){
                        board[vehicle.getFront().getRow()-i][vehicle.getFront().getCol()] = vehicle.getChar();
                    }
                }  
            }
        }
        // fill voids with empty symbols
        for(int i =0; i < 6;i++){
            for(int j=0; j < 6;j++){
                if(!Character.isLetter(board[i][j])){
                    board[i][j] = EMPTY_SYMBOL;
                }
            }
        }
    }

    private void notifyObserver(Vehicle vehicle){
        if(observer != null){
            Vehicle newVehicle = new Vehicle(vehicle);
            observer.vehicleMoved(newVehicle);
        }
    }

    /**
     * @return board
     */
    public char[][] getBoard(){
        return board;
    }

    /**
     * prints out the board
     */
    public void printBoard(){
        for(int i =0; i < 6;i++){
            for(int j=0; j < 6;j++){
                if(i==2 && j == 5){
                    System.out.print(board[i][j] + " < Exit");
                }else{
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
        
    }

    /**
     * checks if move is valid - if true
     * checks if vertical
     * checks all possible invalid moves - if invalid move - throw exception
     * otherwise updateBoard and move veichle
     * increment moveCount
     * @param move
     * @throws RushHourException
     */
    public void moveVehicle(Move move) throws RushHourException{
        
        //check if the vehicle you are trying to move is vertical
        boolean isValid = false;
        for (Character each: VehicleInfo.keySet()){
            if (move.getSymbol() == each){
                isValid = true;
            }
        }
        if (isValid == false){ 
            throw new RushHourException("Invalid Symbol");
        }
        
        Boolean isVert = VehicleInfo.get(move.getSymbol()).isVert();
        Vehicle boo = VehicleInfo.get(move.getSymbol());

        
        if (isVert == true){
            
            if(move.getDirection() == Direction.LEFT || move.getDirection() == Direction.RIGHT){
             throw new RushHourException("Invalid direction");
         }
            else if(move.getDirection() == Direction.UP){
                    if(boo.getBack().getRow() -1 <0 ){
                        throw new RushHourException("You are at the edge ");
                    }
                    if (board[boo.getBack().getRow()-1][boo.getBack().getCol()] != EMPTY_SYMBOL){
                        throw new RushHourException("Another Vehicle is there");
              }
                    updateBoard(move);
                    boo.move(move.getDirection());
                    notifyObserver(boo);
                    //printBoard();
         }
            else if (move.getDirection() == Direction.DOWN){
                if (boo.getFront().getRow() +1 >5  ){
                    throw new RushHourException("You are at the edge ");
                }
                if (board[boo.getFront().getRow()+1][boo.getFront().getCol()] != EMPTY_SYMBOL){
                    throw new RushHourException("Another Vehicle is there");
                }
                updateBoard(move);
                boo.move(move.getDirection());
                notifyObserver(boo);
                //printBoard();
            } 
        }
        else{
            if(move.getDirection() == Direction.UP || move.getDirection() == Direction.DOWN){
                 throw new RushHourException("Invalid direction");
             }
        else if(move.getDirection() == Direction.LEFT){
                if(boo.getBack().getCol() -1 <0 ){
                    throw new RushHourException("You are at the edge");
                }
                if (board[boo.getBack().getRow()][boo.getBack().getCol()-1] != EMPTY_SYMBOL){
                    throw new RushHourException("Another Vehicle is there");
                }
                updateBoard(move);
                boo.move(move.getDirection());
                notifyObserver(boo);
                //printBoard();
        }
        else if (move.getDirection() == Direction.RIGHT){
                if (boo.getFront().getCol() +1 >5){
                    throw new RushHourException("You are at the edge");
                }
                if (board[boo.getFront().getRow()][boo.getFront().getCol()+1] != EMPTY_SYMBOL){
                    throw new RushHourException("Another Vehicle is there");
                }
                updateBoard(move);
                boo.move(move.getDirection());
                notifyObserver(boo);
                //printBoard();
            }
        }
        
        this.moveCount++;
        notifyObserver(boo);
        
    }
    
    /**
     * moves veichle on board
     * adds empty string on space that veichle no longer uses
     * moves veichle 1 over based on given direction
     */
    public void updateBoard(Move move) throws RushHourException{
        Vehicle vehicle = VehicleInfo.get(move.getSymbol());
        Position oldBack = vehicle.getBack();
        Position oldFront = vehicle.getFront();
        if(move.getDirection() == Direction.UP){
            int diff = oldFront.getRow() - oldBack.getRow();
            board[oldBack.getRow()-1][oldBack.getCol()] = move.getSymbol();
            for(int i=1; i < diff+1;i++){
                board[oldBack.getRow()+i][oldBack.getCol()] = move.getSymbol();
            }
            board[oldFront.getRow()][oldFront.getCol()] = EMPTY_SYMBOL;
        }
        if(move.getDirection() == Direction.DOWN){
            int diff = oldFront.getRow() - oldBack.getRow();
            board[oldFront.getRow()+1][oldFront.getCol()] = move.getSymbol();
            for(int i=1; i < diff+1;i++){
                board[oldFront.getRow()-i][oldFront.getCol()] = move.getSymbol();
            }
            board[oldBack.getRow()][oldBack.getCol()] = EMPTY_SYMBOL;
        }
        if(move.getDirection() == Direction.LEFT){
            int diff = oldFront.getCol() - oldBack.getCol();
            board[oldBack.getRow()][oldBack.getCol()-1] = move.getSymbol();
            for(int i=1; i < diff+1;i++){
                board[oldFront.getRow()][oldFront.getCol()-i] = move.getSymbol();
            }
            board[oldFront.getRow()][oldFront.getCol()] = EMPTY_SYMBOL;
        }
        if(move.getDirection() == Direction.RIGHT){
            int diff = oldFront.getCol() - oldBack.getCol();
            board[oldFront.getRow()][oldFront.getCol()+1] = move.getSymbol();
            for(int i=1; i < diff+1;i++){
                board[oldFront.getRow()][oldFront.getCol()+i] = move.getSymbol();
            }
            board[oldBack.getRow()][oldBack.getCol()] = EMPTY_SYMBOL;
        }
        //vehicle.move(move.getDirection());
        notifyObserver(vehicle);
        //this.moveCount++;
        
    }

    /**
     * checks to see if the red vehicle occupies the exit position
     * @return true if the front of the red vehicle is in the exit position
     */
    public boolean isGameOver(){
        for(Vehicle vehicle: vehicles){
            if(vehicle.getChar() == RED_SYMBOL && vehicle.getFront().equals(EXIT_POS)){
                return true;
            }
        }
        return false;
    }

    /**
     * checks if empty symbol is present based on given direction
     * if so, add to possible moves arraylist
     * @return arraylist of possible moves
     */
    public ArrayList<Move> getPossibleMove(){
        ArrayList<Move> possible = new ArrayList<>();
        for(Vehicle v: vehicles){
            if(v.isVert()){
                if(v.getFront().getRow()+1 <6 && board[v.getFront().getRow()+1][v.getFront().getCol()] == EMPTY_SYMBOL){
                    Move m = new Move(v.getChar(), Direction.DOWN);
                    possible.add(m);
                }
                if(board[v.getFront().getRow()-1][v.getFront().getCol()] == EMPTY_SYMBOL){
                    Move m = new Move(v.getChar(), Direction.UP);
                    possible.add(m);
                }
            }else{
                if(board[v.getFront().getRow()][v.getFront().getCol()+1] == EMPTY_SYMBOL){
                    Move m = new Move(v.getChar(), Direction.RIGHT);
                    possible.add(m);
                }
                if(board[v.getFront().getRow()][v.getFront().getCol()-1] == EMPTY_SYMBOL){
                    Move m = new Move(v.getChar(), Direction.LEFT);
                    possible.add(m);
                }
            }
        }
        return possible;
    }

    /**
     * @return move Count
     */
    public int getMoveCount(){
        return this.moveCount;
    }

    public Collection<Vehicle> getVehicles(){
        return Set.copyOf(vehicles);
    }

    public ArrayList<Move> solve(RushHour game) throws IOException{
        RushHourSolver rhs = new RushHourSolver(game);
        Backtracker<RushHourSolver> b = new Backtracker<>(false);
        RushHourSolver solver = b.solve(rhs);
        if (solver != null){
            ArrayList<Move> hints = rhs.getMovesMade();
            
            return hints;
        }else{
            return null;
        }
    }

    public static void main(String[] args) throws IOException, RushHourException {
        
        // RushHour rh = new RushHour("data/03_00.csv");
        // Vehicle[][] newBoard = rh.getBoard();
        // rh.printBoard();
        // System.out.println(rh.getVehicles());
        // Move move1 = new Move('O', Direction.DOWN);
        // Move move2 = new Move('A', Direction.UP);
        // rh.moveVehicle(move1);
        // rh.moveVehicle(move1);
        // rh.moveVehicle(move2);
        // rh.moveVehicle(move1);
        // Move move3 = new Move('R', Direction.RIGHT);
        // rh.moveVehicle(move3);
        // rh.moveVehicle(move3);
    //     Vehicle[][] newBoard = rh.getBoard();
    //     for(int r = 0; r<6; r++){
    //         System.out.println(Arrays.toString(newBoard[r]));
    //     }
    // }
        RushHour rh = new RushHour("data/11_00.csv");
        rh.printBoard();
    }
}
