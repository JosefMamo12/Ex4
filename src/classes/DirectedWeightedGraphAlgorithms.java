package classes;

import com.google.gson.*;


import java.util.*;

/**
 * This class representing a bunch of algorithms which need to work on
 * the DirectedWeightedGraph object.
 */
public class DirectedWeightedGraphAlgorithms implements api.DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph g;
    private HashMap<Integer, Integer> parent;
    final double inf = Double.MAX_VALUE;

    /**
     * Init a directedWeightedGraph object which this algorithm class  will work on.
     *
     */
    @Override
    public void init(api.DirectedWeightedGraph g) {
        this.g = (DirectedWeightedGraph) g;
    }

    /**
     * Return the specific graph.
     *
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return this.g;
    }


    /**
     * Clean method like cleaning buffer of the graph before we do a new algorithm.
     */
    public void clean() {
        parent = new HashMap<>();
        Iterator<NodeData> ndItr = g.nodeIter();
        while (ndItr.hasNext()) {
            NodeData curr = ndItr.next();
            parent.put(curr.getKey(), -1);
            curr.setWeight(inf);
            curr.setTag(0);
            curr.setInfo("");
        }
        Iterator<EdgeData> edgeDataIterator = g.edgeIter();
        while (edgeDataIterator.hasNext()) {
            EdgeData ed = edgeDataIterator.next();
            ed.setInfo("");
        }
    }

    /**
     * Center of a graph is the set of all vertices of minimum eccentricity,
     * that is, the set of all vertices u where the greatest distance d(u,v) to other vertices v is minimal.
     * used the wikipedia - https://en.wikipedia.org/wiki/Graph_center
     *
     * @return -> CenterNode of the graph
     */
    @Override
    public NodeData center() {
        clean();
        NodeData centerNode = null;
        double tempSum = inf;
        double sum;
        Iterator<NodeData> nItr = g.nodeIter();
        while (nItr.hasNext()) {
            NodeData bestNode = nItr.next();
            sum = Double.MIN_VALUE;
            DIJKSTRA(bestNode.getKey());
            Iterator<NodeData> nodeDataIterator = g.nodeIter();
            while (nodeDataIterator.hasNext()) {
                NodeData curr = nodeDataIterator.next();
                double sumCalc = curr.getWeight();
                if (sumCalc > sum) {
                    sum = sumCalc;
                }
            }
            if (sum < tempSum) {
                tempSum = sum;
                centerNode = bestNode;
            }
        }
        assert centerNode != null;
        return centerNode;
    }


    /**
     * Known algorithm which given source node, and it is returning the
     * shortest path from the particular source node to all other nodes in the graph.
     * Time Complexity O(V + E*log*V)
     *
     * @param src -> source node
     * @param dest -> destination node
     */

    public void DIJKSTRA(int src, int dest) {
        clean();
        g.getNode(src).setWeight(0);
        PriorityQueue<NodeData> pq = new PriorityQueue<>();
        pq.add(g.getNode(src));
        while (!pq.isEmpty()) {
            NodeData currNode = pq.poll();
            Iterator<EdgeData> itr = g.edgeIter(currNode.getKey());
            if (dest == currNode.getKey())
                break;
            priorityItr(pq, currNode, itr);
        }
    }

    public void DIJKSTRA(int src) {
        clean();
        g.getNode(src).setWeight(0);
        PriorityQueue<NodeData> pq = new PriorityQueue<>();
        pq.add(g.getNode(src));
        while (!pq.isEmpty()) {
            NodeData currNode = pq.poll();
            Iterator<EdgeData> itr = g.edgeIter(currNode.getKey());
            priorityItr(pq, currNode, itr);
        }
    }

    private void priorityItr(PriorityQueue<NodeData> pq, NodeData currNode, Iterator<EdgeData> itr) {
        while (itr.hasNext()) {
            EdgeData e = itr.next();
            if (g.getNode(e.getDest()).getTag() == 0) {
                NodeData neighbour = g.getNode(e.getDest());
                double weight = currNode.getWeight() + e.getWeight();
                if (weight < neighbour.getWeight()) {
                    neighbour.setWeight(weight);
                    parent.put(neighbour.getKey(), currNode.getKey());
                    pq.add(neighbour);
                }
            }
        }
        currNode.setTag(1);
    }

    /**
     * Return the distance between the source node to the dest node, by computing the weights that's exists on each edge
     * that's have been contained in the path.
     * Return -1 if there is no path.
     *
     * @param src  - start node
     * @param dest - end (target) node
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        clean();
        if (src != dest && g != null && g.getNodes().containsKey(src) && g.getNodes().containsKey(dest)) {
            DIJKSTRA(src, dest);
            if (g.getNode(dest).getWeight() != inf)
                return g.getNode(dest).getWeight();
        }
        return -1;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        if (shortestPathDist(src, dest) != -1) {
            List<NodeData> lst = new LinkedList<>();
            while (src != dest) {
                lst.add(g.getNode(dest));
                int tempDest = dest;
                int tempSrc = parent.get(dest);
                this.g.getEdge(tempSrc, tempDest).setInfo("ToPaint");
                dest = tempSrc;
            }
            g.getNode(src).setInfo("Path");
            lst.add(g.getNode(src));
            Collections.reverse(lst);
            return lst;
        }
        return null;
    }


    /**
     * Load a json file to DirectedWeightedGraph object.
     *
     * @param file - file name of JSON file
     */
    @Override
    public boolean load(String file) {
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(file);
            JsonObject dwg = (JsonObject) obj;
            JsonArray jaE = (JsonArray) dwg.get("Edges");
            JsonArray jaN = (JsonArray) dwg.get("Nodes");
            createGraphFromJson(jaN, jaE);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void createGraphFromJson(JsonArray jaN, JsonArray jaE) {
        for (int i = 0; i < jaN.size(); i++) {
            JsonObject node = (JsonObject) jaN.get(i);
            String pos = node.get("pos").getAsString();
            String[] st = pos.split(",");
            GeoLocation nodeLoc = new GeoLocation(Double.parseDouble(st[0]), Double.parseDouble(st[1]), Double.parseDouble(st[2]));
            int id = node.get("id").getAsInt();
            NodeData nd = new NodeData(id, nodeLoc);
            this.g.addNode(nd);
        }
        for (int i = 0; i < jaE.size(); i++) {
            JsonObject edge = (JsonObject) jaE.get(i);
            int src = edge.get("src").getAsInt();
            int dest = edge.get("dest").getAsInt();
            double weight = edge.get("w").getAsDouble();
            this.g.connect(src, dest, weight);
        }
    }
}
