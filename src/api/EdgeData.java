package api;

/**
 * This interface represents the set of operations applicable on a
 * directional edge(src,dest) in a (directional) weighted graph.
 *
 * @author boaz.benmoshe
 */
public interface EdgeData {
    /**
     * The id of the source node of this edge.
     *
     * @return
     */
    int getSrc();

    /**
     * The id of the destination node of this edge
     *
     * @return
     */
    int getDest();

    /**
     * @return the weight of this edge (positive value).
     */
    double getWeight();
}


