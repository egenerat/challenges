import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName
     *          name of the test case
     */
    public BoardTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(BoardTest.class);
    }

    public void testConvertSectorToArea() {
        List<String> input = new ArrayList<>();
        for (int i=0; i<15; i++) {
            input.add("...............");
        }
        Board board = new Board(input);
        boolean[][] expectedArea = {
                {false, false, false, false, false, false, false, false, false, false, true, true, true, true, true},
                {false, false, false, false, false, false, false, false, false, false, true, true, true, true, true},
                {false, false, false, false, false, false, false, false, false, false, true, true, true, true, true},
                {false, false, false, false, false, false, false, false, false, false, true, true, true, true, true},
                {false, false, false, false, false, false, false, false, false, false, true, true, true, true, true},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}
        };
        assertTrue(Arrays.deepEquals(expectedArea, board.convertSectorToArea(3).area));
        assertEquals(25, board.convertSectorToArea(3).count);
    }

    public void testGetAvailableMoves() {
        List<String> input = new ArrayList<>();
        input.add(".x....");
        input.add("...x..");
        input.add("...x..");
        input.add("......");
        input.add("......");
        input.add("......");
        Board board = new Board(input);
        Coord current = new Coord(0, 0);

        assertEquals(4, board.getAvailableMoves(current, 4).size());
    }
}
