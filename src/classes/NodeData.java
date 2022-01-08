package classes;

import api.GeoLocation;

import java.util.Objects;

public class NodeData implements api.NodeData, Comparable<NodeData> {
    private final int key;
    private GeoLocation location;
    private double weight;
    private int tag;


    public NodeData(int key, GeoLocation location) {
        this.key = key;
        this.setLocation(location);
        this.weight = Double.MAX_VALUE;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return key == nodeData.key && Double.compare(nodeData.weight, weight) == 0 && tag == nodeData.tag && Objects.equals(getLocation(), nodeData.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, getLocation(), weight,  tag);
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        weight = w;
    }


    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    @Override
    public int compareTo(NodeData o) {
        if (this.weight > o.weight) return 1;
        else if (this.weight < o.weight) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return   "" + key ;
    }
}