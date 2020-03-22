import java.util.*;

class Coord {
    final int x;
    final int y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    String getMove(int x, int y) {
        return "";
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

    List<Coord> getAvailableMoves(Coord current) {
        List<Coord> result = new ArrayList<>();
        if (current.x > 0 && getCell(current.x - 1, current.y) != CellType.ISLAND) {
            result.add(new Coord(current.x - 1, current.y));
        }
        if (current.x < 14 && getCell(current.x + 1, current.y) != CellType.ISLAND) {
            result.add(new Coord(current.x + 1, current.y));
        }
        if (current.y > 0 && getCell(current.x, current.y - 1) != CellType.ISLAND) {
            result.add(new Coord(current.x, current.y - 1));
        }
        if (current.y < 14 && getCell(current.x, current.y + 1) != CellType.ISLAND) {
            result.add(new Coord(current.x, current.y + 1));
        }
        return result;
    }

     public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<CellType> row : cells) {
            for (CellType cell: row) {
                sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
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
        Board foo = new Board(map);

//        Starting position
//        We should make sure it is not a island cell
        System.out.println("7 7");

        // game loop
        while (true) {
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
            String opponentOrders = in.nextLine();

            List<Coord> availableMoves = foo.getAvailableMoves(new Coord(x, y));
            System.err.println(availableMoves.size() + " available moves");

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("MSG hello");
            // System.out.println("MOVE N TORPEDO");
        }
    }
}