class OpponentState {
    String command;
    Direction direction;
    Possibilities possibilities;
    int oppLife;
    Coord myTorpedoAttack;

    OpponentState(String command, Direction direction, int oppLife) {
        this.command = command;
        this.direction = direction;
        this.oppLife = oppLife;
    }

    void setComputed(Possibilities possibilities) {
        this.possibilities = possibilities;
    }

    public void setMyTorpedoAttack(Coord myTorpedoAttack) {
        this.myTorpedoAttack = myTorpedoAttack;
    }
}