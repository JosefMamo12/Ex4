package ex4_java_client; /**
 * @author Joseph Mamo
 * A trivial example for starting the server and running all needed commands
 */


import Gui.MyFrame;
import classes.*;


import java.io.IOException;
import java.util.*;


public class StudentCode implements Runnable {
    private GameManger gameManger;
    private MyFrame frame;
    private HashMap<Integer, Stack<Integer>> agentPath;
    private HashMap<Integer, Boolean> agentBool;
    private HashMap<Integer, Integer> agentDest;


    public static void main(String[] args) {
        Thread ex4 = new Thread(new StudentCode());
        ex4.start();

    }


    private void init() {
        agentBool = new HashMap<>();
        agentPath = new HashMap<>();
        agentDest = new HashMap<>();
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameManger = new GameManger(client);
        GameServer gameServer = GameManger.loadGameServer(client.getInfo());
        gameManger.loadGraph();
        gameManger.upadtePokemons();
        gameManger.addAgents(gameServer.getAgentsSize(), agentPath, agentDest);
//        gameManger.update();


        frame = new MyFrame(gameManger);


    }

    @Override
    public void run() {
        init();
        gameManger.start();

        while (gameManger.isRunning()) {

            gameManger.update();
            moveAgents();

            try {
            frame.repaint();
            gameManger.move();
            Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(gameManger.getInfo());

        }


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

            }


        }
    }

    private int nextAgent(Agent agent) {

        PriorityQueue<Pokemon> pq = new PriorityQueue<>(new pokemonComp());
        for (Pokemon p : gameManger.getPokemons()) {
            if (!agentDest.containsValue(p.getEdge().getDest()) || agentDest.get(agent.getId()) == p.getEdge().getDest()) {
                p.setDistance(gameManger.getGraphAlgo().shortestPathDist(agent.getId(), p.getEdge().getDest()));
                pq.add(p);
            }

        }
        ArrayList<NodeData> lst = null;
        if (!pq.isEmpty()) {
            Pokemon pk = pq.poll();
            agentDest.put(agent.getId(), pk.getEdge().getDest());
            if (agent.getSrc() == pk.getEdge().getDest()) {
                return pk.getEdge().getSrc();
            } else {
                lst = new ArrayList<>(gameManger.getGraphAlgo().shortestPath(agent.getSrc(), pk.getEdge().getDest()));
            }

        }
        if (lst == null || lst.isEmpty()) {
            LinkedList<EdgeData> ed = new LinkedList<>(gameManger.getGraph().getEdgeOut(agent.getSrc()));
            return ed.getFirst().getDest();
        }
        return lst.get(1).getKey();
    }

    private List<Integer> convertToIntValue(List<NodeData> list) {
        List<Integer> lst = new Stack<>();
        for (NodeData n : list) {
            lst.add(n.getKey());
        }
        return lst;
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
