import java.util.*;

enum ActionType {
    MOVE,
    SURFACE,
    SILENCE,
    SONAR,
    TORPEDO,
    MINE,
    TRIGGER
}

abstract class Action {
    ActionType type;

    Action(ActionType type) {
        this.type = type;
    }
}

abstract class CoordAction extends Action {
    Coord target;

    CoordAction(ActionType type, Coord target) {
        super(type);
        this.target = target;
    }
}

abstract class ZoneAction extends Action {
    int zone;

    ZoneAction(ActionType type, int zone) {
        super(type);
        this.zone = zone;
    }
}

// MOVE N
class MoveAction extends Action {
    Direction direction;

    MoveAction(Direction direction) {
        super(ActionType.MOVE);
        this.direction = direction;
    }
}

// SURFACE 3
class SurfaceAction extends ZoneAction {
    SurfaceAction(int zone) {
        super(ActionType.SURFACE, zone);
    }
}

// SONAR 4
class SonarAction extends ZoneAction {
    public SonarAction(int zone) {
        super(ActionType.SONAR, zone);
    }
}

// SILENCE
class SilenceAction extends Action {
    public SilenceAction() {
        super(ActionType.SILENCE);
    }
}

// TORPEDO 3 5
class TorpedoAction extends CoordAction {
    public TorpedoAction(Coord target) {
        super(ActionType.TORPEDO, target);
    }
}

// MINE
class MineAction extends Action {
    public MineAction() {
        super(ActionType.MINE);
    }
}

// TRIGGER 3 5
class TriggerAction extends CoordAction {
    public TriggerAction(Coord target) {
        super(ActionType.TRIGGER, target);
    }
}


class Coord {
    final int x;
    final int y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    String getCardinalPoint(Coord target) {
        if (x < target.x) {
            return "E";
        } else if (x > target.x) {
            return "W";
        } else if (y < target.y) {
            return "S";
        } else {
            return "N";
        }
    }

    public String toString() {
        return "Coord " + x + "," + y;
    }

//    TODO does not take islands into consideration
    int distance(Coord other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    Direction getDirection(Coord other) {
        return new Direction(other.x - x, other.y - y);
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
                yVar = -1;
                break;
            case "E":
                xVar = 1;
                break;
            case "S":
                yVar = 1;
                break;
            case "W":
                xVar = -1;
                break;
        }
    }

    Direction(int xVar, int yVar) {
        this.xVar = xVar;
        this.yVar = yVar;
    }

    public String toString() {
        return "Direction: " + xVar + ", " + yVar;
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
    boolean[][] visited;
    int size;

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
        size = cells.size();
        visited = new boolean[size][size];
    }

    CellType getCell(int x, int y) {
        return cells.get(y).get(x);
    }

    CellType getCell(Coord pos) {
        return getCell(pos.x, pos.y);
    }

    List<Coord> getAvailableMoves(Coord current) {
        List<Coord> result = new ArrayList<>();

//        X decreasing (W)
        boolean isAvailable = true;
        int d = 1;
        while (isAvailable && d <= 4) {
            if (!(current.x >= d && getCell(current.x - d, current.y) != CellType.ISLAND && !visited[current.y][current.x - d])) {
                isAvailable = false;
            }
            else {
                result.add(new Coord(current.x - d, current.y));
                d++;
            }
        }

//        X increasing (E)
        isAvailable = true;
        d = 1;
        while (isAvailable && d <= 4) {
            if (!(current.x < size - d && getCell(current.x + d, current.y) != CellType.ISLAND && !visited[current.y][current.x + d])) {
                isAvailable = false;
            }
            else {
                result.add(new Coord(current.x + d, current.y));
                d++;
            }
        }

//        Y decreasing (N)
        isAvailable = true;
        d = 1;
        while (isAvailable && d <= 4) {
            if (!(current.y >= d && getCell(current.x, current.y - d) != CellType.ISLAND && !visited[current.y - d][current.x])) {
                isAvailable = false;
            }
            else {
                result.add(new Coord(current.x, current.y - d));
                d++;
            }
        }

//        Y increasing (S)
        isAvailable = true;
        d = 1;
        while (isAvailable && d <= 4) {
            if (!(current.y < size - d && getCell(current.x, current.y + d) != CellType.ISLAND && !visited[current.y + d][current.x])) {
                isAvailable = false;
            }
            else {
                result.add(new Coord(current.x, current.y + d));
                d++;
            }
        }

        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                CellType c = getCell(i, j);
                if (c == CellType.WATER && visited[j][i]) {
                    sb.append("*");
                } else if (c == CellType.WATER && !visited[j][i]) {
                    sb.append(".");
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
        initialPositions.add(new Coord(8, 9));
        initialPositions.add(new Coord(9, 7));
        initialPositions.add(new Coord(9, 8));
        initialPositions.add(new Coord(9, 9));

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

    Possibilities drawDiamond(Coord target, int distance) {
//       TODO This is a simple approximation, but the torpedo cannot fly over an island, it needs to go around
        boolean[][] diamond = new boolean[size][size];
        int counter = 0;
        for (int diffY = -distance; diffY <= distance; diffY++) {
            int absDiffY = Math.abs(diffY);
            for (int diffX = -distance + absDiffY; diffX <= distance - absDiffY; diffX++) {
                int x = target.x + diffX;
                int y = target.y + diffY;
                if (x >= 0 && x < size && y >= 0 && y < size) {
                    diamond[target.y + diffY][target.x + diffX] = true;
                    counter++;
                }
            }
        }
        return new Possibilities(diamond, counter);
    }

    Possibilities findTorpedoLaunchArea(Coord target) {
        Possibilities possible = drawDiamond(target, 4);
//        System.err.println(possible.toString());
        return possible;
    }

    Possibilities convertSectorToArea(int sectorNum) {
        boolean[][] area = new boolean[size][size];
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
                area[j][i] = true;
                possibleCellCount++;
            }
        }
        return new Possibilities(area, possibleCellCount);
    }
}

class Possibilities {
    boolean[][] area;
    int count;

//    For the tests, we want to create smaller maps
    private int size;

    Possibilities() {
        this.size = 15;
        this.area = new boolean[size][size];
        for (boolean[] row: area) {
            Arrays.fill(row, true);
        }
        this.count = 225;
    }

    Possibilities(boolean[][] area, int count) {
        this.area = area;
        this.count = count;
        this.size = area.length;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(count).append(" possible positions\n");
        System.err.println("|0123456789ABCDE|");
        for (int j = 0; j < size; j++) {
            sb.append("|");
            for (int i = 0; i < size; i++) {
                if (area[j][i]) {
                    sb.append("x");
                } else {
                    sb.append(".");
                }
            }
            sb.append("|" + j + "\n");
        }
        return sb.toString();
    }

    Possibilities shift(Direction d) {
//        System.err.println("xShift: " + d.xVar);
//        System.err.println("yShift: " + d.yVar);
        boolean[][] result = new boolean[size][size];
        int count = 0;
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
//                At this point we should ensure the cell is not an island, but at the moment
//                we don't have a reference to the board
//                if (board.getCell(i, j) != CellType.ISLAND) {
//                Make sure we don't go off the limit
                int xCopy = i - d.xVar;
                int yCopy = j - d.yVar;
                if (yCopy >= 0 && yCopy < size && xCopy >= 0 && xCopy < size) {
                    boolean value = area[yCopy][xCopy];
                    result[j][i] = value;
                    if (value) {
                        count++;
                    }
                }
            }
        }
        return new Possibilities(result, count);
    }

// This method will be useful to extend the possible zone in case of silence action
    Possibilities spread(int distance) {
        boolean[][] result = new boolean[size][size];
        int count = 0;
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                boolean value = area[j][i];
//                TODO: only works if distance = 1
                if (j > 0) {
                    value |= area[j-1][i];
                }
                if (j < size - 1) {
                    value |= area[j+1][i];
                }
                if (i > 0) {
                    value |= area[j][i-1];
                }
                if (i < size - 1) {
                    value |= area[j][i+1];
                }
                if (value) {
                            result[j][i] = true;
                            count++;
                        }
                    }
                }
        return new Possibilities(result, count);
    }

    Possibilities subtract(Possibilities other) {
        boolean[][] result = new boolean[size][size];
        int count = 0;
        if (other != null) {
            for (int j = 0; j < size; j++) {
                for (int i = 0; i < size; i++) {
                    if (area[j][i] && other.area[j][i]) {
                        result[j][i] = true;
                        count++;
                    }
                }
            }
            return new Possibilities(result, count);
        }
        return this;
    }

    List<Coord> getPossiblePositions() {
        List<Coord> possible = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                if (area[j][i]) {
                    possible.add(new Coord(i, j));
                }
            }
        }
        return  possible;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        else if (!(other instanceof Possibilities)) {
            return false;
        }
        Possibilities c = (Possibilities) other;
        return Arrays.deepEquals(area, c.area) && count == c.count && size == c.size;
    }
}

class OpponentState {
    String command;
    Direction direction;
    Possibilities possibilities;
    int oppLife;

    OpponentState(String command, Direction direction, int oppLife) {
        this.command = command;
        this.direction = direction;
        this.oppLife = oppLife;
    }

    void setComputed(Possibilities possibilities) {
        this.possibilities = possibilities;
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
//        } else if (sonarCooldown > 0) {
//            loadAction = " SONAR";
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

    Possibilities getWaterMap() {
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
        return new Possibilities(map, count);
    }

    Possibilities getOpponentLocation() {
        if (opponent.direction != null) {
            computeVariations(opponent.direction);
        }
        Possibilities possibilities = getWaterMap();
//        System.err.println("Map from getWaterMap()");
//        System.err.println(map);
//        System.err.println("Map from computeOpponentPositionFromHistory()");
        possibilities.subtract(computeOpponentPositionFromHistory());
//        System.err.println("with history: " + map.size);
//        System.err.println(map);

        if (opponentsMoveHistory.size() >= 2) {
            OpponentState previous = opponentsMoveHistory.get(opponentsMoveHistory.size() - 2);
            OpponentState current = opponentsMoveHistory.get(opponentsMoveHistory.size() - 1);
            Possibilities p = computeOpponentPositionFromPrevious(previous, current, opponent.direction, opponent.surface);
            possibilities = possibilities.subtract(p);
//            System.err.println("after previous pos: " + map.size);
//            System.err.println(map);
        }

        if (opponent.possibilities != null) {
            possibilities = possibilities.subtract(opponent.possibilities);
//            System.err.println("after parse: " + map.size);
        }
//        System.err.println("Opp can be in " + map.size + " positions");
//        System.err.println(possibilities);

//        Save the computed map
        opponentsMoveHistory.get(opponentsMoveHistory.size() - 1).setComputed(possibilities);

//        Debug
//        for (OpponentState s : opponentsMoveHistory) {
//            System.err.println(s.command + " " + s.possiblePositions.size);
//        }
        return possibilities;
    }

    String getMove() {
        List<Coord> availableMoves = board.getAvailableMoves(current);
        String action = "";
//        System.err.println(availableMoves.size() + " available moves");

        List<Action> actions = parse(opponentRawOrder);
//        Direction opponentDirection = null;
//        for (Action a: actions) {
//            if (a instanceof MoveAction) {
//                opponentDirection = ((MoveAction) a).direction;
//            }
//            else if (a instanceof SilenceAction) {
//
//            }
//            else if (a instanceof TorpedoAction) {
//
//            }
//            else if (a instanceof SurfaceAction) {
//                board.convertSectorToArea();
//            }
//            else if (a instanceof MineAction) {
//            }
//            else {
//
//            }
//        }

        OpponentState currentOS = new OpponentState(opponentRawOrder, null, oppLife);
        opponentsMoveHistory.add(currentOS);
        Possibilities opponent = getOpponentLocation();

//        System.err.println(opponent.size + " possible positions");
        System.err.println(opponent);

        Direction directionToTarget = null;
        Coord opponentPosition = null;

        if (opponent.count > 0 && opponent.count <= 20) {
            if (opponent.count == 1) {
                opponentPosition = opponent.getPossiblePositions().get(0);
                System.err.println("Target acquired: " + opponentPosition);
            }
            else {
                int sumX = 0;
                int sumY = 0;
                int averageX;
                int averageY;
                for (Coord coord : opponent.getPossiblePositions()) {
                    sumX += coord.x;
                    sumY += coord.y;
                }
                averageX = sumX / opponent.getPossiblePositions().size();
                averageY = sumY / opponent.getPossiblePositions().size();
                opponentPosition = new Coord(averageX, averageY);
                System.err.println("Average: " + averageX + ", " + averageY);
            }
        }
        if (opponentPosition != null) {
//            TODO Maybe we could remove the condition current.distance(opponentPosition) > 0 if we can finish the game (more lives than opponent)
//            TODO this could possibly be improved: if we move then shoot, range is higher than 4
            if (current.distance(opponentPosition) <= 4 && current.distance(opponentPosition) > 0 && torpedoCooldown == 0) {
                action = " | TORPEDO " + opponentPosition.x + " " + opponentPosition.y;
            }
            else {
                System.err.println("But too far...");
                directionToTarget = current.getDirection(opponentPosition);
                System.err.println(directionToTarget);
//                Need to move towards the opponent
            }
        }

        String response = null;
        if (availableMoves.size() > 0) {
            if (silenceCooldown == 0) {
//                TODO this one is still random
                response = "SILENCE " + current.getCardinalPoint(availableMoves.get(0)) + " 1";
            }
            else {
                if (directionToTarget != null) {
//                    TODO Actually each of this move could be replaced by a SILENCE 1/2/3/4
                    if (current.distance(opponentPosition) == 0) {
//                        TODO Move away
                    }
                    if (directionToTarget.xVar > 0) {
                        Optional<Coord> coord = availableMoves.stream().filter(c -> c.x > current.x).findAny();
                        if (coord.isPresent()) {
                            response = "MOVE " + current.getCardinalPoint(coord.get());
                            System.err.println("Trying to get closer to the target: E");
                        }
                    }
                    if (response == null && directionToTarget.xVar < 0) {
                        Optional<Coord> coord = availableMoves.stream().filter(c -> c.x < current.x).findAny();
                        if (coord.isPresent()) {
                            response = "MOVE " + current.getCardinalPoint(coord.get());
                            System.err.println("Trying to get closer to the target: W");
                        }
                    }
                    if (response == null && directionToTarget.yVar > 0) {
                        Optional<Coord> coord = availableMoves.stream().filter(c -> c.y > current.y).findAny();
                        if (coord.isPresent()) {
                            response = "MOVE " + current.getCardinalPoint(coord.get());
                            System.err.println("Trying to get closer to the target: S");
                        }
                    }
                    if (response == null && directionToTarget.yVar < 0) {
                        Optional<Coord> coord = availableMoves.stream().filter(c -> c.y < current.y).findAny();
                        if (coord.isPresent()) {
                            response = "MOVE " + current.getCardinalPoint(coord.get());
                            System.err.println("Trying to get closer to the target: N");
                        }
                    }
                }
                if (response == null) {
                    response = "MOVE " + current.getCardinalPoint(availableMoves.get(0));
                }
                response += getLoadAction() + action;
            }
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
        return response + " | MSG BOOM: " + opponent.count;
    }

    void computeVariations(Direction opponentDirection) {
        opponentXVariation += opponentDirection.xVar;
        opponentYVariation += opponentDirection.yVar;
    }

    Possibilities computeOpponentPositionFromHistory() {
//        Returns the list of zones where the opponent can be
//         Ways to find where is the opponent:
//         - The move directions
//         - If it is touched by a torpedo
//         - Where he is launching torpedo
//         int oppLife = in.nextInt();

        //        TODO This needs to be improved to take previous positions into consideration.
        //         The new zone can never be larger than the previous one, except if the opponent used silence
        boolean[][] area = new boolean[board.size][board.size];
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
        return new Possibilities(area, count);
    }

    Possibilities computeOpponentPositionFromPrevious(OpponentState previous, OpponentState current, Direction direction, boolean surface) {
        int lostLives = previous.oppLife - current.oppLife;
        if (!surface && lostLives > 0) {
            if (lostLives == 2) {
                System.err.println("BIM right in the middle !!!");
            }
            else {
                System.err.println("BIM one of the 8 positions around the bomb");
            }
        }

        if (direction != null) {
            return previous.possibilities.shift(direction);
        }
        else {
//            If direction is null, it means the opponent has just played silence or surface.
            if (surface) {
                return previous.possibilities;
            }
            else {
                return previous.possibilities.spread(4);
            }
        }
    }

    List<Action> parse(String opponentOrdersText) {
        List<Action> result = new ArrayList<>();
//       If we start, the first opponent's order is "NA"
        if (!opponentOrdersText.equals("NA")) {
//            System.err.println(opponentOrdersText);
            String[] opponentsOrders = opponentOrdersText.split("\\|");
            for (String order : opponentsOrders) {
                String[] splitOrder = order.split(" ");
                int x;
                int y;
                switch (splitOrder[0]) {
                    case "MOVE":
                        result.add(new MoveAction(new Direction(splitOrder[1])));
                        break;
                    case "TORPEDO":
                        x = Integer.parseInt(splitOrder[1]);
                        y = Integer.parseInt(splitOrder[2]);
                        result.add(new TorpedoAction(new Coord(x, y)));
                        break;
                    case "SURFACE":
                        result.add(new SurfaceAction(Integer.parseInt(splitOrder[1])));
                        break;
                    case "SILENCE":
                        result.add(new SilenceAction());
                        break;
                    case "SONAR":
                        result.add(new SonarAction(Integer.parseInt(splitOrder[1])));
                        break;
                    case "MINE":
                        result.add(new MineAction());
                        break;
                    case "TRIGGER":
                        x = Integer.parseInt(splitOrder[1]);
                        y = Integer.parseInt(splitOrder[2]);
                        result.add(new TriggerAction(new Coord(x, y)));
                        break;
                    default:
                        System.err.println("Action not implemented: " + splitOrder[0]);
                        break;
                }
            }
        }
        return result;
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