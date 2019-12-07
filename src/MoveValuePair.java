public class MoveValuePair {
  private Move move;
  private int value;

  public MoveValuePair(Move move, int value) {
    this.move = move;
    this.value = value;
  }

  public Move getMove() {
    return move;
  }

  public int getValue() {
    return value;
  }
}
