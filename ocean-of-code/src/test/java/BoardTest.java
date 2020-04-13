import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
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
        Board board = new Board(new ArrayList<>());
//        X range: 0 -> 4
//        Y range: 0 -> 4
//        X range: 10 -> 14
//        Y range: 0 -> 4
//        X range: 10 -> 14
//        Y range: 10 -> 14
        board.convertSectorToArea(1);
        board.convertSectorToArea(3);
        board.convertSectorToArea(9);
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

        assertEquals(4, board.getAvailableMoves(current).size());
    }
}
