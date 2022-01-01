package classes;

public class GameServer {
    private int pokemonsSize;
    private boolean isLoggedIn;
    private int moves;
    private int grade;
    private int gameLevel;
    private int maxUserLever;
    private float id;
    private String graph;
    private int agentsSize;

    public GameServer(int pokemonsSize, boolean isLoggedIn, int moves, int grade, int gameLevel, int maxUserLever,float id, String graph, int agentsSize) {
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


    public int getPokemonsSize() {
        return pokemonsSize;
    }

    public void setPokemonsSize(int pokemonsSize) {
        this.pokemonsSize = pokemonsSize;
    }

    public boolean getIsLogedIn() {
        return isLoggedIn;
    }

    public void setIsLogedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
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
