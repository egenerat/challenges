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

    public String toString() {
        return "Coord " + x + "," + y;
    }

//    TODO does not take islands into consideration
    int distance(Coord other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}

class Direction {
    int xVar;
    int yVar;

    Direction(String directionLetter) {
        xVar = 0;
        yVar = 0;
        switch (directionLetter) {
            case "N":
                yVar--;
                break;
            case "E":
                xVar++;
                break;
            case "S":
                yVar++;
                break;
            case "W":
                xVar--;
                break;
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
//        System.err.println(possible.toString());
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

    Map() {
        this.area = new boolean[15][15];
        for (boolean[] row: area) {
            Arrays.fill(row, true);
        }
        this.size = 225;
    }

    Map(boolean[][] area, int size) {
        this.area = area;
        this.size = size;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append(" possible positions\n");
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

    Map shift(Direction d) {
//        System.err.println("xShift: " + d.xVar);
//        System.err.println("yShift: " + d.yVar);
        boolean[][] result = new boolean[15][15];
        int count = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; i++) {
//                At this point we should ensure the cell is not an island, but at the moment
//                we don't have a reference to the board
//                if (board.getCell(i, j) != CellType.ISLAND) {
//                Make sure we don't go off the limit
                int xCopy = i - d.xVar;
                int yCopy = j - d.yVar;
                if (yCopy >= 0 && yCopy < 15 && xCopy >= 0 && xCopy < 15) {
                    boolean value = area[yCopy][xCopy];
                    result[j][i] = value;
                    if (value) {
                        count++;
                    }
                }
            }
        }
        return new Map(result, count);
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

    List<Coord> getPossiblePositions() {
        List<Coord> possible = new ArrayList<>();
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; i++) {
                if (area[j][i]) {
                    possible.add(new Coord(i, j));
                }
            }
        }
        return  possible;
    }
}

class OpponentState {
    String command;
    Direction direction;
    Map map;
    int oppLife;

    OpponentState(String command, Direction direction, int oppLife) {
        this.command = command;
        this.direction = direction;
        this.oppLife = oppLife;
    }

    void setComputed(Map map) {
        this.map = map;
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
    String opponentRawOrder;

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
        this.opponentRawOrder = opponentMove;
    }

    Map getWaterMap() {
        boolean[][] map = new boolean[15][15];
        int count = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; i++) {
                boolean value = board.getCell(i, j) != CellType.ISLAND;
                map[j][i] = value;
                if (value) {
                    count++;
                }
            }
        }
        return new Map(map, count);
    }

    Map getOpponentLocation(Foo opponent) {
        if (opponent.direction != null) {
            computeVariations(opponent.direction);
        }
        Map map = getWaterMap();
        map.substract(computeOpponentPositionFromHistory());
//        System.err.println("with history: " + map.size);
//        System.err.println(map);
        if (opponent.direction != null && opponentsMoveHistory.size() >= 2) {
            Map previousMap = opponentsMoveHistory.get(opponentsMoveHistory.size() - 2).map;
//            System.err.println("Previous map");
//            System.err.println(previousMap);
            map = map.substract(computeOpponentPositionFromPrevious(opponentsMoveHistory.get(opponentsMoveHistory.size() - 2),
                    opponent.direction));
//            System.err.println("after previous pos: " + map.size);
//            System.err.println(map);
        }

//        opponentsMoveHistory.get(opponentsMoveHistory.size() - 1), opponent.direction
        if (opponent.map != null) {
            map = map.substract(opponent.map);
//            System.err.println("after parse: " + map.size);
        }
//        System.err.println("Opp can be in " + map.size + " positions");
//        System.err.println(map);
        opponentsMoveHistory.get(opponentsMoveHistory.size() - 1).setComputed(map);
        for (OpponentState s : opponentsMoveHistory) {
//            System.err.println(s.command + " " + s.possiblePositions.size);
        }
        return map;
    }

    String getMove() {
        List<Coord> availableMoves = board.getAvailableMoves(current);
        String action = "";
//        System.err.println(availableMoves.size() + " available moves");

//        This one should only be called if MOVE
        Foo foo = parse(opponentRawOrder);
        OpponentState latestOS = new OpponentState(opponentRawOrder, foo.direction, oppLife);
        opponentsMoveHistory.add(latestOS);
        Map opponent = getOpponentLocation(foo);

        if (opponent.size == 1) {
            System.err.println("BOOOOOM");
            Coord opponentPosition = opponent.getPossiblePositions().get(0);
            System.err.println("Target acquired: " + opponentPosition );

            if (current.distance(opponentPosition) < 4 && current.distance(opponentPosition) > 0 && torpedoCooldown == 0) {
                action = " | TORPEDO " + opponentPosition.x + " " + opponentPosition.y;
            }
        }

        String response;
        if (availableMoves.size() > 0) {
            response = current.getMove(availableMoves.get(0)) + getLoadAction() + action;
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

    void computeVariations(Direction opponentDirection) {
        opponentXVariation += opponentDirection.xVar;
        opponentYVariation += opponentDirection.yVar;
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
        int count = 0;
//        System.err.println("XVar: " + opponentXVariation);
//        System.err.println("YVar: " + opponentYVariation);
        int minX = Math.max(0, opponentXVariation);
        int minY = Math.max(0, opponentYVariation);
        int maxX = Math.min(14, 14 + opponentXVariation);
        int maxY = Math.min(14, 14 + opponentYVariation);
        for (int j = minY; j <= maxY; j++) {
            for (int i = minX; i <= maxX; i++) {
                area[j][i] = true;
                count++;
            }
        }
//        System.err.println("Opponent x between " + minX + " and " + maxX);
//        System.err.println("Opponent y between " + minY + " and " + maxY);
//        System.err.println("Size of the area : " + count);
        return new Map(area, count);
    }

    Map computeOpponentPositionFromPrevious(OpponentState previous, Direction direction) {
//        System.err.println(previous.possiblePositions);
        return previous.map.shift(direction);
    }

    Foo parse(String opponentOrdersText) {
        Direction opponentDirection = null;
        Map map = new Map();

//       If we start, the first opponent's order is "NA"
        if (!opponentOrdersText.equals("NA")) {
//            System.err.println(opponentOrdersText);
            String[] opponentsOrders = opponentOrdersText.split("\\|");
            for (String order : opponentsOrders) {
                String[] splitOrder = order.split(" ");
                if (splitOrder[0].equals("MOVE")) {
                    opponentDirection = new Direction(splitOrder[1]);
                }
                else if (splitOrder[0].equals("TORPEDO")) {
//                    System.err.println("/!\\ BOOOOOOOMMMMMMMM");
                    int x = Integer.parseInt(splitOrder[1]);
                    int y = Integer.parseInt(splitOrder[2]);
//                    System.err.println("Target: " + x + ", " + y);
                    map = board.findTorpedoLaunchArea(new Coord(x, y));
                }
                else if (splitOrder[0].equals("SURFACE")) {
//                    System.err.println("Opponent has surfaced in : " + splitOrder[1]);
                    map = board.convertSectorToArea(Integer.parseInt(splitOrder[1]));
//                    System.err.println(map);
                }
                else if (splitOrder[0].equals("SILENCE")) {
                    System.err.println("Silence: this is going to be complicated");
//                    TODO: It is still not random, from the previous position we can still find something
                }
            }
        }
//        System.err.println("End of parse:");
//        System.err.println(map);
        return new Foo(opponentDirection, map);
    }
}

class Foo {
    Direction direction;
    Map map;

    public Foo(Direction direction, Map map) {
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