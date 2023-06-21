package rushhour.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import backtracker.Backtracker;
import backtracker.Configuration;

public class RushHourSolver implements Configuration<RushHourSolver> {

    private RushHour game;
    private ArrayList<Move> MovesMade;
    private int moveCount;
    private static int i=0;

    /**
     * 
     * @return a list with all the moves that lead up to a solution
     */

    public ArrayList<Move> getMovesMade() {
        return MovesMade;
    }
    /**
     * basic constructor
     * @param game
     * @throws IOException
     */
    public RushHourSolver(RushHour game) throws IOException{
        this.game = new RushHour(game);
        this.MovesMade = new ArrayList<>();
        this.moveCount = game.getMoveCount();
    }
    /**
     * copy constructor
     * @param game
     * @param MovesMade
     * @param move
     * @throws IOException
     * @throws RushHourException
     */
    public RushHourSolver(RushHour game, ArrayList<Move> MovesMade, Move move) throws IOException, RushHourException{
        this.game = new RushHour(game);
        game.moveVehicle(move);
        this.MovesMade = new ArrayList<>(MovesMade);
        this.moveCount = game.getMoveCount();
        this.MovesMade.add(move);
        
        
    }
    /**
     * returns a list of possible configurations
     */
    @Override
    public Collection<RushHourSolver> getSuccessors(){
        
        ArrayList<RushHourSolver> successsors = new ArrayList<>();
        ArrayList<Move> possibleMoves = game.getPossibleMove();
        for(Move move: possibleMoves){
            
            try {
                RushHourSolver rhs = new RushHourSolver(game,this.MovesMade, move); 
                
                successsors.add(rhs);
            } catch (IOException e) {} catch (RushHourException e) {}}
            
        return successsors;
    }
    /**
     * checks if the movecount was incremented, only then we say a move was valid
     */
    @Override
    public boolean isValid() {
       if  (this.moveCount == game.getMoveCount()){
            i++;
            return true;
       }
       return false;
    }
    /**
     * invokes the game's method of isGameOver
     */
    @Override
    public boolean isGoal() {
        return game.isGameOver();
    }
    /**
     * This to String returns the board, and the previous move list, and current move made
     */
    @Override
    public String toString(){
        game.printBoard();
        System.out.println("Index "+ i);
        System.out.println(MovesMade.toString());
        if(MovesMade.size()>0){
            Move move =  MovesMade.get(i-1);
            return move.getSymbol() + " " + move.getDirection();
            //return MovesMade.toString();
        }
        return "No moves made.";
    }
    /**
     * this makes an instance of the solver and asks the backstracker to solve it
     * @param game
     * @return the winning config
     * @throws IOException
     */
    public static RushHourSolver solve(RushHour game) throws IOException{
        RushHourSolver rhs = new RushHourSolver(game);
        Backtracker<RushHourSolver> b = new Backtracker<>(true);
        b.solve(rhs);
        return rhs;
    }

    public static void main(String[] args) throws IOException {
        //RushHour rh = new RushHour("data/03_00.csv");
        //RushHourSolver rhs = RushHourSolver.solve(rh);
        
        
    }
    
}
