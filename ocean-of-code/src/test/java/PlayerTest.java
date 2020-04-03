import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class PlayerTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName
     *          name of the test case
     */
    public PlayerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PlayerTest.class);
    }

    public void testTemplate() {
        Board board = new Board(new ArrayList<String>());
//        X range: 0 -> 4
//        Y range: 0 -> 4
//        X range: 10 -> 14
//        Y range: 0 -> 4
//        X range: 10 -> 14
//        Y range: 10 -> 14
        board.convertSectorToArea(1);
        board.convertSectorToArea(3);
        board.convertSectorToArea(9);
        assertTrue(true);
    }
}
