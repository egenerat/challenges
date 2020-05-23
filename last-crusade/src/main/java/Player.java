import java.util.*;

enum Direction {
    TOP(new int[] {0, -1}),
    LEFT(new int[] {-1, 0}),
    RIGHT(new int[] {1, 0}),
    BOTTOM(new int[] {0, 1});

    private int[] move;

    Direction(int[] move) {
        this.move = move;
    }

    int[] getMove() {
        return move;
    }
}


class Player {

    Direction getDirection(int roomType, Direction entrance) {
        Direction result = null;
        if (Arrays.asList(new Integer[]{1, 3, 7, 8, 9, 12, 13}).contains(roomType) || (roomType == 4 && entrance == Direction.RIGHT) || (roomType == 5 && entrance == Direction.LEFT)) {
            result = Direction.BOTTOM;
        }
        else if (roomType == 10 || ((roomType == 2 || roomType == 6) && entrance == Direction.RIGHT) || (roomType == 4 && entrance == Direction.TOP)) {
            result = Direction.LEFT;
        }
        else if (roomType == 11 || ((roomType == 2 || roomType == 6) && entrance == Direction.LEFT) || (roomType == 5 && entrance == Direction.TOP) ) {
            result = Direction.RIGHT;
        }
        return result;
    }

    String getNextPosition(int x, int y, Direction direction) {
        int[] move = direction.getMove();
        return (x + move[0]) + " " + (y + move[1]);
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt(); // number of columns.
        int H = in.nextInt(); // number of rows.
        if (in.hasNextLine()) {
            in.nextLine();
        }
        int[][] map = new int[H][W];
        for (int i = 0; i < H; i++) {
            String[] values = in.nextLine().split(" "); // represents a line in the grid and contains W integers. Each integer represents one room of a given type.
            for (int j = 0; j < W; j++) {
                map[i][j] = Integer.parseInt(values[j]);
            }
        }
        int ex = in.nextInt(); // the coordinate along the X axis of the exit (not useful for this first mission, but must be read).
        Player p = new Player();

        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            String pos = in.next();

            Direction direction = p.getDirection(map[y][x], Direction.valueOf(pos));
            System.out.println(p.getNextPosition(x, y, direction));
        }
    }
}