import java.util.*;

class Direction {
    static final String LEFT = "LEFT";
    static final String RIGHT = "RIGHT";
}

class Order {
    static final String WAIT = "WAIT";
    static final String BLOCK = "BLOCK";
}


class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int nbFloors = in.nextInt(); // number of floors
        int width = in.nextInt(); // width of the area
        int nbRounds = in.nextInt(); // maximum number of rounds
        int exitFloor = in.nextInt(); // floor on which the exit is found
        int exitPos = in.nextInt(); // position of the exit on its floor
        int nbTotalClones = in.nextInt(); // number of generated clones
        int nbAdditionalElevators = in.nextInt(); // ignore (always zero)
        int nbElevators = in.nextInt(); // number of elevators

//        We store the exit position for each floor
        int[] floors = new int[nbFloors];

//        Add the elevators for each floor
        for (int i = 0; i < nbElevators; i++) {
            int elevatorFloor = in.nextInt(); // floor on which this elevator is found
            int elevatorPos = in.nextInt(); // position of the elevator on its floor
            floors[elevatorFloor] = elevatorPos;
        }

//        Add the final exit
        floors[exitFloor] = exitPos;

        boolean blockNext = false;

        while (true) {
            int cloneFloor = in.nextInt(); // floor of the leading clone
            int clonePos = in.nextInt(); // position of the leading clone on its floor
            String direction = in.next(); // direction of the leading clone: LEFT or RIGHT

            System.err.println("Leading clone: " + cloneFloor + ", " + clonePos + " " + direction);
            if (cloneFloor >= 0) {
                if (floors[cloneFloor] < clonePos && direction.equals(Direction.RIGHT))  {
                    blockNext = true;
                }
                if (floors[cloneFloor] > clonePos && direction.equals(Direction.LEFT))  {
                    blockNext = true;
                }
                if (blockNext) {
                    System.out.println(Order.BLOCK);
                    blockNext = false;
                }
                else {
                    System.out.println(Order.WAIT);
                }
            }
            else {
                System.out.println(Order.WAIT);
            }
        }
    }
}