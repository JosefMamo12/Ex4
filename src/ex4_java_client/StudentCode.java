package ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */

import classes.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class StudentCode {
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
                System.out.println(agent);
                String [] pos_str = agent1.get("pos").getAsString().split(",");
                double x = Double.parseDouble(pos_str[0]), y = Double.parseDouble(pos_str[1]), z = Double.parseDouble(pos_str[2]);
                Agent agent2 = new Agent(agent1.get("id").getAsInt(), agent1.get("value").getAsDouble(), agent1.get("src").getAsInt(), agent1.get("dest").getAsInt(), agent1.get("speed").getAsInt(), new GeoLocation(x, y, z));
                agents.add(agent2);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return agents;
    }
    private static ArrayList<Pokemon> loadPokemons(String pokemonsStr) {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        JsonParser jp = new JsonParser();
        try {
            Object obj = jp.parse(pokemonsStr);
            JsonObject jo = (JsonObject) obj;
            JsonArray jap = (JsonArray)jo.get("Pokemons");
            for (int i = 0; i < jap.size(); i++){
                JsonObject poke1 =(JsonObject) jap.get(i);
                JsonObject poke2 = (JsonObject) poke1.get("Pokemon");
                String [] str_pos = poke2.get("pos").getAsString().split(",");
                double x = Double.parseDouble(str_pos[0]), y = Double.parseDouble(str_pos[1]),z = Double.parseDouble(str_pos[2]);
                Pokemon p = new Pokemon(poke2.get("value").getAsDouble(),poke2.get("type").getAsInt(),new GeoLocation(x,y,z));
                pokemons.add(p);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return pokemons;
    }

    public static void main(String[] args) {
        DirectedWeightedGraph graph = new DirectedWeightedGraph();
        DirectedWeightedGraphAlgorithms graphAlgo = new DirectedWeightedGraphAlgorithms();
        graphAlgo.init(graph);

        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String graphStr = client.getGraph();
        System.out.println(graphStr);

        graphAlgo.load(graphStr);
        client.addAgent("{\"id\":0}");

        String agentsStr = client.getAgents();
        ArrayList<Agent> agents =  loadAgents(agentsStr);

        String pokemonsStr = client.getPokemons();
        ArrayList<Pokemon> pokemons = loadPokemons(pokemonsStr);

        String isRunningStr = client.isRunning();
        System.out.println(isRunningStr);
        client.start();

        while (client.isRunning().equals("true")) {

            client.move();
            System.out.println(client.getAgents());
            System.out.println(client.timeToEnd());

            Scanner keyboard = new Scanner(System.in);
            System.out.println("enter the next dest: ");
            int next = keyboard.nextInt();
            client.chooseNextEdge("{\"agent_id\":0, \"next_node_id\": " + next + "}");

        }
    }




}
