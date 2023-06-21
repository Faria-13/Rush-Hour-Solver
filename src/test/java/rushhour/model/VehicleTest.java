package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VehicleTest {
    @Test
    public void testVertical(){
        Position pos1 = new Position(2, 2);
        Position pos2 = new Position(2, 4);
        Vehicle vec = new Vehicle('R', pos1, pos2);
        assertEquals(vec.isVert(), false);
    }


    @Test
    public void testVerticalnot(){
        Position pos1 = new Position(2, 2);
        Position pos2 = new Position(5, 2);
        Vehicle vec = new Vehicle('R', pos1, pos2);
        assertEquals(vec.isVert(), true);
    }
}
