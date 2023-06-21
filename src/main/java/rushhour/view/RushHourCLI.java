package rushhour.view;

import java.io.IOException;
import java.util.Scanner;

import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;
import rushhour.model.RushHourSolver;


/**
 * This file runs the game in command line
 */

public class RushHourCLI {

    /**
     * 
     * @param args
     * @throws IOException
     * @throws RushHourException
     * The main makes a RushHour object and asks for userinput 
     * it creates a move out of user input and passses it to the moveVehicle method which updates the board
     */
    public static void main(String[] args) throws IOException,RushHourException {
        System.out.print("Enter RushHour filename: ");
        boolean won = true;
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();
        RushHour rh = new RushHour(filename);
        rh.printBoard();
       

        while(!rh.isGameOver() ){
            try{
                String input = scanner.nextLine();
                if (input.equals("Help")){
                    help(); 
                }
                if(input.equals("Hint")){
                    System.out.println("Try " + rh.getPossibleMove().get(0));
                    
                }else if(input.equals("quit")){
                    won = false;
                    break;
                }else if (input.equals("solve")){
                    RushHourSolver rhs = RushHourSolver.solve(rh);
                    
                }else if(input.equals("reset")){
                    RushHour rh2 = new RushHour(filename);
                    rh2.printBoard();
                    continue;
                }
                
                else{
                    
                String[] tokens = input.split(" ");
                Move move = new Move(tokens[0].charAt(0), Direction.valueOf(tokens[1]));
                rh.moveVehicle(move);
                rh.printBoard();
                System.out.println("\n");
                System.out.println("Moves " + rh.getMoveCount());
            }
            }catch(RushHourException e){
                System.out.println(e.getMessage() + "! Try Again");
                continue;
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Typing error! Type 'Help' for help");
                continue;
            }
            catch(IOException e){
                System.out.println("Typing error!");
                continue;
            }
           
        }
        if(won){
            System.out.println("You didn't lose!!!");
        }else{
            System.out.println("You Lost!! GoodBye Liam");
        }
        


        scanner.close();

    }

    /**
     * this method displays all the available commands if you type "Help"
     */
    public static void help(){
        System.out.println("Help Menu:");
        System.out.println("\thelp - this menu");
        System.out.println("\tquit - quit");
        System.out.println("\tsolve - solve");
        System.out.println("\thint - display a valid move");
        System.out.println("\treset - reset the game");
        System.out.println("\t<symbol> <UP|DOWN|LEFT|RIGHT> - move the vehicle one space in the given direction");
    }
}