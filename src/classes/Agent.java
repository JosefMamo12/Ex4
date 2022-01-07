package classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.util.ArrayList;


public class Agent {
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private GeoLocation pos;




    public Agent(int id, double value, int src, int dest, double speed, GeoLocation pos) {
        this.id = id;
        this.value = value;
        this.src = src;
        this.dest = dest;
        this.speed = speed;
        this.pos = pos;

    }


    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value += value;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public double getSpeed() {
        return speed;
    }

    public GeoLocation getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return
                "id=" + id + "\n" +
                ", value=" + value + "\n" +
                ", src=" + src +  "\n" +
                ", dest=" + dest + "\n" +
                ", speed=" + speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

/**
 * returns: json str of agents. for example:
 *
 * <pre>
 * {
 *     "Agents":[
 *         {
 *             "Agent":
 *             {
 *                 "id":0,
 *                 "value":0.0,
 *                 "src":0,
 *                 "dest":1,
 *                 "speed":1.0,
 *                 "pos":"35.18753053591606,32.10378225882353,0.0"
 *             }
 *         }
 *     ]
 * }
 * </pre>
 *
 * @return json str of agents
 */