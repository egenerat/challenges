import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestGraph extends TestCase {
    /**
     * Create the test case
     *
     * @param testName
     *          name of the test case
     */
    public TestGraph(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestGraph.class);
    }

    public void testGetAvailableMoves() {
        Graph g = new Graph();
        g.addLink(1, 2);
        g.addLink(1, 3);
        assertEquals(2, g.getNeighbours(1).size());
        assertEquals(1, g.getNeighbours(3).size());
    }
}
