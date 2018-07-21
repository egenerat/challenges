import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;;

public class Bfs {

  static class Graph {
    Map<Integer, Node> nodes = new HashMap<>();

    private class Node {
      private int id;
      private List<Node> neighbours;

      public Node(int id) {
        this.id = id;
        neighbours = new ArrayList<>();
      }

      public int getId() {
        return id;
      }

      public void addNeighbour(Node neighbour) {
        neighbours.add(neighbour);
      }

      public List<Node> getNeighbours() {
        return neighbours;
      }
    }

    private Node getNode(int nodeId) {
      if (nodes.get(nodeId) == null) {
        nodes.put(nodeId, new Node(nodeId));
      }
      return nodes.get(nodeId);
    }

    public void addEdge(int nodeId1, int nodeId2) {
      Node node1 = getNode(nodeId1);
      Node node2 = getNode(nodeId2);
      node1.addNeighbour(node2);
      node2.addNeighbour(node1);
    }

    public String prettyPrint() {
      StringBuilder sb = new StringBuilder();
      nodes.forEach((k, v) -> {
        sb.append(k).append(" => ");
        sb.append(v.getNeighbours().stream()
          .map(Node::getId)
          .map(Object::toString)
          .collect(Collectors.joining(", "))
        );
        sb.append("\n");
      });
      return sb.toString();
    }
  }

  public static void main(String... args) {
    Graph g = new Graph();
    g.addEdge(1, 2);
    g.addEdge(2, 4);
    g.addEdge(4, 5);
    g.addEdge(1, 3);
    g.addEdge(3, 5);
    System.out.println(g.prettyPrint());
  }
}