import java.util.*;

class SurfacePoint {
    int x;
    int y;

    public SurfacePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class LandingSite {
    int xMin;
    int xMax;
    int xMiddle;
    int altitude;

    public LandingSite(int xMin, int xMax, int altitude) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.xMiddle = (xMin + xMax) / 2;
        this.altitude = altitude;
    }

    public String toString() {
        return xMin + " -> " + xMax + ", altitude: " + altitude;
    }
}

class Player {

    LandingSite findLandingSite(List<SurfacePoint> surfacePoints) {
        int altitude = -1;
        int begin = -1;
        int end = -1;
        for (SurfacePoint s: surfacePoints) {
            if (s.y == altitude) {
                end = s.x;
            }
            else if (end == -1) {
                begin = s.x;
                altitude = s.y;
            }
        }
        return new LandingSite(begin, end, altitude);
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int surfaceN = in.nextInt(); // the number of points used to draw the surface of Mars.
        System.err.println("Size: " + surfaceN);
        List<SurfacePoint> surfacePoints = new ArrayList<>();
        for (int i = 0; i < surfaceN; i++) {
            int landX = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
            int landY = in.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
            surfacePoints.add(new SurfacePoint(landX, landY));
            System.err.println("Land " + landX + ", " + landY);
        }

        Player p = new Player();
        LandingSite landing = p.findLandingSite(surfacePoints);
        System.err.println(landing);

        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int hSpeed = in.nextInt(); // the horizontal speed (in m/s), can be negative.
            int vSpeed = in.nextInt(); // the vertical speed (in m/s), can be negative.
            int fuel = in.nextInt(); // the quantity of remaining fuel in liters.
            int rotate = in.nextInt(); // the rotation angle in degrees (-90 to 90).
            int power = in.nextInt(); // the thrust power (0 to 4).

            if (x <= landing.xMin) {
                System.out.println("-30 4");
            }
            else if (x > landing.xMax) {
                System.out.println("30 4");
            }
            else {
                System.out.println("0 3");
            }
            // rotate power. rotate is the desired rotation angle. power is the desired thrust power.

        }
    }
}
