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
}
