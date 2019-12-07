import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class Board {
  public static final int ROWS = 8;
  public static final int COLS = 8;

  // false denotes a black pawn, true denotes a white pawn.
  private HashMap<Vector, Boolean> pawns;
  private boolean whitesTurn;
  private Move lastMove;
  private int whitePawnCount;
  private int blackPawnCount;
  private GameState state;

  public Board() {
    resetBoard();
    whitesTurn = true;
    state = GameState.PLAYING;
  }

  public void print() {
    System.out.println(this);
    System.out.println(lastMove);
    System.out.println(stateToString());
    System.out.println();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append('\t');

    for (int i = 0; i < COLS; i++) {
      sb.append((char) ('A' + i));
      sb.append(' ');
    }

    sb.append('\n');

    for (int i = 0; i < ROWS; i++) {
      sb.append(ROWS - i);
      sb.append('\t');

      for (int j = 0; j < COLS; j++) {
        Boolean pawn = pawns.get(new Vector(i, j));

        if (pawn == null) {
          sb.append(". ");
        }
        else if (pawn) {
          sb.append("W ");
        }
        else {
          sb.append("B ");
        }
      }

      sb.append('\n');
    }

    return sb.toString();
  }

  private String stateToString() {
    switch (state) {
      case BLACK_WON:
        return "Black wins!";
      case WHITE_WON:
        return "White wins!";
      case DRAW:
        return "It's a draw!";
      default:
        return "";
    }
  }

  private void resetBoard() {
    pawns = new HashMap<>();

    for (int i = 0; i < COLS; i++) {
      pawns.put(new Vector(ROWS - 2,COLS - 1 - i), true);
      whitePawnCount++;
      pawns.put(new Vector(1, i), false);
      blackPawnCount++;
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

    for(Entry<Vector, Boolean> pawn : pawns.entrySet()) {
      if (pawn.getValue() == whitesTurn) {
        Move[] potentialMoves = new Move[4];
        Vector pos = pawn.getKey();

        potentialMoves[0] = new Move(pos, new Vector(pos.r + dir, pos.c));
        potentialMoves[1] = new Move(pos, new Vector(pos.r + 2 * dir, pos.c));
        potentialMoves[2] = new Move(pos, new Vector(pos.r + dir, pos.c + 1));
        potentialMoves[3] = new Move(pos, new Vector(pos.r + dir, pos.c - 1));

        for(Move m : potentialMoves) {
          if (isValidMove(m)) {
            moves.add(m);
          }
        }
      }
    }

    return moves;
  }

  private void pawnCaptured() {
    if (whitesTurn) {
      blackPawnCount--;
    }
    else {
      whitePawnCount--;
    }
  }

  private void pawnUncaptured() {
    if (whitesTurn) {
      blackPawnCount++;
    }
    else {
      whitePawnCount++;
    }
  }

  private void checkWon(Move m) {
    if (m.to.r == 0) {
      state = GameState.WHITE_WON;
    }
    else if (m.to.r == ROWS - 1) {
      state = GameState.BLACK_WON;
    }
  }

  public boolean isPlaying() {
    return state.equals(GameState.PLAYING);
  }

  public void makeHumanMove() {
    List<Move> moves = getMoves();
    boolean validMoveFound = false;
    Scanner input = new Scanner(System.in);

    System.out.println("Please enter a valid move in algebraic notation.");

    while (!validMoveFound) {
      String humanMove = input.nextLine().toLowerCase().replaceAll(" ", "");

      for (Move m : moves) {
        if (m.toString().equals(humanMove)) {
          validMoveFound = true;
          applyMove(m);
        }
      }

      if (!validMoveFound) {
        System.out.println("The move entered is incorrect; please try again.");
      }
    }
  }

  public Move getLastMove() {
    return lastMove;
  }

  private void applyMove(Move m) {
    if (!isValidMove(m)) {
      throw new IllegalArgumentException("Move applied is not valid.");
    }

    if (m.isDiagonal()) {
      pawnCaptured();
    }

    if (m.isEnPassant(lastMove)) {
      pawns.remove(lastMove.to);
    }

    pawns.put(m.to, whitesTurn);
    pawns.remove(m.from);

    checkWon(m);

    whitesTurn = !whitesTurn;
    lastMove = m;
  }

  public void revertMove(Move secondToLastMove) {
    if (lastMove.isDiagonal()) {
      pawnUncaptured();

      if (lastMove.isEnPassant(secondToLastMove)) {
        pawns.put(secondToLastMove.to, whitesTurn);
        pawns.remove(lastMove.to);
      }
      else {
        pawns.put(lastMove.to, whitesTurn);
      }
    }
    else {
      pawns.remove(lastMove.to);
    }

    pawns.put(lastMove.from, !whitesTurn);

    whitesTurn = !whitesTurn;
    lastMove = secondToLastMove;
  }
}
