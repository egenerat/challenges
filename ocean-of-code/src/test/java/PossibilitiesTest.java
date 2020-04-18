import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;

public class PossibilitiesTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName
     *          name of the test case
     */
    public PossibilitiesTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PossibilitiesTest.class);
    }

    public void testSpreadEmpty() {
        boolean[][] originalArea = new boolean[4][4];
        Possibilities original = new Possibilities(originalArea, 0);
        boolean[][] expectedArea = new boolean[4][4];
        Possibilities expected = new Possibilities(expectedArea, 0);
        assertEquals(original.spread(1), expected);
    }

    public void testSpreadFull() {
        boolean[][] originalArea = new boolean[4][4];
        for (boolean[] row: originalArea) {
            Arrays.fill(row, true);
        }
        Possibilities original = new Possibilities(originalArea, 16);
        boolean[][] expectedArea = new boolean[4][4];
        for (boolean[] row: expectedArea) {
            Arrays.fill(row, true);
        }
        Possibilities expected = new Possibilities(expectedArea, 16);
        assertEquals(original.spread(1), expected);
    }

    public void testSpreadUsualCaseDistance1() {
        boolean[][] originalArea = {
                { false, false, false, false },
                { false, true, false, false },
                { false, false, false, false },
                { false, false, false, true }
        };
        Possibilities original = new Possibilities(originalArea, 2);
        boolean[][] expectedArea = {
                { false, true, false, false },
                { true, true, true, false },
                { false, true, false, true },
                { false, false, true, true }
        };
        Possibilities expected = new Possibilities(expectedArea, 8);
        assertEquals(expected, original.spread(1));
    }

    public void testSpreadUsualCaseDistance2() {
        boolean[][] originalArea = {
                { false, false, false, false },
                { false, true, false, false },
                { false, false, false, false },
                { false, false, false, true }
        };
        Possibilities original = new Possibilities(originalArea, 2);
        boolean[][] expectedArea = {
                { false, true, false, false },
                { true, true, true, true },
                { false, true, false, true },
                { false, true, true, true }
        };
        Possibilities expected = new Possibilities(expectedArea, 10);
        assertEquals(expected, original.spread(2));
    }
}
