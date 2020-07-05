import java.util.*;

class Zone {
    int id;
    int ownerId;
    int[] pods = {0, 0, 0, 0};
    int platinumSource;
    List<Zone> connectedZones = new ArrayList<>();

    Zone(int id, int platinumSource) {
        this.id = id;
        this.platinumSource = platinumSource;
    }

    //    Refresh method called at each turn
    void set(int ownerId, int pods0, int pods1, int pods2, int pods3) {
        this.ownerId = ownerId;
        this.pods = new int[]{pods0, pods1, pods2, pods3};
    }

    public String toString() {
        return "Zone " + id + " (" + platinumSource + ")";
    }
}

class Graph {
    private static Map<Integer, Zone> graph = new HashMap<>();
    int myId;
    int numPlayers;

    Graph(int myId, int numPlayers) {
        this.myId = myId;
        this.numPlayers = numPlayers;
    }

    Zone getInstance(int i) {
        return graph.get(i);
    }

    void addZone(Zone z) {
        graph.put(z.id, z);
    }

    void addLink(int i, int j) {
        Zone zone1 = graph.get(i);
        Zone zone2 = graph.get(j);
        graph.get(i).connectedZones.add(zone2);
        graph.get(j).connectedZones.add(zone1);
    }

    List<Zone> getMyPodZones() {
        List<Zone> result = new ArrayList<>();
        for(Zone zone: graph.values()) {
            if (zone.ownerId == myId) {
                result.add(zone);
            }
        }
        return result;
    }

    List<Zone> getMyZones() {
        List<Zone> result = new ArrayList<>();
        for (Zone z: graph.values()) {
            if (z.ownerId == myId) {
                result.add(z);
            }
        }
        result.sort((a,b)->b.platinumSource-a.platinumSource);
        return result;
    }

    List<Zone> getNeutralZonesWithHighestProduction() {
        List<Zone> result = new ArrayList<>();
        for (Zone z: graph.values()) {
            if (z.ownerId == -1) {
                result.add(z);
            }
        }
        result.sort((a,b)->b.platinumSource-a.platinumSource);
        return result;
    }
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int playerCount = in.nextInt(); // the amount of players (2 to 4)
        int myId = in.nextInt(); // my player ID (0, 1, 2 or 3)
        int zoneCount = in.nextInt();
        int linkCount = in.nextInt();
        Graph board = new Graph(myId, playerCount);
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

        int row = 1;
        while (true) {
            int myPlatinum = in.nextInt();
            for (int i = 0; i < zoneCount; i++) {
                int zId = in.nextInt(); // this zone's ID
                int ownerId = in.nextInt(); // the player who owns this zone (-1 otherwise)
                int podsP0 = in.nextInt(); // player 0's PODs on this zone
                int podsP1 = in.nextInt(); // player 1's PODs on this zone
                int podsP2 = in.nextInt(); // player 2's PODs on this zone (always 0 for a two player game)
                int podsP3 = in.nextInt(); // player 3's PODs on this zone (always 0 for a two or three player game)
                board.getInstance(zId).set(ownerId, podsP0, podsP1, podsP2, podsP3);
            }

//            Moves
            String moveCommand = "WAIT";
            List<String> moves = new ArrayList<>();
            System.err.println("I control " + board.getMyPodZones().size() + " zones");
            List<Zone> neutralAndEnemiesNeighbours = new ArrayList<>();
            List<Zone> neutralNeighbours = new ArrayList<>();
            for (Zone zone: board.getMyPodZones()) {
                int numPods = zone.pods[myId];
                List<Zone> neighbours = zone.connectedZones;
                System.err.println("Zone " + zone.id + " is connected to " + neighbours);
                for (Zone z: neighbours) {
                    if (z.ownerId != myId) {
                        neutralAndEnemiesNeighbours.add(z);
                        if (z.ownerId == -1) {
                            neutralNeighbours.add(z);
                        }
                    }
                }
                for (int i=0; i<numPods; i++) {
                    if (neutralAndEnemiesNeighbours.size() > 0) {
                        neutralAndEnemiesNeighbours.sort((a,b)->b.platinumSource-a.platinumSource);
                        Zone target = neutralAndEnemiesNeighbours.remove(0);
                        moves.add("1 " + zone.id + " " + target.id);
                    }
                }
            }
            System.err.println(moves.size() + " moves requested");
            if (moves.size() > 0) {
                moveCommand = String.join(" ", moves);
            }
            System.out.println(moveCommand);

//            Purchases
            String purchaseCommand = "WAIT";
            List<Zone> neutralZones = board.getNeutralZonesWithHighestProduction();
            List<String> purchases = new ArrayList<>();
            int numSpawn = 1;
            while (myPlatinum >= 20) {
                int quantity = 1;
                myPlatinum-=20;
                Zone creationZone = null;
                if (row == 1) {
                    neutralZones.remove(0);
                    neutralZones.remove(0);
                }
                if (neutralZones.size() > 0) {
                    creationZone = neutralZones.remove(0);
                }
                else if (board.getMyZones().size() > 0) {
                    creationZone = board.getMyZones().get(0);
                }
//                Bonus for best zones
                if (row < 2 && numSpawn < 3 && myPlatinum >= 20) {
                    quantity+=1;
                    myPlatinum-=20;
                }
                if (creationZone != null) {
                    purchases.add(quantity + " " + creationZone.id);
                }
                numSpawn++;
            }
            if (purchases.size() > 0) {
                purchaseCommand = String.join(" ", purchases);
            }
            System.out.println(purchaseCommand);
            row++;
        }
    }
}