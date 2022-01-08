package classes;

import ex4_java_client.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class GameMangerTest {
    static GameManger gameManger;
    static GameServer gameServer;
    static HashMap<Integer, Integer> dummyMap;

    @AfterAll
    static void afterAll() throws IOException {
        gameManger.getClient().stopConnection();
    }

    @BeforeEach
    void setUp() {
        dummyMap = new HashMap<>();
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        gameServer = GameManger.loadGameServer(client.getInfo());
        gameManger = new GameManger(client, gameServer);
        gameManger.loadGraph();
   }

    @AfterEach
    void tearDown() {
        gameManger.stop();

    }

    /**
     * Case 11 Check
     * 3 Agents
     * 6 Pokemons
     */
    @Test
    void case11Test() {
        ArrayList<Integer> srcCheck = new ArrayList<>();
        gameManger.updatePokemonsInit();
        assertEquals(6, gameManger.getPokemons().size());
        PriorityQueue<Pokemon> pk = new PriorityQueue<>(((o1, o2) -> -Double.compare(o1.getValue(), o2.getValue())));
        pk.addAll(gameManger.getPokemons());
        gameManger.addAgents(gameServer.getAgentsSize(), dummyMap);
        gameManger.setAgents(GameManger.loadAgents(gameManger.getClient().getAgents()));
        for (Agent agent : gameManger.getAgents()) {
            srcCheck.add(agent.getSrc());
        }
        for (int i = 0; i < gameManger.getAgents().size()  && !pk.isEmpty() ; i++) {
            assertTrue(srcCheck.contains(pk.poll().getEdge().getSrc())); //Check if it placed it is checking the related edge also.
        }
        assertEquals(3, gameManger.getAgents().size());
    }

}