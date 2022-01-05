package ex4_java_client; /**
 * @author Joseph Mamo
 * A trivial example for starting the server and running all needed commands
 */


import Gui.MyFrame;
import classes.*;


import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;


public class StudentCode implements Runnable {
    int moveCounter = 0;
    private GameManger gameManger;
    private MyFrame frame;
    private HashMap<Integer, Integer> agentDest;
    private long waitingTime;


    public static void main(String[] args) {
        Thread ex4 = new Thread(new StudentCode());
        ex4.start();

    }


    private void init() {
        agentDest = new HashMap<>();
        Client client = new Client();
        waitingTime = 100;
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameManger = new GameManger(client);
        GameServer gameServer = GameManger.loadGameServer(client.getInfo());
        gameManger.loadGraph();
        gameManger.upadtePokemons();
        gameManger.addAgents(gameServer.getAgentsSize(), agentDest);

        frame = new MyFrame(gameManger);


    }

    @Override
    public void run() {
        init();
        gameManger.start();
        System.out.println(gameManger.getInfo());
        while (gameManger.isRunning()) {
            gameManger.update();
            moveAgents();

            try {
                if (allAgentGotDest()) {
                    gameManger.move();
                }
                Thread.sleep(100);

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

    private void moveAgents() {

        int id = -1;

        for (Agent agent : gameManger.getAgents()) {
            id = agent.getId();
            int dest = agent.getDest();
            if (dest == -1) {
                if (agent.getSrc() == agentDest.get(id)) {
                    agentDest.put(id, -1);
                }
                int nextNode = nextAgent(agent);
                gameManger.chooseNextEdge(id, nextNode);
                System.out.println();
                System.out.println("Agent: " + id + " currnet node: " + agent.getSrc() + " Go To Node: " + nextNode + " current value: " + agent.getValue());

            }
        }
    }

    private int nextAgent(Agent agent) {
        PriorityQueue<Pokemon> pq = new PriorityQueue<>(new pokemonComp().reversed());
        for (Pokemon p : gameManger.getPokemons()) {
            if (!agentDest.containsValue(p.getEdge().getDest()) || agentDest.get(agent.getId()) == p.getEdge().getDest()) {
                p.setDistance(gameManger.getGraphAlgo().shortestPathDist(agent.getId(), p.getEdge().getDest()));
                System.out.println("Agent pos: " + agent.getSrc() + ", Pokemon edge dest: " + p.getEdge().getDest() + ", PokemonDistance " + p.getDistance() + ", Type: " + p.getType());
                pq.add(p);
            }
        }
        ArrayList<NodeData> lst = null;
        if (!pq.isEmpty()) {
            Pokemon pk = pq.poll();
            agentDest.put(agent.getId(), pk.getEdge().getDest());
            if (agent.getSrc() == pk.getEdge().getDest()) {
                agent.setValue(pk.getValue());
                return pk.getEdge().getSrc();
            } else {
                lst = new ArrayList<>(gameManger.getGraphAlgo().shortestPath(agent.getSrc(), pk.getEdge().getDest()));
            }
        }

        assert lst != null;
        return lst.get(1).getKey();
    }

    public static class pokemonComp implements Comparator<Pokemon> {
        @Override
        public Comparator<Pokemon> reversed() {
            return Comparator.super.reversed();
        }

        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return Double.compare(p1.getValue() / p1.getDistance(), p2.getValue() / p2.getDistance());
        }
    }
}
