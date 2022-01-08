package api;

import classes.NodeData;

import java.util.List;

/**
 * This interface represents a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<NodeData> shortestPath(int src, int dest);
 * 8. load(file); // JSON file
 *
 * @author boaz.benmoshe
 */
public interface DirectedWeightedGraphAlgorithms {
    /**
     * Inits the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    void init(DirectedWeightedGraph g);

    /**
     * Returns the underlying graph of which this class works.
     *
     * @return
     */
    DirectedWeightedGraph getGraph();


    /**
     * Computes the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    double shortestPathDist(int src, int dest);

    /**
     * Computes the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    List<NodeData> shortestPath(int src, int dest);



    /**
     * This method loads a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    boolean load(String file);

    /**
     * Center of a graph is the set of all vertices of minimum eccentricity,
     * that is, the set of all vertices u where the greatest distance d(u,v) to other vertices v is minimal.
     * used the wikipedia - https://en.wikipedia.org/wiki/Graph_center
     *
     * @return
     */

    public NodeData center();
}
