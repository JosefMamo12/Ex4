package ex4_java_client;


import Gui.MyFrame;
import classes.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;


/**
 * Main function that's implements runnable interface.
 * The thread help us to get the information from the server in any time.
 * and to see the graph components move more flow.
 */
public class Main implements Runnable {
    int moveCounter = 0;
    int waitingTime = 100;
    private int centerNode;
    private GameManger gameManger;
    private MyFrame frame;
    private HashMap<Integer, Integer> agentDest;


    public static void main(String[] args) {
        Thread ex4 = new Thread(new Main());
        ex4.start();

    }

    /**
     * Intial all the variables load the graph, load the agents to agents array, load the pokemons,
     * and load all the graphics.
     */
    private void init() {
        agentDest = new HashMap<>();
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameServer gameServer = GameManger.loadGameServer(client.getInfo());
        gameManger = new GameManger(client, gameServer);
        gameManger.loadGraph();
        centerNode = gameManger.getGraphAlgo().center().getKey();
        gameManger.updatePokemonsInit();
        gameManger.addAgents(gameServer.getAgentsSize(), agentDest);
        gameManger.setAgents(GameManger.loadAgents(client.getAgents()));
        frame = new MyFrame(gameManger);


    }

    @Override
    public void run() {

        init();

        gameManger.start();
        while (gameManger.isRunning()) {
            gameManger.update();
            gameManger.getGameServer().update(gameManger.getInfo());
            moveAgents();

            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frame.repaint();
        }
    }

    private boolean allAgentGotDest() {
        for (Map.Entry<Integer, Integer> entry : agentDest.entrySet()) {
            if (entry.getValue() == -1)
                return false;
        }
        return true;
    }

    /**
     * This method chose one agent by the agents order and move
     * the agent to the next edge.
     * Every ten moves the function decrease the waiting time to 30
     * to prevent the stuck problem when agent doesn't eat pokemon on the edge.
     */
    private void moveAgents() {
        if (allAgentGotDest()) {
            gameManger.move();
            moveCounter++;
            if (moveCounter % 10 == 1)
                waitingTime = 20;
            else
                waitingTime = 100;
        }
        for (Agent agent : gameManger.getAgents()) {
            int id = agent.getId();
            int dest = agent.getDest();
            if (dest == -1) {
                if (agent.getSrc() == agentDest.get(id)) {
                    agentDest.put(id, -1);

                }
                int nextNode = nextAgent(agent);
                gameManger.chooseNextEdge(id, nextNode);
                gameManger.updatePokemons();
            }
        }

    }

    /**
     * Using priority queue to get the shortest distance from the current position of the agent to
     * all pokemons then pop it from queue and take the shortest path to the source of the edge that contains the pokemon.
     * The if statement in line 115 avoid the problem of two agents going to the same pokemon.
     * if the agent not have any destination the agent will go to the center node.
     *
     * @param agent -> agent param.
     * @return -> integer next edge
     */
    private int nextAgent(Agent agent) {
        PriorityQueue<Pokemon> pq = new PriorityQueue<>((Comparator.comparingDouble(Pokemon::getDistance)));
        for (Pokemon p : gameManger.getPokemons()) {
            gameManger.updatePokemons();
            if (!agentDest.containsValue(p.getEdge().getDest()) || (agent.getDest() != agentDest.get(agent.getId()))) {
                double distance = gameManger.getGraphAlgo().shortestPathDist(agent.getSrc(), p.getEdge().getSrc());
                p.setDistance(distance);
                pq.add(p);
            }
        }

        ArrayList<NodeData> lst = null;
        if (!pq.isEmpty()) {
            Pokemon pk = pq.poll();
            agentDest.put(agent.getId(), pk.getEdge().getDest());
            if (agent.getSrc() == pk.getEdge().getSrc()) {
                return pk.getEdge().getDest();
            } else {
                System.out.println("Agent: " + agent.getId() + " Go to " + pk);
                lst = new ArrayList<>(gameManger.getGraphAlgo().shortestPath(agent.getSrc(), pk.getEdge().getSrc()));
            }
        }
        if (lst == null) {
            lst = new ArrayList<>(gameManger.getGraphAlgo().shortestPath(agent.getSrc(), centerNode));
        }
        return lst.get(1).getKey();

    }
}
