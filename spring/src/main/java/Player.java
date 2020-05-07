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
        return x + ", " + y;
    }
}

class Pellet {
    Coord pos;
    int value;

    Pellet(Coord c, int value) {
        this.pos = c;
        this.value = value;
    }
}

class Pacman {
    Coord pos;
    int id;

    Pacman(Coord pos, int id) {
        this.pos = pos;
        this.id = id;
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
            System.err.println(row);
        }

        List<Pacman> myPacmans;
        // game loop
        while (true) {
            myPacmans = new ArrayList<>();
            int myScore = in.nextInt();
            int opponentScore = in.nextInt();
            int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight
            for (int i = 0; i < visiblePacCount; i++) {
                int pacId = in.nextInt(); // pac number (unique within a team)
                boolean mine = in.nextInt() != 0; // true if this pac is yours
                int x = in.nextInt();
                int y = in.nextInt();
                String typeId = in.next(); // unused in wood leagues
                int speedTurnsLeft = in.nextInt(); // unused in wood leagues
                int abilityCooldown = in.nextInt(); // unused in wood leagues

                if (mine) {
                    myPacmans.add(new Pacman(new Coord(x, y), pacId));
                }
            }
            int visiblePelletCount = in.nextInt(); // all pellets in sight
            Coord target = null;
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
            Integer minDistance = null;
            Pacman pacman = myPacmans.get(0);
            System.err.println("My current position: " + pacman.pos.x + "," + pacman.pos.y);
            System.err.println("Pellet 10: " + pellets10.size() + ", Pellets 1: " + pellets1.size());
            for (Pellet p : pellets10) {
                int tmpDist = p.pos.getFlyingDistance(pacman.pos);
                System.err.println(p.pos + " distance: " + tmpDist);
                if (minDistance == null || tmpDist < minDistance) {
                    minDistance = tmpDist;
                    target = p.pos;
                }
            }
            if (target == null) {
                for (Pellet p : pellets1) {
                    int tmpDist = p.pos.getFlyingDistance(pacman.pos);
                    System.err.println(p.pos + " distance: " + tmpDist);
                    if (minDistance == null || tmpDist < minDistance) {
                        minDistance = tmpDist;
                        target = p.pos;
                    }
                }
            }
            System.err.println("Closest is : " + target + " with distance of: " + minDistance);
            if (target != null) {
                System.out.println("MOVE 0 " + target.x + " " + target.y);
            }
            else {
                System.out.println("MOVE 0 0 0");
            }
            
        }
    }
}