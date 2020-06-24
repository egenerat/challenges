import java.util.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int nextCheckpointX = in.nextInt();
            int nextCheckpointY = in.nextInt();
            int nextCheckpointDist = in.nextInt();
            int nextCheckpointAngle = in.nextInt();
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();

            String thrust = "100";
            if (Math.abs(nextCheckpointAngle) > 90) {
                thrust = "0";
            }
            else if (Math.abs(nextCheckpointAngle) < 10 && nextCheckpointDist > 3000) {
                thrust = "BOOST";
            }

            System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + thrust);
        }
    }
}