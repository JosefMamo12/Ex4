package classes;

import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class represnating a bunch of algorithms which need to work on
 * the DirectedWeightedGraph object.
 */
public class DirectedWeightedGraphAlgorithms implements api.DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph g;
    private HashMap<Integer, Integer> parent;
    final double inf = Double.MAX_VALUE;

    /**
     * Init a directedWeightedGraph object which this algortihm class  will work on.
     *
     * @param g
     */
    @Override
    public void init(api.DirectedWeightedGraph g) {
        this.g = (DirectedWeightedGraph) g;
    }

    /**
     * Return the specific graph.
     *
     * @return
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return this.g;
    }

    /**
     * Create a deep copy of the graph.
     * without the mc changes.
     *
     * @return DirectedWeightedGraph
     */
    @Override
    public DirectedWeightedGraph copy() {
        if (g == null)
            return null;
        DirectedWeightedGraph copy = new DirectedWeightedGraph();
        Iterator<NodeData> nodeIt = g.nodeIter();
        while (nodeIt.hasNext()) {
            NodeData node = nodeIt.next();
            copy.addNode(node);
        }

        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : g.getGraph().entrySet()) {
            Integer entryKey = entry.getKey();
            for (Map.Entry<Integer, EdgeData> innerEntry : entry.getValue().entrySet()) {
                EdgeData e = innerEntry.getValue();
                copy.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
        }
        return copy;
    }

    /**
     * Graph is called connected if and only if there is path between u and v which u and v are vertex in the graph.
     * Did it with the help of dfs algorithm which go over all the graph and see it there is a path between a source code
     * to all other nodes in the graph.
     * Also transposed the graph to save the amount of time by using the dfs algorithm to each node.
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (g == null)
            return true;

        HashMap<Integer, Boolean> visited = new HashMap<>();
        fillHashFalse(visited);
        int entryKey = g.getGraph().entrySet().iterator().next().getKey();
        DirectedWeightedGraph gTrans = graphTranspose();
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                DFS(this.g, entryKey, visited);
            }
            if (i == 1) {
                DFS(gTrans, entryKey, visited);
            }
            for (Boolean value : visited.values()) {
                if (!value)
                    return false;
            }
            fillHashFalse(visited);
        }
        return true;
    }

    /**
     * Helper to dfs.
     *
     * @param visited
     */
    private void fillHashFalse(HashMap<Integer, Boolean> visited) {
        Iterator<NodeData> nItr = g.nodeIter();
        while (nItr.hasNext()) {
            NodeData n = nItr.next();
            visited.put(n.getKey(), false);
        }
    }

    /**
     * Helper to is connected graph,
     * return new transposed graph,
     * by opposited all the edges direction.
     *
     * @return
     */
    private DirectedWeightedGraph graphTranspose() {
        DirectedWeightedGraph ans = new DirectedWeightedGraph();
        Iterator<NodeData> nItr = g.nodeIter();
        while (nItr.hasNext()) {
            NodeData curr = nItr.next();
            ans.addNode(new NodeData(curr));
        }
        Iterator<EdgeData> eItr = g.edgeIter();
        while (eItr.hasNext()) {
            EdgeData e = eItr.next();
            ans.connect(e.getDest(), e.getSrc(), e.getWeight());
        }
        return ans;
    }

    /**
     * Helper to isConnected function.
     * getting a soruce node id and and iterate over all his neighbors nood add it to stack and after that
     * it iterating over the neighbors of the neighbor and reach to the all nodes that related to the source Tying
     * element.
     *
     * @param gr
     * @param entryKey
     * @param visited
     */
    private void DFS(DirectedWeightedGraph gr, int entryKey, HashMap<Integer, Boolean> visited) {
        Stack<Integer> st = new Stack<>();
        visited.put(entryKey, true);
        st.add(entryKey);
        while (!st.isEmpty()) {
            int node = st.pop();
            Iterator<EdgeData> itr = gr.edgeIter(node);
            while (itr.hasNext()) {
                EdgeData e = itr.next();
                if (!visited.get(e.getDest())) {
                    visited.put(e.getDest(), true);
                    st.add(e.getDest());
                }
            }
        }
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
     * Known algorithm which given source node and it returning the
     * shortest path from the particular source node to all other nodes in the graph.
     * Time Complexity O(V + E*log*V)
     *
     * @param src
     * @param dest
     */

    public void DIJKSTRA(int src, int dest) {
        clean();
        g.getNode(src).setWeight(0);
        PriorityQueue<NodeData> pq = new PriorityQueue<>();
        pq.add(g.getNode(src));
        while (!pq.isEmpty()) {
            NodeData currNode = pq.poll();
            if(currNode.getKey() == dest)
                break;
            Iterator<EdgeData> itr = g.edgeIter(currNode.getKey());
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
    }

    /**
     * Return the distance between the source node to the dest node, by computing the weights thats exists on each edge
     * thats have been contained in the path.
     * Return -1 if there is no path.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        clean();
        if (src != dest && g != null && g.getNodes().containsKey(src) && g.getNodes().containsKey(dest)) {
            DIJKSTRA(src,dest);
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
     * @return
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
/**
 * returns: json str of agents. for example:
 *
 * <pre>
 * {
 *     "Agents":[
 *         {
 *             "Agent":
 *             {
 *                 "id":0,
 *                 "value":0.0,
 *                 "src":0,
 *                 "dest":1,
 *                 "speed":1.0,
 *                 "pos":"35.18753053591606,32.10378225882353,0.0"
 *             }
 *         }
 *     ]
 * }
 * </pre>
 *
 * @return json str of agents
 */