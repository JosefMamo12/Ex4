package classes;

public class Pokemon{
    private final double value;
    private final int type;
    private final GeoLocation pos;
    private EdgeData edge;
    private double distance;


    public Pokemon(double value, int type, GeoLocation pos) {
        this.value = value;
        this.type = type;
        this.pos = pos;
        this.distance = 0;

    }


    public double getValue() {
        return value;
    }

    public void setDistance(double distance) {this.distance = distance;}

    public double getDistance() {
        return distance;
    }

    public int getType() {
        return type;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public EdgeData getEdge() {
        return edge;
    }

    public void setEdge(EdgeData relatedEdge) {
        this.edge = relatedEdge;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "value=" + value + " " +
                "EdgeSrc=" + getEdge().getSrc() +" " +
                "EdgeDest=" + getEdge().getDest() +
                '}';
    }
}
