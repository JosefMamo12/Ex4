package classes;

public class Pokemon {
    private double value;
    private int type;
    private GeoLocation pos;

    public Pokemon(double value, int type, GeoLocation pos) {
        this.value = value;
        this.type = type;
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
}
