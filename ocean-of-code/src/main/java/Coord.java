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