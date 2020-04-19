class ParserOutput {
    Direction direction;
    Possibilities possibilities;
    boolean surface;
    boolean silence;

    public ParserOutput(Direction direction, Possibilities possibilities, boolean surface, boolean silence) {
        this.direction = direction;
        this.possibilities = possibilities;
        this.surface = surface;
        this.silence = silence;
    }
}