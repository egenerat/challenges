import java.util.*;

class Coord {
    int x;
    int y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getFlyingDistance(Coord other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public String toString() {
        return x + ":" + y;
    }
}

class Pellet {
    Coord pos;
    int value;

    Pellet(Coord c, int value) {
        this.pos = c;
        this.value = value;
    }

    public String toString() {
        return pos.toString() + "; " + value;
    }
}

class Pacman {
    Coord pos;
    int id;
    int abilityCooldown;

    Pacman(Coord pos, int id, int abilityCooldown) {
        this.pos = pos;
        this.id = id;
        this.abilityCooldown = abilityCooldown;
    }
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // size of the grid
        int height = in.nextInt(); // top left corner is (x=0, y=0)
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String row = in.nextLine(); // one line of the grid: space " " is floor, pound "#" is wall
//            System.err.println(row);
        }

        List<Pacman> myPacmans;
        List<String> response;
        // game loop
        while (true) {
            myPacmans = new ArrayList<>();
            response = new ArrayList<>();
            int myScore = in.nextInt();
            int opponentScore = in.nextInt();
            int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight
            for (int i = 0; i < visiblePacCount; i++) {
                int pacId = in.nextInt(); // pac number (unique within a team)
                boolean mine = in.nextInt() != 0; // true if this pac is yours
                int x = in.nextInt();
                int y = in.nextInt();
                String typeId = in.next();
                int speedTurnsLeft = in.nextInt();
                int abilityCooldown = in.nextInt();

                if (mine) {
                    myPacmans.add(new Pacman(new Coord(x, y), pacId, abilityCooldown));
                }
            }
            int visiblePelletCount = in.nextInt(); // all pellets in sight
            List<Pellet> pellets10 = new ArrayList<>();
            List<Pellet> pellets1 = new ArrayList<>();
            for (int i = 0; i < visiblePelletCount; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                int value = in.nextInt();
                if (value == 10) {
                    pellets10.add(new Pellet(new Coord(x, y), value));
                }
                else {
                    pellets1.add(new Pellet(new Coord(x, y), value));
                }
            }
            for (Pacman pacman: myPacmans) {
                if (pacman.abilityCooldown == 0) {
                    response.add("SPEED " + pacman.id);
                }
                else {
                Pellet targetPellet = null;
                Integer minDistance = null;
//                    System.err.println("My current position: " + pacman.pos.x + "," + pacman.pos.y);
//                    System.err.println("Pellet 10: " + pellets10.size() + ", Pellets 1: " + pellets1.size());
                for (Pellet p : pellets10) {
                    int tmpDist = p.pos.getFlyingDistance(pacman.pos);
//                        System.err.println(p.pos + " distance: " + tmpDist);
                    if (minDistance == null || tmpDist < minDistance) {
                        minDistance = tmpDist;
                        targetPellet = p;
                    }
                }
                if (targetPellet == null) {
                    for (Pellet p : pellets1) {
                        int tmpDist = p.pos.getFlyingDistance(pacman.pos);
//                            System.err.println(p.pos + " distance: " + tmpDist);
                        if (minDistance == null || tmpDist < minDistance) {
                            minDistance = tmpDist;
                            targetPellet = p;
                        }
                    }
                }
//                    System.err.println("Closest is : " + targetPellet + " with distance of: " + minDistance);
                if (targetPellet != null) {
                        response.add("MOVE " + pacman.id + " " + targetPellet.pos.x + " " + targetPellet.pos.y + " " + targetPellet);
                    } else {
                    response.add("MOVE " + pacman.id + " 0 0");
                }
                pellets10.remove(targetPellet);
                pellets1.remove(targetPellet);
            }
            }
            System.out.println(String.join(" | ", response));
        }
    }
}