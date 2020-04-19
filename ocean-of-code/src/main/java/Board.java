import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    List<Coord> getAvailableMoves(Coord current, int distance) {
        List<Coord> result = new ArrayList<>();

//        X decreasing (W)
        boolean isAvailable = true;
        int d = 1;
        while (isAvailable && d <= distance) {
            if (!(current.x >= d && getCell(current.x - d, current.y) != CellType.ISLAND && !visited[current.y][current.x - d])) {
                isAvailable = false;
            } else {
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
            } else {
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
            } else {
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
            } else {
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