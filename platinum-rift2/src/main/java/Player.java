import java.util.*;

class Graph {
    Map<Integer, List<Integer>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public void addLink(int a, int b) {
        if (!graph.containsKey(a)) {
            graph.put(a, new ArrayList<>());
        }
        if (!graph.containsKey(b)) {
            graph.put(b, new ArrayList<>());
        }
        graph.get(a).add(b);
        graph.get(b).add(a);
    }

    public List<Integer> getNeighbours(Integer nodeId) {
        return graph.get(nodeId);
    }

}

class Player {

    public static void main(String args[]) {
        Graph graph = new Graph();
        List<Integer> visited = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        int playerCount = in.nextInt(); // the amount of players (always 2)
        int myId = in.nextInt(); // my player ID (0 or 1)
        int zoneCount = in.nextInt(); // the amount of zones on the map
        int linkCount = in.nextInt(); // the amount of links between all zones
        for (int i = 0; i < zoneCount; i++) {
            int zoneId = in.nextInt(); // this zone's ID (between 0 and zoneCount-1)
            int platinumSource = in.nextInt(); // Because of the fog, will always be 0
        }
        for (int i = 0; i < linkCount; i++) {
            int zone1 = in.nextInt();
            int zone2 = in.nextInt();
            graph.addLink(zone1, zone2);
        }

        // game loop
        while (true) {
            Map<Integer, Integer> myPods = new HashMap<>();
            Map<Integer, Integer> platinumSources = new HashMap<>();

            int myPlatinum = in.nextInt(); // your available Platinum
            for (int i = 0; i < zoneCount; i++) {
                int zId = in.nextInt(); // this zone's ID
                int ownerId = in.nextInt(); // the player who owns this zone (-1 otherwise)
                int podsP0 = in.nextInt(); // player 0's PODs on this zone
                int podsP1 = in.nextInt(); // player 1's PODs on this zone
                int visible = in.nextInt(); // 1 if one of your units can see this tile, else 0
                int platinum = in.nextInt(); // the amount of Platinum this zone can provide (0 if hidden by fog)
                if (myId == 0 && podsP0 > 0) {
                    myPods.put(zId, podsP0);
                }
                if (myId == 1 && podsP1 > 0) {
                    myPods.put(zId, podsP1);
                }
                if (platinum > 0) {
                    platinumSources.put(zId, platinum);
                }
            }

            platinumSources.forEach((zoneId, platinumQty) -> {
                System.err.println("Platinum in zone " + zoneId + ": " + platinumQty);
            });

            List<String> moves = new ArrayList<>();
            myPods.forEach((zoneId, podsCount) -> {
                List<Integer> neighbours = graph.getNeighbours(zoneId);
                System.err.println(podsCount + " pods and " + neighbours.size() + " neighbours");
                int remainingPods = podsCount;

                for (Integer neighbour: neighbours) {
                    if (remainingPods > 0) {
                        moves.add("1 " + zoneId + " " + neighbour);
                        remainingPods -= 1;
                    }
                }
            });

            // first line for movement commands, second line no longer used (see the protocol in the statement for details)
            System.out.println(String.join(" ", moves));
            System.out.println("WAIT");
        }
    }
}