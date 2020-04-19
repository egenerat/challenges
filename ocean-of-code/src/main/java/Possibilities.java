import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Possibilities {
    boolean[][] area;
    int count;

    //    For the tests, we want to create smaller maps
    private int size;

    Possibilities() {
        this.size = 15;
        this.area = new boolean[size][size];
        for (boolean[] row : area) {
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
//        We iterate over the new map
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
//                If the position i,j is within distance of a possible one, we set it to true
                boolean newValue = false;
                for (int y = Math.max(0, j - distance); y <= Math.min(size - 1, j + distance); y++) {
                    boolean oldValue = area[y][i];
                    newValue = newValue || (oldValue && y != j);
                }
                for (int x = Math.max(0, i - distance); x <= Math.min(size - 1, i + distance); x++) {
                    boolean oldValue = area[j][x];
                    newValue = newValue || (oldValue && x != i);
                }
                if (newValue) {
                    result[j][i] = true;
                    count++;
                }
            }
        }
        return new Possibilities(result, count);
    }

    //    This method will be used to get the damage zone of a torpedo
    Possibilities getNeighbours(Coord impact) {
        boolean[][] result = new boolean[size][size];
        int count = 0;
        for (int j = Math.max(0, impact.y - 1); j <= Math.min(size - 1, impact.x + 1); j++) {
            for (int i = Math.max(0, impact.x - 1); i <= Math.min(size - 1, impact.x + 1); i++) {
                if (!(j == impact.y && i == impact.x)) {
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
        return possible;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof Possibilities)) {
            return false;
        }
        Possibilities c = (Possibilities) other;
        return Arrays.deepEquals(area, c.area) && count == c.count && size == c.size;
    }
}