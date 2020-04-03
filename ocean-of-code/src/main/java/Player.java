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

    Map drawDiamond(Coord target, int distance) {
//       TODO This is a simple approximation, but the torpedo cannot fly over an island, it needs to go around
        boolean[][] diamond = new boolean[15][15];
        int counter = 0;
        for (int diffY = -distance; diffY <= distance; diffY++) {
            int absDiffY = Math.abs(diffY);
            for (int diffX = -distance + absDiffY; diffX <= distance - absDiffY; diffX++) {
                int x = target.x + diffX;
                int y = target.y + diffY;
                if (x >= 0 && x <= 14 && y >= 0 && y <= 14) {
                    diamond[target.y + diffY][target.x + diffX] = true;
                    counter++;
                }
            }
        }
        return new Map(diamond, counter);
    }

    Map findTorpedoLaunchArea(Coord target) {
        Map possible = drawDiamond(target, 4);
        System.err.println(possible.toString());
        return possible;
    }

    Map convertSectorToArea(int sectorNum) {
        boolean[][] area = new boolean[15][15];
        int possibleCellCount = 0;
//        As sectorNum is between 1 and 9, to have it between 0 and 8
        int horizontalZone = (sectorNum - 1) % 3;
        int beginX = 5 * horizontalZone;
        int endX = 4 + 5 * horizontalZone;
//        System.err.println("X range: " + beginX + " -> " + endX);

        int verticalZone = (sectorNum - 1) / 3;
        int beginY = 5 * verticalZone;
        int endY = 4 + 5 * verticalZone;
//        System.err.println("Y range: " + beginY + " -> " + endY);
        for (int j = beginY; j <= endY; j++) {
            for (int i = beginX; i <= endX; i++) {
                if (getCell(i, j) != CellType.ISLAND) {
                    area[j][i] = true;
                    possibleCellCount++;
                }
            }
        }
        return new Map(area, possibleCellCount);
    }
}

class Map {
    boolean[][] area;
    int size;

    public Map(boolean[][] area, int size) {
        this.area = area;
        this.size = size;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append(" possible positions");
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; i++) {
                if (area[j][i]) {
                    sb.append("x");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    Map substract(Map other) {
        boolean[][] result = new boolean[15][15];
        int count = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; i++) {
                if (area[j][i] && other.area[j][i]) {
                    result[j][i] = true;
                    count++;
                }
            }
        }
        return new Map(result, count);
    }
}

class OpponentState {
    String command;
    String direction;
    Map possiblePositions;
    int oppLife;

    OpponentState(String command, String direction, int oppLife) {
        this.command = command;
        this.direction = direction;
        this.oppLife = oppLife;
    }

    void setComputed(Map possiblePositions) {
        this.possiblePositions = possiblePositions;
    }
}

class Strategist {
//    Improve location with:
//    - the launches of torpedo
//    - the surface command

    Board board;
    List<OpponentState> opponentsMoveHistory = new ArrayList<>();
    int opponentXVariation = 0;
    int opponentYVariation = 0;
    int torpedoCooldown;
    int sonarCooldown;
    int silenceCooldown;
    int oppLife;
    Coord current;
    String opponentMove;

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

    void setRoundData(int torpedoCooldown, int sonarCooldown, int silenceCooldown, int oppLife,
                      Coord current, String opponentMove) {
        this.torpedoCooldown = torpedoCooldown;
        this.sonarCooldown = sonarCooldown;
        this.silenceCooldown = silenceCooldown;
        this.oppLife = oppLife;
        this.current = current;
        this.opponentMove = opponentMove;
    }

    Map getOpponentLocation(Foo opponent) {
        OpponentState latestOS = new OpponentState(opponentMove, opponent.direction, oppLife);
        opponentsMoveHistory.add(latestOS);
        computeVariations(opponent.direction);
        Map map = computeOpponentPositionFromHistory();
        latestOS.setComputed(map);
        for (OpponentState s : opponentsMoveHistory) {
            System.err.println(s.command + " " + s.possiblePositions.size);
        }
        return null;
    }

    String getMove() {
        List<Coord> availableMoves = board.getAvailableMoves(current);
//        System.err.println(availableMoves.size() + " available moves");

//        This one should only be called if MOVE
        Foo foo = parse(opponentMove);
        Map bar = getOpponentLocation(foo);

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

    Map computeOpponentPositionFromHistory() {
//        Returns the list of zones where the opponent can be
//         Ways to find where is the opponent:
//         - The move directions
//         - If it is touched by a torpedo
//         - Where he is launching torpedo
//         int oppLife = in.nextInt();

        //        TODO This needs to be improved to take previous positions into consideration.
        //         The new zone can never be larger than the previous one, except if the opponent used silence
        boolean[][] area = new boolean[15][15];
        int minX = Math.max(0, opponentXVariation);
        int minY = Math.max(0, opponentYVariation);
        int maxX = Math.min(14, 14 + opponentXVariation);
        int maxY = Math.min(14, 14 + opponentYVariation);
        System.err.println("Opponent x between " + minX + " and " + maxX);
        System.err.println("Opponent y between " + minY + " and " + maxY);
        int sizeOfArea = ((maxX - minX) + 1) * ((maxY - minY) + 1);
        System.err.println("Size of the area : " + sizeOfArea);
        return new Map(area, sizeOfArea);
    }

    Foo parse(String opponentOrdersText) {
        String opponentDirection = "";
        Map map = null;

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
                    int x = Integer.parseInt(splitOrder[1]);
                    int y = Integer.parseInt(splitOrder[2]);
                    System.err.println("Target: " + x + ", " + y);
                    map = board.findTorpedoLaunchArea(new Coord(x, y));
                }
                if (splitOrder[0].equals("SURFACE")) {
                    System.err.println("Opponent has surfaced in : " + splitOrder[1]);
                    map = board.convertSectorToArea(Integer.parseInt(splitOrder[1]));
//                    System.err.println(map);
                }
                if (splitOrder[0].equals("SILENCE")) {
                    System.err.println("Silence: this is going to be complicated");
                }
            }
        }
        return new Foo(opponentDirection, map);
    }
}

class Foo {
    String direction;
    Map map;

    public Foo(String direction, Map map) {
        this.direction = direction;
        this.map = map;
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
//            board.debug();
            strategist.setRoundData(torpedoCooldown, sonarCooldown, silenceCooldown, oppLife, current,
                    opponentOrdersText);
            System.out.println(strategist.getMove());
        }
    }
}