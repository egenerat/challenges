import java.util.*;
import java.io.*;
import java.math.*;

class Zone {
    int id;
    int platinumSource;
    List<Zone> connectedZones = new ArrayList<>();

    Zone(int id, int platinumSource) {
        this.id = id;
        this.platinumSource = platinumSource;
    }
}

class Graph {
    private static Map<Integer, Zone> graph = new HashMap<>();

    Zone getInstance(int i) {
        return graph.get(i);
    }

    void addZone(Zone z) {
        graph.put(z.id, z);
    }

    void addLink(int i, int j) {
        Zone zone1 = graph.get(i);
        Zone zone2 = graph.get(j);
        graph.get(i).connectedZones.add(zone1);
        graph.get(j).connectedZones.add(zone2);
    }
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int playerCount = in.nextInt(); // the amount of players (2 to 4)
        int myId = in.nextInt(); // my player ID (0, 1, 2 or 3)
        int zoneCount = in.nextInt();
        int linkCount = in.nextInt();
        Graph board = new Graph();
        for (int i = 0; i < zoneCount; i++) {
            int zoneId = in.nextInt(); // this zone's ID (between 0 and zoneCount-1)
            int platinumSource = in.nextInt(); // the amount of Platinum this zone can provide per game turn
            board.addZone(new Zone(zoneId, platinumSource));
        }
        for (int i = 0; i < linkCount; i++) {
            int zone1 = in.nextInt();
            int zone2 = in.nextInt();
            
            board.addLink(zone1, zone2);
        }

        // game loop
        while (true) {
            int platinum = in.nextInt(); // my available Platinum
            for (int i = 0; i < zoneCount; i++) {
                int zId = in.nextInt(); // this zone's ID
                int ownerId = in.nextInt(); // the player who owns this zone (-1 otherwise)
                int podsP0 = in.nextInt(); // player 0's PODs on this zone
                int podsP1 = in.nextInt(); // player 1's PODs on this zone
                int podsP2 = in.nextInt(); // player 2's PODs on this zone (always 0 for a two player game)
                int podsP3 = in.nextInt(); // player 3's PODs on this zone (always 0 for a two or three player game)
            }
            // if (zId == myId) {
            //     System.err.println();
            // }

            // first line for movement commands, second line for POD purchase (see the protocol in the statement for details)
            System.out.println("WAIT");
            System.out.println("1 73");
        }
    }
}