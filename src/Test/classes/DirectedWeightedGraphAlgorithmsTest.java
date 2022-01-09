package classes;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DirectedWeightedGraphAlgorithmsTest {
    NodeData[] nodes;
    DirectedWeightedGraph g;
    private static Random _rand;


    public static void initSeed(long seed) {
        _rand = new Random(seed);
    }

    @BeforeEach
    void Initialization() {
        initSeed(0);
        g = new DirectedWeightedGraph();
    }


    @Test
    void shortestPathDist() {
        g = graphCreator(6);
        g.connect(0, 1, 3);
        g.connect(1, 2, 2);
        g.connect(2, 3, 1);
        g.connect(3, 5, 1);
        g.connect(1, 5, 8);
        g.connect(5, 1, 8);
        g.connect(1, 4, 1);
        g.connect(4, 0, 3);
        DirectedWeightedGraphAlgorithms dwa = new DirectedWeightedGraphAlgorithms();
        dwa.init(g);
        assertEquals(7, dwa.shortestPathDist(0, 5));
        g.removeEdge(0, 1);
        assertEquals(-1, dwa.shortestPathDist(0, 5));

    }

    @Test
    void shortestPath() {
        DirectedWeightedGraph g1 = graphCreator(13);
        g1.connect(0, 1, 3);
        g1.connect(1, 2, 2);
        g1.connect(2, 3, 1);
        g1.connect(3, 5, 1);
        g1.connect(1, 5, 8);
        g1.connect(5, 1, 8);
        g1.connect(5, 10, 6);
        g1.connect(0, 10, 1);
        g1.connect(10, 12, 10);
        g1.connect(12, 6, 2);
        g1.connect(6, 9, 12);
        g1.connect(9, 6, 12);
        g1.connect(3, 9, 20);
        g1.connect(11, 9, 10);
        g1.connect(12, 11, 5);
        g1.connect(11, 8, 3);
        g1.connect(8, 3, 4);
        DirectedWeightedGraphAlgorithms dwa1 = new DirectedWeightedGraphAlgorithms();
        dwa1.init(g1);
        StringBuilder actual = new StringBuilder();
        String expected = "->1->2->3->5->10->12";
        List<NodeData> lst = dwa1.shortestPath(1, 12);
        for (NodeData nodeData : lst) {
            actual.append("->").append(nodeData.getKey());
        }
        g1.removeNode(5);
        assertNull(dwa1.shortestPath(1, 12));
        assertEquals(expected, actual.toString());
    }

    @Test
    void center() {
        DirectedWeightedGraphAlgorithms dwa = new DirectedWeightedGraphAlgorithms();
        dwa.init(g);
        DirectedWeightedGraph dwg = graphCreator(4);
        dwg.connect(1, 0, 1);
        dwg.connect(0, 1, 1);
        dwg.connect(1, 3, 1);
        dwg.connect(3, 2, 1);
        dwg.connect(2, 0, 1);
        dwa.init(dwg);
        assertEquals(1, dwa.center().getKey());
    }


    @Test
    void load() {
        DirectedWeightedGraphAlgorithms dwa = new DirectedWeightedGraphAlgorithms();
        dwa.init(g);
        assertFalse(dwa.load("data/A0"));
        assertTrue(dwa.load("{"
                + "  \"Edges\": ["
                + "    {"
                + "      \"src\": \"0\","
                + "      \"w\": \"1.232037506070033\","
                + "      \"dest\" : \"1\""
                + "    },"
                + "    {"
                + "      \"src\": \"1\","
                + "      \"w\": \"1.232037506070033\","
                + "      \"dest\" : \"0\""
                + "    }"
                + "  ],"
                + "  \"Nodes\": ["
                + "    {"
                + "      \"pos\": \"35.19589389346247,32.10152879327731,0.0\","
                + "      \"id\": \"0\""
                + "    },"
                + "    {"
                + "      \"pos\": \"35.20319591121872,32.10318254621849,0.0\","
                + "      \"id\": \"1\""
                + "    }"
                + "  ]"
                + "}"));

    }


    /**
     * Bank of nodes;
     */
    private void nodeCreator(int size) {
        nodes = new NodeData[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = new classes.NodeData(i, new GeoLocation(_rand.nextDouble() + 35, _rand.nextDouble() + 35, 0.0));

        }
    }

    /**
     * Graph creator without any edges!
     */
    private DirectedWeightedGraph graphCreator(int numOfNodes) {
        DirectedWeightedGraph gr = new DirectedWeightedGraph();
        nodeCreator(numOfNodes);
        for (NodeData node : this.nodes) {
            gr.addNode(node);
        }
        return gr;
    }

}