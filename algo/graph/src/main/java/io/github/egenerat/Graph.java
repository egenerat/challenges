package io.github.egenerat;

import java.util.*;

public class Graph {
    private Map<Integer, Vertex> vertices = new HashMap<>();

    public void addNode(Vertex n) {
        vertices.put(n.getId(), n);
    }

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph " + vertices.size() + " vertices\n");
        vertices.entrySet().stream().forEach(x -> sb.append(x.getValue().prettyPrint()));
        return sb.toString();
    }

    public int dijkstra(int begin, int end) {
        Set<Vertex> unvisitedVertex = new HashSet<>();
        unvisitedVertex.addAll(vertices.values());

        Map<Vertex, Integer> distances = new HashMap<>();
        vertices.entrySet().stream().forEach( vertex -> {
            if (vertex.getKey().equals(begin)) {
                distances.put(vertex.getValue(), 0);
            } else {
                distances.put(vertex.getValue(), Integer.MAX_VALUE);
            }
        });
        boolean isFound = false;
        Vertex current = vertices.get(begin);
        while (!isFound) {
            unvisitedVertex.remove(current);
            current.getNeighbours().entrySet().stream()
                    .filter(entry -> !unvisitedVertex.contains(entry.getKey()))
                    .filter(unvisitedNeighbour ->
                            distances.get(current) + unvisitedNeighbour.getValue() < distances.get(unvisitedNeighbour.getKey())
                    )
                    .forEach(unvisitedNeighbour -> distances.put(unvisitedNeighbour.getKey(), distances.get(current) + unvisitedNeighbour.getValue()) );
            unvisitedVertex.remove(current);
            if (current.getId() == end) {
                return distances.get(current);
            }
            // set current
//            current = unvisitedVertex.
        }
        return -1;
    }
}