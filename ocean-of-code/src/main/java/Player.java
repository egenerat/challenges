import java.util.*;

enum CellType {
    WATER,
    ISLAND
}

class Board {
    List<List<CellType>> cells;
    Board(List<String> lines) {
//        for (String foo: lines) {
//            System.err.println(foo);
//        }

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
        System.err.println(foo.toString());

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");

        System.out.println("14 14");

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

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("MSG hello");
            // System.out.println("MOVE N TORPEDO");
        }
    }
}