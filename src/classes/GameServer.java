package classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GameServer {
    private final int pokemonsSize;
    private boolean isLoggedIn;
    private int moves;
    private int grade;
    private int gameLevel;
    private int maxUserLever;
    private final float id;
    private final String graph;
    private final int agentsSize;

    public GameServer(int pokemonsSize, boolean isLoggedIn, int moves, int grade, int gameLevel, int maxUserLever, float id, String graph, int agentsSize) {
        this.pokemonsSize = pokemonsSize;
        this.isLoggedIn = isLoggedIn;
        this.moves = moves;
        this.grade = grade;
        this.gameLevel = gameLevel;
        this.maxUserLever = maxUserLever;
        this.id = id;
        this.graph = graph;
        this.agentsSize = agentsSize;
    }

    public void update(String str) {
        JsonParser jp = new JsonParser();
        try {
            Object obj = jp.parse(str);
            JsonObject jo = (JsonObject) obj;
            JsonObject ja = (JsonObject) jo.get("GameServer");
            setMoves(Integer.parseInt(String.valueOf(ja.get("moves"))));
            setGrade(Integer.parseInt(String.valueOf(ja.get("grade"))));
            setGameLevel(Integer.parseInt(String.valueOf(ja.get("game_level"))));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * {
     * "GameServer": {
     * "pokemons": 6,
     * "is_logged_in": false,
     * "moves": 15,
     * "grade": 12,
     * "game_level": 11,
     * "max_user_level": -1,
     * "id": 0,
     * "graph": "data/A2",
     * "agents": 3
     * }
     * }
     **/
    public int getPokemonsSize() {
        return pokemonsSize;
    }

    public boolean getIsLogedIn() {
        return isLoggedIn;
    }

    public void setIsLogedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getMoves() {
        return moves;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }

    public int getMaxUserLever() {
        return maxUserLever;
    }

    public void setMaxUserLever(int maxUserLever) {
        this.maxUserLever = maxUserLever;
    }

    public String getGraph() {
        return graph;
    }

    public int getAgentsSize() {
        return agentsSize;
    }


}
