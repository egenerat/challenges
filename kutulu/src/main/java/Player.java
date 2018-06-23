// no package as we want to copy paste the result in the web editor
import java.util.*;

class Player {

    Board board;
    Explorer myExplorer;

    class Board {
        private List<String> map;
        public Board(List<String> map) {
            this.map = map;
        }
    }

    class Entity {
        private int id;
        private int x;
        private int y;

        public Entity(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    class Wanderer extends Entity {

        public Wanderer(int id, int x, int y) {
            super(id, x, y);
        }
    }

    class Explorer extends Entity {

        public Explorer(int id, int x, int y) {
            super(id, x, y);
        }
    }

    public int calculateManhattanDistanceBetween2Points() {
        return -1;
    }

    public int findClosestWanderer() {
        return -1;
    }

    public void refreshTurn(Scanner in) {
        int entityCount = in.nextInt(); // the first given entity corresponds to your explorer
        List<Wanderer> wanderers = new ArrayList<>();
        for (int i = 0; i < entityCount; i++) {
            String entityType = in.next();
            int id = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int param0 = in.nextInt(); //      Explorer: sanity
//                Spawning minion: time before spawn
//                Wanderer: time before being recalled
            int param1 = in.nextInt(); // Minion: Current state amongst those -> SPAWNING = 0 , WANDERING = 1
            int param2 = in.nextInt(); // Minion: id of the explorer targeted by this minion. -1 if no target (occurs only on spawn)
            if (entityType.equals("EXPLORER")) {
                Explorer explorer = new Explorer(id, x, y);
                if (i == 0) {
                    myExplorer = explorer;
                }
            } else {
                wanderers.add(new Wanderer(id, x, y));
            }
        }
    }

    public void play() {
//        System.out.println("MOVE x y");
        System.out.println("WAIT");
    }

    public void initializeInput(Scanner in) {
        Board board = new Board();
        int width = in.nextInt();
        int height = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        List<String> map = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            map.add(in.nextLine());
        }
        int sanityLossLonely = in.nextInt(); // how much sanity you lose every turn when alone, always 3 until wood 1
        int sanityLossGroup = in.nextInt(); // how much sanity you lose every turn when near another player, always 1 until wood 1
        int wandererSpawnTime = in.nextInt(); // how many turns the wanderer take to spawn, always 3 until wood 1
        int wandererLifeTime = in.nextInt(); // how many turns the wanderer is on map after spawning, always 40 until wood 1
        this.board = new Board(map);
    }

    public static void main(String... args) {
        Player player = new Player();
        Scanner in = new Scanner(System.in);
        player.initializeInput(in);
        while(true) {
            player.refreshTurn(in);
            player.play();
        }
    }
}