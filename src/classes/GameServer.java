package classes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
Represent all the backend game information that the client supply us.
 */

public class GameServer {
    private int moves;
    private int grade;
    private int gameLevel;
    private final String graph;
    private final int agentsSize;

    public GameServer(int moves, int grade, int gameLevel, String graph, int agentsSize) {
        this.moves = moves;
        this.grade = grade;
        this.gameLevel = gameLevel;
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

    public String getGraph() {
        return graph;
    }

    public int getAgentsSize() {
        return agentsSize;
    }

}
