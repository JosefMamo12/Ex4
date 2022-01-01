package ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */


import Gui.MyFrame;
import classes.*;


import java.io.IOException;
import java.util.ArrayList;


public class StudentCode implements Runnable {
    private static DirectedWeightedGraph graph;
    private static final DirectedWeightedGraphAlgorithms graphAlgo = new DirectedWeightedGraphAlgorithms();
    private GameManger gameManger;
    private MyFrame frame;


    public static void main(String[] args) {

        Thread ex4 = new Thread(new StudentCode());
        ex4.start();

    }


    private void init() {
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
        gameManger.addAgents(gameServer.getAgentsSize());


        frame = new MyFrame(gameManger);

    }

    @Override
    public void run() {
        init();
        gameManger.start();

        while (gameManger.isRunning()) {
            gameManger.update();
            System.out.println("Pokemon pos: "+ gameManger.getPokemons().get(0).getPos());
            System.out.println("Agent pos: "+ gameManger.getAgents().get(0).getPos());
            for (Agent agent : gameManger.getAgents()) {
                if (agent.getDest() == -1) {
                    int next = (agent.getSrc() + 1) % (gameManger.getGraph().nodeSize());
                    System.out.println(next);
                    gameManger.chooseNextEdge(agent.getId(), next);
                }
            }
            try {
                frame.repaint();
                gameManger.move();
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }
}


