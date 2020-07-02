import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class State {
    List<Zone> myZones;
    List<Zone> neutralZones;
    List<Zone> enemiesZones;
}

class Zone {
    int id;
    Coord coord;

    Zone(int id, Coord coord) {
        this.id = id;
        this.coord = coord;
    }

    public String toString() {
        return "Zone " + id + " " + coord;
    }
}

class Drone {
    int team;
    Coord coord;

    Drone(int team, Coord coord) {
        this.team = team;
        this.coord = coord;
    }

    public String toString() {
        return "Team " + team + " " + coord;
    }
}

class Coord {
    int x;
    int y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    double distance(Coord other) {
        return Math.hypot(x - other.x, y - other.y);
    }
}

class Strategist {
    Zone findClosesZone() {
        return null;
    }
}

class Player {

    public static void main(String args[]) {
        List<Zone> zones = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        int p = in.nextInt(); // number of players in the game (2 to 4 players)
        int id = in.nextInt(); // ID of your player (0, 1, 2, or 3)
        System.err.println("My ID: " + id);
        int d = in.nextInt(); // number of drones in each team (3 to 11)
        int numZones = in.nextInt(); // number of zones on the map (4 to 8)
        int x;
        int y;

        // Loop over zones
        for (int i = 0; i < numZones; i++) {
            x = in.nextInt(); // Center of a zone. A zone is a circle with a radius of 100 units.
            y = in.nextInt();
            zones.add(new Zone(i, new Coord(x, y)));
        }

        while (true) {
            List<Zone> zonesInMyControl = new ArrayList<>();
            List<Zone> zonesNeutral = new ArrayList<>();
            List<Zone> zonesNotInMyControl = new ArrayList<>();
            List<Drone> dronesMine = new ArrayList<>();
            List<Drone> dronesEnemies = new ArrayList<>();
            Map<Zone, List<Drone>> dronesAllocation = new HashMap<>();

            for (int i = 0; i < numZones; i++) {
                int tid = in.nextInt(); // ID of the team controlling the zone (0, 1, 2, or 3) or -1 if it is not controlled. The zones are given in the same order as in the initialization.
//                System.err.println("Zone belongs to : " + tid);
                if (tid == id) {
                    zonesInMyControl.add(zones.get(i));
                } else if (tid == 0) {
                    zonesNeutral.add(zones.get(i));
                } else {
                    zonesNotInMyControl.add(zones.get(i));
                }
            }
//            System.err.println("In my control: " + zonesInMyControl);
//            System.err.println("Neutral: " + zonesNotInMyControl);
//            System.err.println("Enemies' zones: " + zonesNotInMyControl);

            // Iterate over the players
            for (int i = 0; i < p; i++) {
                // Iterate over the drones of each player
                for (int j = 0; j < d; j++) {
                    int dx = in.nextInt();
                    int dy = in.nextInt();
                    Drone currentDrone = new Drone(i, new Coord(dx, dy));
                    if (i == id) {
                        dronesMine.add(currentDrone);
                    } else {
                        dronesEnemies.add(currentDrone);
                    }
                    for (Zone z : zones) {
                        if (currentDrone.coord.distance(z.coord) < 100) {
                            if (dronesAllocation.get(z) == null) {
                                dronesAllocation.put(z, new ArrayList<>());
                            }
                            dronesAllocation.get(z).add(currentDrone);
                        }
                    }
                }
            }
            dronesAllocation.forEach((k, v) -> {
                        System.err.println("******\nZone " + k.id);
                        System.err.println("Mine: " + v.stream().filter(drone -> drone.team == id).count());
                        System.err.println("Theirs: " + v.stream().filter(drone -> drone.team != id).count());
                    }
            );
            for (int i = 0; i < d; i++) {
                Drone currentDrone = dronesMine.get(i);
                List<Zone> notInMyControl = Stream.concat(zonesNeutral.stream(), zonesNotInMyControl.stream())
                        .collect(Collectors.toList());
                Zone closestZone = notInMyControl.get(0);
                double minDistance = 99999999;
                for (Zone z : notInMyControl) {
                    double tmpDistance = currentDrone.coord.distance(z.coord);
                    if (tmpDistance < minDistance) {
                        minDistance = tmpDistance;
                        closestZone = z;
                    }
                }
                System.out.println(closestZone.coord.x + " " + closestZone.coord.y);
            }
        }
    }
}