package api;

/**
 * This interface represents the set of operations applicable on a
 * node (vertex) in a (directional) weighted graph.
 *
 * @author boaz.benmoshe
 */
public interface NodeData {
    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    int getKey();

    /**
     * Returns the location of this node, if none return null.
     *
     * @return
     */
    api.GeoLocation getLocation();

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    void setLocation(api.GeoLocation p);

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    double getWeight();

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    void setWeight(double w);

    /**
     * Returns the remark (metadata) associated with this node.
     *
     * @return
     */

    int getTag();

    /**
     * Allows setting the "tag" value for temporal marking a node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    void setTag(int t);
}