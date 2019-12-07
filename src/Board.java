import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Board {
  public static final int ROWS = 8;
  public static final int COLS = 8;

  // false denotes a black pawn, true denotes a white pawn.
  private HashMap<int[], Boolean> pawns;
  private boolean whitesTurn;
  private Move lastMove;

  public Board() {
    resetBoard();
    whitesTurn = true;
  }

  private void resetBoard() {
    pawns = new HashMap<>();

    for (int i = 0; i < COLS; i++) {
      pawns.put(pos(1, i), false);
      pawns.put(pos(ROWS - 2,COLS - 1 - i), false);
    }
  }

  private boolean isValidMove(Move m) {
    Boolean pawn = pawns.get(m.to);

    if (pawn == null) {
      return m.isForward() || m.isDoubleForward() || m.isEnPassant(lastMove);
    }
    else if (pawn != whitesTurn) {
      return m.isDiagonal();
    }

    return false;
  }

  private List<Move> getMoves() {
    List<Move> moves = new ArrayList<>();
    int dir = whitesTurn ? -1 : 1;

    for(Entry<int[], Boolean> pawn : pawns.entrySet()) {
      if (pawn.getValue() == whitesTurn) {
        Move[] potentialMoves = new Move[4];
        int[] pos = pawn.getKey();

        potentialMoves[0] = new Move(pos, new int[] {pos[0] + dir, pos[1]});
        potentialMoves[1] = new Move(pos, new int[] {pos[0] + 2 * dir, pos[1]});
        potentialMoves[2] = new Move(pos, new int[] {pos[0] + dir, pos[1] + 1});
        potentialMoves[3] = new Move(pos, new int[] {pos[0] + dir, pos[1] - 1});

        for(Move m : potentialMoves) {
          if (isValidMove(m)) {
            moves.add(m);
          }
        }
      }
    }
  }

  private int[] pos(int r, int c) {
    return new int[] {r, c};
  }
}
