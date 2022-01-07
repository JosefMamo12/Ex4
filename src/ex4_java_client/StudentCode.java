package ex4_java_client; /**
 * @author Joseph Mamo
 * A trivial example for starting the server and running all needed commands
 */


import Gui.MyFrame;
import classes.*;

import java.io.IOException;
import java.util.*;


public class StudentCode implements Runnable {
    int moveCounter = 0;
    int waitingTime = 100;
    private GameManger gameManger;
    private MyFrame frame;
    private HashMap<Integer, Integer> agentDest;


    public static void main(String[] args) {
        Thread ex4 = new Thread(new StudentCode());
        ex4.start();

    }


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

    private void moveAgents() {
        if (allAgentGotDest()) {
            gameManger.move();
            moveCounter++;
            if (moveCounter % 10 == 1)
                waitingTime = 30;
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

    private synchronized int nextAgent(Agent agent) {
        PriorityQueue<Pokemon> pq = new PriorityQueue<>(new pokemonComp());
        for (Pokemon p : gameManger.getPokemons()) {
            gameManger.updatePokemons();
            if (!agentDest.containsValue(p.getEdge().getDest()) || (agent.getDest() != agentDest.get(agent.getId()))) {
                double distance = gameManger.getGraphAlgo().shortestPathDist(agent.getSrc(), p.getEdge().getDest());
                p.setDistance(distance);
                pq.add(p);
            }
        }

        ArrayList<NodeData> lst = null;
        if (!pq.isEmpty()) {
            Pokemon pk = pq.poll();
            agentDest.put(agent.getId(), pk.getEdge().getDest());
            if (agent.getSrc() == pk.getEdge().getSrc()) {
                agent.setValue(pk.getValue());
                return pk.getEdge().getDest();
            } else {
                System.out.println("Agent: " + agent.getId() + " Go to " + pk);
                lst = new ArrayList<>(gameManger.getGraphAlgo().shortestPath(agent.getSrc(), pk.getEdge().getSrc()));
            }
        }
        if (lst == null) {
            LinkedList<EdgeData> list = new LinkedList<>(gameManger.getGraph().getEdgeOut(agent.getSrc()));
            return list.getFirst().getDest();
        }
        return lst.get(1).getKey();

    }


    public static class pokemonComp implements Comparator<Pokemon> {
        @Override
        public Comparator<Pokemon> reversed() {
            return Comparator.super.reversed();
        }

        @Override
        public int compare(Pokemon p1, Pokemon p2) {
            return Double.compare(p1.getDistance(), p2.getDistance());
        }
    }
}
