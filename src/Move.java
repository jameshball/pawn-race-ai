public class Move {
  public Vector from;
  public Vector to;

  public Move(Vector from, Vector to) {
    this.from = from;
    this.to = to;
  }

  public Move(int rFrom, int cFrom, int rTo, int cTo) {
    this(new Vector(rFrom, cFrom), new Vector(rTo, cTo));
  }

  public boolean isForward() {
    return from.c == to.c
      && (from.r + 1 == to.r || from.r - 1 == to.r);
  }

  public boolean isDoubleForward() {
    return from.c == to.c
      && ((from.r + 2 == to.r && from.r == 1) || (from.r - 2 == to.r && from.r == Board.ROWS - 2));
  }

  public boolean isDiagonal() {
    return (from.r + 1 == to.r || from.r - 1 == to.r)
      && (from.c + 1 == to.c || from.c - 1 == to.c);
  }

  public boolean isEnPassant(Move lastMove) {
    if (lastMove == null) {
      return false;
    }

    return isDiagonal()
      && lastMove.isDoubleForward()
      && to.c == lastMove.to.c
      && ((to.r == lastMove.to.r + 1 && to.r == Board.ROWS - 3) || (to.r == lastMove.to.r - 1 && to.r == 2));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append((char) ('a' + from.c));
    sb.append(Board.ROWS - from.r);
    sb.append('x');
    sb.append((char) ('a' + to.c));
    sb.append(Board.ROWS - to.r);

    if (isForward() || isDoubleForward()) {
      return sb.substring(3, 5);
    }
    else {
      return sb.toString();
    }
  }

  public Move deepCopy() {
    return new Move(new Vector(from.r, from.c), new Vector(to.r, to.c));
  }
}
