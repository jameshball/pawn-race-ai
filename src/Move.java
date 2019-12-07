public class Move {
  public int[] from;
  public int[] to;

  public Move(int[] from, int[] to) {
    this.from = from;
    this.to = to;
  }

  public Move(int rFrom, int cFrom, int rTo, int cTo) {
    this(new int[] {rFrom, cFrom}, new int[] {rTo, cTo});
  }

  public boolean isForward() {
    return from[1] == to[1]
      && (from[0] + 1 == to[0] || from[0] - 1 == to[0]);
  }

  public boolean isDoubleForward() {
    return from[1] == to[1]
      && ((from[0] + 2 == to[0] && from[0] == 1) || (from[0] - 2 == to[0] && from[0] == Board.ROWS - 2));
  }

  public boolean isDiagonal() {
    return (from[0] + 1 == to[0] || from[0] - 1 == to[0])
      && (from[1] + 1 == to[1] || from[1] - 1 == to[1]);
  }

  public boolean isEnPassant(Move lastMove) {
    if (lastMove == null) {
      return false;
    }

    return isDiagonal()
      && lastMove.isDoubleForward()
      && to[1] == lastMove.to[1]
      && (to[0] == lastMove.to[0] + 1 || to[0] == lastMove.to[0] - 1);
  }
}
