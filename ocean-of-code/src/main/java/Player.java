import java.util.*;
import java.util.stream.Collectors;

class Coord {
    final int x;
    final int y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    String getMove(Coord target) {
        if (x < target.x) {
            return "MOVE E";
        }
        else if (x > target.x) {
            return "MOVE W";
        }
        else if (y < target.y) {
            return "MOVE S";
        }
        else {
            return "MOVE N";
        }
    }
}

enum CellType {
    WATER,
    ISLAND;

    @Override
    public String toString() {
        switch(this) {
            case WATER:
                return " ";
            case ISLAND:
                return "X";
            default:
                throw new IllegalArgumentException();
        }
    }
}

class Board {
    List<List<CellType>> cells;
    boolean[][] visited = new boolean[15][15];

    Board(List<String> lines) {

        cells = new ArrayList<>();
        for (String line: lines) {
            List<CellType> row = new ArrayList<>();
            for (char c: line.toCharArray()) {
                if (c == '.') {
                    row.add(CellType.WATER);
                }
                else {
                    row.add(CellType.ISLAND);
                }
            }
            cells.add(row);
        }
    }

    CellType getCell(int x, int y) {
        return cells.get(y).get(x);
    }

    CellType getCell(Coord pos) {
        return getCell(pos.x, pos.y);
    }

    List<Coord> getAvailableMoves(Coord current) {
        List<Coord> result = new ArrayList<>();
        if (current.x > 0 && getCell(current.x - 1, current.y) != CellType.ISLAND && !visited[current.y][current.x - 1]) {
            result.add(new Coord(current.x - 1, current.y));
        }
        if (current.x < 14 && getCell(current.x + 1, current.y) != CellType.ISLAND && !visited[current.y][current.x + 1]) {
            result.add(new Coord(current.x + 1, current.y));
        }
        if (current.y > 0 && getCell(current.x, current.y - 1) != CellType.ISLAND && !visited[current.y - 1][current.x]) {
            result.add(new Coord(current.x, current.y - 1));
        }
        if (current.y < 14 && getCell(current.x, current.y + 1) != CellType.ISLAND && !visited[current.y + 1][current.x]) {
            result.add(new Coord(current.x, current.y + 1));
        }
        return result;
    }

     public String toString() {
        StringBuilder sb = new StringBuilder();
         for (int j=0; j<15; j++) {
             for (int i=0; i<15; i++) {
                 CellType c = getCell(i, j);
                 if (c == CellType.WATER && visited[j][i]) {
                    sb.append("-");
                 }
                 else if (c == CellType.WATER && !visited[j][i]) {
                     sb.append(" ");
                 }
                 else {
                     sb.append(c);
                 }
            }
            sb.append("\n");
        }
        return sb.toString();
     }

     Coord getInitialPosition() {
        List<Coord> initialPositions = new ArrayList<>();
        initialPositions.add(new Coord(7, 7));
        initialPositions.add(new Coord(7, 8));
        initialPositions.add(new Coord(8, 7));
        initialPositions.add(new Coord(8, 8));
        initialPositions.add(new Coord(7, 9));

        return initialPositions.stream()
                .filter(pos -> getCell(pos) != CellType.ISLAND)
                .findFirst().get();
     }

     void markAsVisited(Coord coord) {
        visited[coord.y][coord.x] = true;
     }

     void resetVisited() {
         for (boolean[] row: visited) {
             Arrays.fill(row, false);
         }
     }
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        int myId = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }

        List<String> map = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            String line = in.nextLine();
            map.add(line);
        }
        Board board = new Board(map);

        List<String> opponentsMoveHistory = new ArrayList<>();
        int opponentXVariation = 0;
        int opponentYVariation = 0;

//        Starting position
        Coord initialPosition = board.getInitialPosition();
        System.out.println(initialPosition.x + " " + initialPosition.y);

        // game loop
        while (true) {
//            Loop input
            int x = in.nextInt();
            int y = in.nextInt();
            int myLife = in.nextInt();
            int oppLife = in.nextInt();
            int torpedoCooldown = in.nextInt();
            int sonarCooldown = in.nextInt();
            int silenceCooldown = in.nextInt();
            int mineCooldown = in.nextInt();
            String sonarResult = in.next();
            if (in.hasNextLine()) {
                in.nextLine();
            }
            String opponentOrdersText = in.nextLine();
            if (!opponentOrdersText.equals("NA")) {
                System.err.println(opponentOrdersText);
                String[] opponentsOrders = opponentOrdersText.split("\\|");
                for (String order: opponentsOrders) {
                    String[] splitOrder = order.split(" ");
                    if (splitOrder[0].equals("MOVE")) {
                        String opponentDirection = splitOrder[1];
                        System.err.println(opponentDirection);
                        switch(opponentDirection) {
                            case "N":
                                opponentYVariation -= 1; break;
                            case "S":
                                opponentYVariation += 1; break;
                            case "E":
                                opponentXVariation += 1; break;
                            case "W":
                                opponentXVariation -= 1; break;
                        }
//                        Compute in which area the opponent can be
                        opponentsMoveHistory.add(opponentDirection);
                    }
                    if (splitOrder[0].equals("TORPEDO")) {
                        System.err.println("/!\\ BOOOOOOOMMMMMMMM");
                        System.err.println("Target: " + splitOrder[0] + ", " + splitOrder[1]);
                    }
                }
                System.err.println("Opponent X variation: " + opponentXVariation);
                System.err.println("Opponent Y variation: " + opponentYVariation);
            }

//            Our logic
            Coord current = new Coord(x, y);

            System.err.println("torpedoCooldown: " + torpedoCooldown);
            board.markAsVisited(current);
//            System.err.println("===============");
//            System.err.println(foo);
//            System.err.println("===============");
            List<Coord> availableMoves = board.getAvailableMoves(new Coord(x, y));
            System.err.println(availableMoves.size() + " available moves");

            if (availableMoves.size() > 0) {
                System.out.println(current.getMove(availableMoves.get(0)) + " TORPEDO");
            }
            else {
                System.out.println("SURFACE" + " TORPEDO");
                board.resetVisited();
            }
//            if (torpedoCooldown > 0) {
        // }
//            else {
//                System.out.println("TORPEDO 0 7");
//            }
        }
    }
}