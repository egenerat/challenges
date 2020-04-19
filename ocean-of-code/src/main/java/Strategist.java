import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    Possibilities getOpponentLocation(ParserOutput opponent) {
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

    String getMoveOrSilence(String direction) {
        if (silenceCooldown == 0) {
            return "SILENCE " + direction + " 1";
        }
        else {
            return "MOVE " + direction;
        }
    }

    String getMove() {
        List<Coord> availableMoves;
        if (silenceCooldown == 0) {
            availableMoves = board.getAvailableMoves(current, 4);
        } else {
            availableMoves = board.getAvailableMoves(current, 1);
        }
        String action = "";
//        System.err.println(availableMoves.size() + " available moves");

//        This one should only be called if MOVE
        ParserOutput parserOutput = parse(opponentRawOrder);
        OpponentState latestOS = new OpponentState(opponentRawOrder, parserOutput.direction, oppLife);
        opponentsMoveHistory.add(latestOS);
        Possibilities opponent = getOpponentLocation(parserOutput);

//        System.err.println(opponent.size + " possible positions");
        System.err.println(opponent);

        Direction directionToTarget = null;
        Coord opponentPosition = null;

        if (opponent.count > 0 && opponent.count <= 20) {
            if (opponent.count == 1) {
                opponentPosition = opponent.getPossiblePositions().get(0);
                System.err.println("Target acquired: " + opponentPosition);
            } else {
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
//                setMyTorpedoAttack(new Coord(opponentPosition.x, opponentPosition.y));
            } else {
                System.err.println("But too far...");
                directionToTarget = current.getDirection(opponentPosition);
                System.err.println(directionToTarget);
//                Need to move towards the opponent
            }
        }

        String response = null;
        if (availableMoves.size() > 0) {
            if (directionToTarget != null) {
//                    TODO Actually each of this move could be replaced by a SILENCE 1/2/3/4
                if (current.distance(opponentPosition) == 0) {
//                        TODO Move away
                }
                if (directionToTarget.xVar > 0) {
                    Optional<Coord> coord = availableMoves.stream().filter(c -> c.x > current.x).findAny();
                    if (coord.isPresent()) {
                        response = getMoveOrSilence(current.getCardinalPoint(coord.get()));
                        System.err.println("Trying to get closer to the target: E");
                    }
                }
                if (response == null && directionToTarget.xVar < 0) {
                    Optional<Coord> coord = availableMoves.stream().filter(c -> c.x < current.x).findAny();
                    if (coord.isPresent()) {
                        response = getMoveOrSilence(current.getCardinalPoint(coord.get()));
                        System.err.println("Trying to get closer to the target: W");
                    }
                }
                if (response == null && directionToTarget.yVar > 0) {
                    Optional<Coord> coord = availableMoves.stream().filter(c -> c.y > current.y).findAny();
                    if (coord.isPresent()) {
                        response = getMoveOrSilence(current.getCardinalPoint(coord.get()));
                        System.err.println("Trying to get closer to the target: S");
                    }
                }
                if (response == null && directionToTarget.yVar < 0) {
                    Optional<Coord> coord = availableMoves.stream().filter(c -> c.y < current.y).findAny();
                    if (coord.isPresent()) {
                        response = getMoveOrSilence(current.getCardinalPoint(coord.get()));
                        System.err.println("Trying to get closer to the target: N");
                    }
                }
            }
            if (response == null) {
                response = getMoveOrSilence(current.getCardinalPoint(availableMoves.get(0)));
            }
            response += getLoadAction() + action;
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
//        It may not work great if the opponent self-inflicts damages
        int lostLives = previous.oppLife - current.oppLife;
        if (!surface && lostLives > 0) {
            //                    TODO: Retrieve the position of the torpedo we threw
            if (lostLives == 2) {
                System.err.println("BIM right in the middle !!!");
            } else {
//                    getNeighbours(null);
                System.err.println("BIM one of the 8 positions around the bomb");
            }
        }

        if (direction != null) {
            return previous.possibilities.shift(direction);
        } else {
//            If direction is null, it means the opponent has just played silence or surface.
            if (surface) {
                return previous.possibilities;
            } else {
                return previous.possibilities.spread(4);
            }
        }
    }

    ParserOutput parse(String opponentOrdersText) {
        Direction opponentDirection = null;
        Possibilities possibilities = new Possibilities();
        boolean surface = false;
        boolean silence = false;

//       If we start, the first opponent's order is "NA"
        if (!opponentOrdersText.equals("NA")) {
//            System.err.println(opponentOrdersText);
            String[] opponentsOrders = opponentOrdersText.split("\\|");
            for (String order : opponentsOrders) {
                String[] splitOrder = order.split(" ");
                if (splitOrder[0].equals("MOVE")) {
                    opponentDirection = new Direction(splitOrder[1]);
                } else if (splitOrder[0].equals("TORPEDO")) {
//                    System.err.println("/!\\ BOOOOOOOMMMMMMMM");
                    int x = Integer.parseInt(splitOrder[1]);
                    int y = Integer.parseInt(splitOrder[2]);
//                    System.err.println("Target: " + x + ", " + y);
                    possibilities = board.findTorpedoLaunchArea(new Coord(x, y));
                } else if (splitOrder[0].equals("SURFACE")) {
//                    System.err.println("Opponent has surfaced in : " + splitOrder[1]);
                    possibilities = board.convertSectorToArea(Integer.parseInt(splitOrder[1]));
                    surface = true;
//                    System.err.println(map);
                } else if (splitOrder[0].equals("SILENCE")) {
                    System.err.println("Silence...");
                    silence = true;
                }
            }
        }
//        System.err.println("End of parse:");
//        System.err.println(map);
        return new ParserOutput(opponentDirection, possibilities, surface, silence);
    }
}