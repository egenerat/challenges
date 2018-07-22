import java.util.*;
import java.util.stream.Collectors;

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

      public String toString() {
        return "Node: " + id;
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

    public List<Integer> shortestPath(int nodeStart, int nodeTarget) {
      Queue<Node> queue = new LinkedList<>();
      Set<Node> visitedNodes = new HashSet<>();
      Node root = getNode(nodeStart);
      Map<Node, Node> pathToNodes = new HashMap<>();

      visitedNodes.add(root);
      queue.add(root);
      List<Integer> result = null;
      Node subtreeRoot;
      while (result == null && (subtreeRoot = queue.poll()) != null) {
        for (Node neighbour: subtreeRoot.neighbours) {
          if (neighbour.id == nodeTarget) {
            pathToNodes.put(neighbour, subtreeRoot);
            result = buildPath(pathToNodes, neighbour);
          }
          if (!visitedNodes.contains(neighbour)) {
            pathToNodes.put(neighbour, subtreeRoot);
            queue.add(neighbour);
            visitedNodes.add(neighbour);
          }
        }
      }
      return result;
    }

    private List<Integer> buildPath(Map<Node, Node> paths, Node end) {
      List<Integer> result = new Stack<>();
      Node current = end;
      result.add(end.getId());

      while ((current = paths.get(current)) != null) {
        result.add(0, current.getId());
      }
      return result;
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
    System.out.println(g.shortestPath(1, 5));
  }
}