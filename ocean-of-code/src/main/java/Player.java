import java.util.*;

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
        } else if (x > target.x) {
            return "MOVE W";
        } else if (y < target.y) {
            return "MOVE S";
        } else {
            return "MOVE N";
        }
    }
}

enum CellType {
    WATER,
    ISLAND;

    @Override
    public String toString() {
        switch (this) {
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
        for (String line : lines) {
            List<CellType> row = new ArrayList<>();
            for (char c : line.toCharArray()) {
                if (c == '.') {
                    row.add(CellType.WATER);
                } else {
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
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; i++) {
                CellType c = getCell(i, j);
                if (c == CellType.WATER && visited[j][i]) {
                    sb.append("-");
                } else if (c == CellType.WATER && !visited[j][i]) {
                    sb.append(" ");
                } else {
                    sb.append(c);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    void debug() {
        System.err.println("===============");
        System.err.println(toString());
        System.err.println("===============");
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
        for (boolean[] row : visited) {
            Arrays.fill(row, false);
        }
    }

    boolean[][] drawDiamond(Coord target, int distance) {
//       TODO This is a simple approximation, but the torpedo cannot fly over an island, it needs to go around
//         TODO ArrayOutOfBoundException unchecked
        boolean[][] diamond = new boolean[15][15];
        for (int diffY = -distance; diffY <= distance; diffY++) {
            int absDiffY = Math.abs(diffY);
            for (int diffX = -distance + absDiffY; diffX <= distance - absDiffY; diffX++) {
                diamond[target.y + diffY][target.x + diffX] = true;
            }
        }
        return diamond;
    }

    void findTorpadoLaunchArea(Coord target) {
        boolean[][] possible = drawDiamond(target, 4);
        int countPossible = 0;
        System.err.println("DIAMOND============");
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; i++) {
                if (i == target.x && j == target.y) {
                    System.err.print("O");
                    countPossible++;
                } else if (possible[j][i] && getCell(i, j) != CellType.ISLAND) {
                    System.err.print("X");
                    countPossible++;
                } else {
                    System.err.print(" ");
                }
            }
            System.err.println();
        }
        System.err.println(countPossible + " possible positions");
        System.err.println("DIAMOND END============");
    }
}

class Strategist {
//    Improve location with:
//    - the launches of torpedo
//    - the surface command

    Board board;
    List<String> opponentsMoveHistory = new ArrayList<>();
    int opponentXVariation = 0;
    int opponentYVariation = 0;
    int torpedoCooldown;
    int sonarCooldown;
    int silenceCooldown;

    Strategist(Board board) {
        this.board = board;
    }

    String getLoadAction() {
        String loadAction;
        if (torpedoCooldown > 0) {
            loadAction = " TORPEDO";
        } else if (sonarCooldown > 0) {
            loadAction = " SONAR";
        } else if (silenceCooldown > 0) {
            loadAction = " SILENCE";
        } else {
            loadAction = "";
        }
        return loadAction;
    }

    String getMove(Coord current, String opponentMove) {
        List<Coord> availableMoves = board.getAvailableMoves(current);
//        System.err.println(availableMoves.size() + " available moves");

        String opponentDirection = parse(opponentMove);
        opponentsMoveHistory.add(opponentDirection);
        computeVariations(opponentDirection);
        computeOpponentPosition();

        String response;
        if (availableMoves.size() > 0) {
            response = current.getMove(availableMoves.get(0)) + getLoadAction();
        } else {
            response = "SURFACE";
            board.resetVisited();
        }
//        System.err.println("torpedoCooldown: " + torpedoCooldown);
//        if (torpedoCooldown > 0) {
        // }
//        else {
//            System.out.println("TORPEDO 0 7");
//        }
        return response;
    }

    void computeVariations(String opponentDirection) {
//        Test the surface case
        switch (opponentDirection) {
            case "N":
                opponentYVariation -= 1;
                break;
            case "S":
                opponentYVariation += 1;
                break;
            case "E":
                opponentXVariation += 1;
                break;
            case "W":
                opponentXVariation -= 1;
                break;
        }
    }

    void computeOpponentPosition() {
//        Returns the list of zones where the opponent can be
//         Ways to find where is the opponent:
//         - The move directions
//         - If it is touched by a torpedo
//         - Where he is launching torpedo
//         int oppLife = in.nextInt();
        int minX = Math.max(0, opponentXVariation);
        int minY = Math.max(0, opponentYVariation);
        int maxX = Math.min(14, 14 + opponentXVariation);
        int maxY = Math.min(14, 14 + opponentYVariation);
        System.err.println("Opponent x between " + minX + " and " + maxX);
        System.err.println("Opponent y between " + minY + " and " + maxY);
        int sizeOfArea = (maxX - minX) * (maxY - minY);
        System.err.println("Size of the area : " + sizeOfArea);
    }

    String parse(String opponentOrdersText) {
        String opponentDirection = "";

//       If we start, the first opponent's order is "NA"
        if (!opponentOrdersText.equals("NA")) {
            System.err.println(opponentOrdersText);
            String[] opponentsOrders = opponentOrdersText.split("\\|");
            for (String order : opponentsOrders) {
                String[] splitOrder = order.split(" ");
                if (splitOrder[0].equals("MOVE")) {
                    opponentDirection = splitOrder[1];
                }
                if (splitOrder[0].equals("TORPEDO")) {
                    System.err.println("/!\\ BOOOOOOOMMMMMMMM");
                    System.err.println("Target: " + splitOrder[1] + ", " + splitOrder[2]);
                }
                if (splitOrder[0].equals("SURFACE")) {
//                    TODO
                    System.err.println("Opponent has surfaced in : " + splitOrder[1]);
                }
            }
        }
        return opponentDirection;
    }

    int computeSizePossiblePositions() {
        return -1;
    }

    void updateCooldowns(int torpedoCooldown, int sonarCooldown, int silenceCooldown) {
        this.torpedoCooldown = torpedoCooldown;
        this.sonarCooldown = sonarCooldown;
        this.silenceCooldown = silenceCooldown;
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
        Strategist strategist = new Strategist(board);

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

//            Our logic
            Coord current = new Coord(x, y);
            board.markAsVisited(current);
            board.findTorpadoLaunchArea(current);
//            board.debug();
            strategist.updateCooldowns(torpedoCooldown, sonarCooldown, silenceCooldown);
            System.out.println(strategist.getMove(current, opponentOrdersText));
        }
    }
}