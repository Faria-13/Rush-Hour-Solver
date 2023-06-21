package rushhour.model;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
public class MoveTest {
    @Test
    public void getSymbolTest(){
        //setup
        Move move = new Move('R', Direction.DOWN);
        char expected = 'R';
        //invoke
        char actual = move.getSymbol();
        //analyze
        assertEquals(expected, actual);
    }
    @Test
    public void getDirectionTest(){
        //setup
        Move move = new Move('R', Direction.DOWN);
        Direction expected = Direction.DOWN;
        //invoke
        Direction actual = move.getDirection();
        //analyze
        assertEquals(expected, actual);
    }
}
