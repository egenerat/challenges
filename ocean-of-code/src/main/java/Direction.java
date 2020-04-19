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

    Direction(int xVar, int yVar) {
        this.xVar = xVar;
        this.yVar = yVar;
    }

    public String toString() {
        return "Direction: " + xVar + ", " + yVar;
    }
}