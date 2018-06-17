package io.github.egenerat;

public class Main {
    public static void main(String... args) {
        Graph g = new Graph();
        Vertex n1 = new Vertex(1);
        Vertex n2 = new Vertex(2);
        Vertex n3 = new Vertex(3);
        Vertex n4 = new Vertex(4);
        Vertex n5 = new Vertex(5);

        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);

        n1.addNeighbour(n2, 7);
        n1.addNeighbour(n3, 2);

        n2.addNeighbour(n1, 7);
        n2.addNeighbour(n4, 4);
        n2.addNeighbour(n5, 1);

        n3.addNeighbour(n1, 2);
        n3.addNeighbour(n4, 3);

        n4.addNeighbour(n2, 4);
        n4.addNeighbour(n3, 3);

        n5.addNeighbour(n2, 1);

//        System.out.println(g.prettyPrint());

        g.dijkstra(1, 4);
        // expected answer: distance: 5, path 1 -> 3 > 4
    }
}