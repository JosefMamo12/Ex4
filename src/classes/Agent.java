package classes;


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
        this.value = value;
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

