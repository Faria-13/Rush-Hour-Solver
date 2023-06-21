package rushhour.model;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

public class PositionTest {
    @Test
    public void getColTest(){
        //setup
        Position position = new Position(2, 3);
        int expected = 3;
        //invoke
        int actual = position.getCol();
        //analyze
        assertEquals(expected, actual);

    }

    @Test
    public void getRowTest(){
        //setup
        Position position = new Position(2, 3);
        int expected = 2;
        //invoke
        int actual = position.getRow();
        //analyze
        assertEquals(expected, actual);

    }

    @Test
    public void toStringTest(){
        //setup
        Position position = new Position(2, 3);
        String expected = "row, col: 2,3";
        //invoke
        String actual = position.toString();
        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void hashTest(){
        //setup
        Position position = new Position(2, 3);
        int expected = 8;
        //invoke
        int actual = position.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void equalsTest(){
        //setup
        Position position = new Position(2, 3);
        Position position2 = new Position(2, 3);
        boolean expected = true;
        //invoke
        boolean actual = position.equals(position2);
        assertEquals(expected, actual);

    }
}
