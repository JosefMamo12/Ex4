package Tests;

import classes.DirectedWeightedGraph;
import classes.EdgeData;
import classes.GeoLocation;
import classes.NodeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DirectedWeightedGraphTest {
    NodeData[] nodes;
    DirectedWeightedGraph graph;
    private static Random _rand;

    public static void initSeed(long seed) {
        _rand = new Random(seed);
    }

    @BeforeEach
    void Initialization() {
        initSeed(0);
        this.graph = new DirectedWeightedGraph();

    }

    @Test
    void addGetNode() {
        assertNull(graph.getNode(0));
        int size = 15, expected = 0, actual = 0;
        nodeCreator(size);
        for (NodeData node : nodes) {
            graph.addNode(node);
            assertEquals(expected++, graph.getNode(actual++).getKey());
        }
    }

    @Test
    void connect() {
        int size = 15, edgesNumber = 30;
        EdgeData expected;
        nodeCreator(size);
        for (NodeData node : nodes) {
            graph.addNode(node);
        }
        assertNull(graph.getEdge(1, 1));// Should not work
        while (this.graph.edgeSize() < edgesNumber) {
            int l = _rand.nextInt(size);
            int r = _rand.nextInt(size);
            double w = _rand.nextDouble() + 1;
            if (l != r && w > 0)
                graph.connect(l, r, w);
            EdgeData actual = graph.getEdge(l, r);
            if (l != r) expected = new EdgeData(l, r, w);
            else expected = null;
            assertEquals(expected, actual);
        }
    }

    @Test
    void nodeIter() {
        int counter = 0;
        graphCreator(100,2000);
        Iterator<NodeData> nItr = this.graph.nodeIter();
        while(nItr.hasNext()){
            nItr.next();
            counter++;
        }
        assertEquals(100,counter);
        nItr = this.graph.nodeIter();
        while (nItr.hasNext())
            nItr.next();
        graph.connect(5,13,200.233);
        Iterator<NodeData> finalNItr = nItr;
        Throwable exception = assertThrows(ConcurrentModificationException.class, finalNItr::next);
        assertEquals(DirectedWeightedGraph.ERROR,exception.getMessage());

    }/**
     * Because I use the nodeIter function for this so exception will pop at this edgeIter!
     */
    @Test
    void edgeIter() {
        int counter = 0;
        graphCreator(800,500);
        Iterator<EdgeData> eItr = this.graph.edgeIter();
        while(eItr.hasNext()){
            EdgeData e = eItr.next();
            counter++;
        }
        assertEquals(counter,graph.edgeSize());

    }


    @Test
    void removeNode() {
        assertNull(graph.removeNode(0)); // There is now any nodes in this particular graph
        graphCreator(5, 10);
        for (int i = 0; i < 5; i++) {
            NodeData actual = this.graph.removeNode(i);
            NodeData expected = nodes[i];
            assertEquals(expected, actual);// Returning the right Node!
        }

        assertEquals(0, this.graph.nodeSize());
    }

    @Test
    void removeEdgeAndEdgeSize() {
        int edgeCount = 0;
        assertNull(graph.removeEdge(0, 1)); // There is now any edges should return null
        graphCreator(10); // CREATE A Graph with 10 nodes and with no edges 0 -> 9 (Graph Nodes)
        graph.connect(0, 1, 1);
        edgeCount++;
        assertNull(graph.removeEdge(1, 0)); // Directed graph opposite direction should return null
        assertEquals(edgeCount, graph.edgeSize());
        graph.connect(0, 1, 1);// should not happen because there is already edge between this two nodes
        edgeCount++;
        assertNotEquals(edgeCount, graph.edgeSize());
        edgeCount--;
        EdgeData expected = new EdgeData(0, 1, 1);
        EdgeData actual = graph.removeEdge(0, 1);
        edgeCount--;
        assertEquals(expected, actual);
        for (int i = 0; i < nodes.length - 2; i++) {
            int j = i + 1;
            int k = j + 1;
            graph.connect(i, j, 1);
            edgeCount++;
            graph.connect(i, k, 1);
            edgeCount++;
        }
        assertEquals(graph.edgeSize(), edgeCount);
        for (int i = 0; i < nodes.length - 2; i++) {
            int j = i + 1;
            int k = j + 1;
            expected = new EdgeData(i, j, 0);
            actual = graph.removeEdge(i, j);
            edgeCount--;
            assertEquals(expected, actual);

            expected = new EdgeData(i, k, 0);
            actual = graph.removeEdge(i, k);
            edgeCount--;
            assertEquals(expected, actual);
        }
        assertEquals(0, graph.edgeSize());
    }

    @Test
    void nodeSize() {
        assertEquals(0, graph.nodeSize());// empty graph check
        int expected = 10;
        graphCreator(10,5 );
        assertEquals(expected, graph.nodeSize()); // creating graph with 10 nodes
        for (int i = 0; i < 5; i++) {
            graph.removeNode(i); // removing some nodes and check if the node size is up-to-date
            assertEquals(expected - i - 1, graph.nodeSize());
        }
    }

    @Test
    void getMC() {
        assertEquals(0, graph.getMC());// Initial status
        int mcCounter = 10;
        graphCreator(mcCounter);
        assertEquals(mcCounter, graph.getMC());//checking addNode mc
        for (int i = 0; i < 5; i++) { //connecting 5 different edges
            graph.connect(i, i + 1, 1); //Example of the graph: 0->1->2->3->4->5->6 7 8 9
            mcCounter++;
        }
        assertEquals(mcCounter, graph.getMC());// checking connect mc
        graph.connect(0, 1, 1);// the mc should not change because there is already edges between this two nodes.
        mcCounter += 2;
        assertEquals(mcCounter, graph.getMC());
        for (int i = 0; i < 5; i++) {
            graph.removeEdge(i, i + 1);
            mcCounter++;
        }
        assertEquals(mcCounter, graph.getMC()); // remove edges mc
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////// PRIVATE FUNCTIONS //////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Bank of nodes;
     *
     */
    private void nodeCreator(int size) {
        nodes = new NodeData[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = new NodeData(i, new GeoLocation(_rand.nextDouble() + 35, _rand.nextDouble() + 35, 0.0));
        }
    }

    /**
     * Graph creator without any edges!
     *
     */
    private void graphCreator(int numOfNodes) {
        nodeCreator(numOfNodes);
        for (NodeData node : this.nodes) {
            graph.addNode(node);
        }
    }

    /**
     * Graph creator with numOfEdge param which means the
     * count of the edges, this graph is contains.
     *
     */
    private void graphCreator(int numOfNodes, int numOfEdges) {
        nodeCreator(numOfNodes);
        for (NodeData node : this.nodes) {
            graph.addNode(node);
        }
        while (this.graph.edgeSize() < numOfEdges) {
            int l = _rand.nextInt(numOfNodes);
            int r = _rand.nextInt(numOfNodes);
            double w = _rand.nextDouble() + 1;
            if (l != r && w > 0)
                graph.connect(l, r, w);
        }
    }

}