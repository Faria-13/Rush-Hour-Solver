package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.IOException;

import org.junit.jupiter.api.Test;

public class RushHourTest {
    @Test
    public void testBoardPosition() throws IOException, RushHourException{
        RushHour rh = new RushHour("data/03_00.csv");
        
        Move move1 = new Move('O', Direction.DOWN);
        rh.moveVehicle(move1);
        

        char[][] board = rh.getBoard();
        char Expected = 'O';
        char Actual = board[1][2];
        

        assertEquals(Expected, Actual);
        
    }

    @Test
    public void testInitialBoardPos() throws IOException, RushHourException{
        RushHour rh = new RushHour("data/03_00.csv");
        

        char[][] board = rh.getBoard();
       

        assertEquals(board[2][4], '-');
    }
}
