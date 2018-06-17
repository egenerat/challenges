package io.github.egenerat;

import java.util.HashMap;
import java.util.Map;

public class Vertex {
    private int id;
    private Map<Vertex, Integer> neighbours = new HashMap<>();

    public Vertex(int id) {
        this.id = id;
    }

    public void addNeighbour(Vertex v, Integer distance) {
        this.neighbours.put(v, distance);
    }

    public int getId() {
        return this.id;
    }

    public Map<Vertex, Integer> getNeighbours() {
        return neighbours;
    }

    public String toString() {
        return "Vertex " + id;
    }

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertex\n");
        neighbours.forEach((neighbour, distance) ->
                sb.append("  " + id + " => " + neighbour.getId() + " distance: " + distance + "\n")
        );
        return sb.toString();
    }
}