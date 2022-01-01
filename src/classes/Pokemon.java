package classes;

import Gui.GraphDraw;
import Util.Point3D;
import Util.Range2Range;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Pokemon {
    private double value;
    private int type;
    private GeoLocation pos;
    private EdgeData edge;

    public Pokemon(double value, int type, GeoLocation pos) {
        this.pos = pos;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }

    public EdgeData getEdge() {
        return edge;
    }

    public void setEdge(EdgeData relatedEdge) {
        this.edge = relatedEdge;
    }
}
