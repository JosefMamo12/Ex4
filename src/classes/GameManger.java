package classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ex4_java_client.Client;

import java.util.*;

public class GameManger {
    public static final double EPS = 0.001, EPS2 = EPS * EPS * EPS;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Agent> agents;
    private final Client client;
    private final DirectedWeightedGraph graph;
    private final GameServer gameServer;
    private final DirectedWeightedGraphAlgorithms graphAlgo;
    private final PriorityQueue<Pokemon> highestPokeValue;

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public DirectedWeightedGraphAlgorithms getGraphAlgo() {
        return graphAlgo;
    }

    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
    }

    public GameManger(Client client, GameServer gameServer) {
        this.gameServer = gameServer;
        this.client = client;
        pokemons = new ArrayList<>();
        agents = new ArrayList<>();
        graph = new DirectedWeightedGraph();
        graphAlgo = new DirectedWeightedGraphAlgorithms();
        graphAlgo.init(graph);
        highestPokeValue = new PriorityQueue<>((o1, o2) -> -Double.compare(o1.getValue(), o2.getValue()));
    }

    public void chooseNextEdge(int agentId, int nextNode) {
        client.chooseNextEdge("{\"agent_id\":" + agentId + ", \"next_node_id\":" + nextNode + "}");
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public void stop() {
        client.stop();
    }

    public void move() {
        client.move();
    }

    public void start() {
        client.start();
    }

    public boolean isRunning() {
        return client.isRunning().equals("true");
    }

    public void loadGraph() {
        graphAlgo.load(client.getGraph());
    }

    public DirectedWeightedGraph getGraph() {
        return graph;
    }

    public static ArrayList<Agent> loadAgents(String file) {
        ArrayList<Agent> agents = new ArrayList<>();
        JsonParser jp = new JsonParser();
        try {
            Object obj = jp.parse(file);
            JsonObject jo = (JsonObject) obj;
            JsonArray ja = (JsonArray) jo.get("Agents");
            for (int i = 0; i < ja.size(); i++) {
                JsonObject agent = (JsonObject) ja.get(i);
                JsonObject agent1 = (JsonObject) agent.get("Agent");
                String[] pos_str = agent1.get("pos").getAsString().split(",");
                double x = Double.parseDouble(pos_str[0]), y = Double.parseDouble(pos_str[1]), z = Double.parseDouble(pos_str[2]);
                Agent agent2 = new Agent(agent1.get("id").getAsInt(), agent1.get("value").getAsDouble(), agent1.get("src").getAsInt(), agent1.get("dest").getAsInt(), agent1.get("speed").getAsInt(), new GeoLocation(x, y, z));
                agents.add(agent2);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return agents;
    }

    public static ArrayList<Pokemon> loadPokemons(String pokemonsStr) {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        JsonParser jp = new JsonParser();
        try {
            Object obj = jp.parse(pokemonsStr);
            JsonObject jo = (JsonObject) obj;
            JsonArray jap = (JsonArray) jo.get("Pokemons");
            for (int i = 0; i < jap.size(); i++) {
                JsonObject poke1 = (JsonObject) jap.get(i);
                JsonObject poke2 = (JsonObject) poke1.get("Pokemon");
                String[] str_pos = poke2.get("pos").getAsString().split(",");
                double x = Double.parseDouble(str_pos[0]), y = Double.parseDouble(str_pos[1]), z = Double.parseDouble(str_pos[2]);
                Pokemon p = new Pokemon(poke2.get("value").getAsDouble(), poke2.get("type").getAsInt(), new GeoLocation(x, y, z));
                pokemons.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemons;
    }

    public String TimeToEnd() {
        return client.timeToEnd();
    }

    public static GameServer loadGameServer(String file) {
        JsonParser jp = new JsonParser();
        int pokemons = 0;
        boolean isLoggedIn = false;
        int moves = 0;
        int grade = 0;
        int gameLevel = 0;
        int maxUserLevel = 0;
        float id = 0;
        String graph = "";
        int agents = 0;
        try {
            Object obj = jp.parse(file);
            JsonObject jo = (JsonObject) obj;
            JsonObject ja = (JsonObject) jo.get("GameServer");
            pokemons = ja.get("pokemons").getAsInt();
            isLoggedIn = ja.get("is_logged_in").getAsBoolean();
            moves = ja.get("moves").getAsInt();
            grade = ja.get("grade").getAsInt();
            gameLevel = ja.get("game_level").getAsInt();
            maxUserLevel = ja.get("max_user_level").getAsInt();
            id = ja.get("id").getAsFloat();
            graph = ja.get("graph").getAsString();
            agents = ja.get("agents").getAsInt();


        } catch (Exception e) {
            e.printStackTrace();

        }
        return new GameServer(pokemons, isLoggedIn, moves, grade, gameLevel, maxUserLevel, id, graph, agents);
    }

    public void relatedEdge(Pokemon p) {
        int src = 0, dest = 0;
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> nodeRunner : graph.getGraph().entrySet()) {
            for (Map.Entry<Integer, EdgeData> edgeRunner : nodeRunner.getValue().entrySet()) {
                GeoLocation pSrc = (GeoLocation) graph.getNode(edgeRunner.getValue().getSrc()).getLocation();
                GeoLocation pDest = (GeoLocation) graph.getNode(edgeRunner.getValue().getDest()).getLocation();
                GeoLocation pPos = p.getPos();

                double fullDist = pSrc.distance(pDest);
                double sequenceDist = pSrc.distance(pPos) + pPos.distance(pDest);

                if (Math.abs(fullDist - sequenceDist) >= 0 && Math.abs(fullDist - sequenceDist) <= EPS2) {
                    src = edgeRunner.getValue().getSrc();
                    dest = edgeRunner.getValue().getDest();
                }
            }
        }
        if (src > dest) {
            if (p.getType() > 0)
                p.setEdge(graph.getEdge(dest, src));
            else {
                p.setEdge(graph.getEdge(src, dest));
            }
        } else {
            if (p.getType() > 0) {
                p.setEdge(graph.getEdge(src, dest));
            } else {
                p.setEdge(graph.getEdge(dest, src));
            }
        }
    }
    public void updatePokemonsInit() {
        pokemons = loadPokemons(client.getPokemons());
        for (Pokemon p : pokemons) {
            highestPokeValue.add(p);
            relatedEdge(p);
        }
    }

    public void updatePokemons() {
        pokemons = loadPokemons(client.getPokemons());
        for (Pokemon p : pokemons) {
            relatedEdge(p);
        }
    }

    public void update() {
        pokemons = loadPokemons(client.getPokemons());
        for (Pokemon p : pokemons) {
            relatedEdge(p);
        }
        updateAgents();

    }

    @SuppressWarnings("DuplicatedCode")
    public void updateAgents() {
        String file = client.getAgents();
        JsonParser jp = new JsonParser();
        try {
            Object obj = jp.parse(file);
            JsonObject jo = (JsonObject) obj;
            JsonArray ja = (JsonArray) jo.get("Agents");
            for (int i = 0; i < ja.size(); i++) {
                JsonObject agent = (JsonObject) ja.get(i);
                JsonObject agent1 = (JsonObject) agent.get("Agent");
                String[] pos_str = agent1.get("pos").getAsString().split(",");
                double x = Double.parseDouble(pos_str[0]), y = Double.parseDouble(pos_str[1]), z = Double.parseDouble(pos_str[2]);
                agents.get(i).setSrc(agent1.get("src").getAsInt());
                agents.get(i).setSpeed(agent1.get("speed").getAsDouble());
                agents.get(i).setDest(agent1.get("dest").getAsInt());
                agents.get(i).setPos(new GeoLocation(x, y, z));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void addAgents(int agentsSize, HashMap<Integer, Integer> agentBool) {
        for (int i = 0; i < agentsSize; i++) {
            Pokemon p = highestPokeValue.poll();
            assert p != null;
            client.addAgent("{\"id\":" + p.getEdge().getSrc() + "}");// Initial the agent path
            agentBool.put(i, -1);
        }
    }

    public String getInfo() {
        return client.getInfo();
    }

}


