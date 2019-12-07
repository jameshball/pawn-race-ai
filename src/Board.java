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

  public boolean isValidMove(Move m) {
    Boolean pawn = pawns.get(m.to);

    if (pawn == null) {
      //return m.isForward() || m.isDoubleForward() || m.isEnPassant(lastMove);
      return m.isForward() || m.isDoubleForward();
    }
    else if (pawn != whitesTurn) {
      return m.isDiagonal();
    }

    return false;
  }

  private int evaluate() {
    if (state.equals(GameState.WHITE_WON)) {
      return whitesTurn ? 20 : -20;
    }

    if (state.equals(GameState.BLACK_WON)) {
      return whitesTurn ? -20 : 20;
    }

    return (whitesTurn ? 1 : -1) * (whitePawnCount - blackPawnCount);
  }

  private MoveValuePair negaMax(int depth) {
    if (state.equals(GameState.WHITE_WON) || state.equals(GameState.BLACK_WON)) {
      depth = 0;
    }
    if (depth == 0) {
      return new MoveValuePair(null, evaluate());
    }

    List<Move> moves = getMoves();

    if (state.equals(GameState.DRAW)) {
      return new MoveValuePair(null, 0);
    }

    int max = Integer.MIN_VALUE;
    Move bestMove = null;

    for (Move m : moves) {
      Move secondToLastMove = lastMove == null ? null : lastMove.deepCopy();
      makeMove(m);
      MoveValuePair moveValue = negaMax(depth - 1);
      unmakeMove(secondToLastMove);
      int score = -moveValue.getValue();

      if (score > max) {
        max = score;
        bestMove = m;
      }
    }

    return new MoveValuePair(bestMove, max);
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

    if (moves.isEmpty()) {
      state = GameState.DRAW;
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
      whitePawnCount++;
    }
    else {
      blackPawnCount++;
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

  public void makeHumanMove(String move) {
    List<Move> moves = getMoves();

    for (Move m : moves) {
      if (m.toString().equals(move)) {
        makeMove(m);
      }
    }
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
          makeMove(m);
        }
      }

      if (!validMoveFound) {
        System.out.println("The move entered is incorrect; please try again.");
      }
    }
  }

  public void makeAIMove(int depth) {
    Move nextMove = negaMax(depth).getMove();

    if (nextMove != null) {
      makeMove(nextMove);
    }
  }

  public Move getLastMove() {
    return lastMove;
  }

  private void makeMove(Move m) {
    if (!isValidMove(m)) {
      print();
      System.out.println(m);
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

  public void unmakeMove(Move secondToLastMove) {
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

    state = GameState.PLAYING;

    whitesTurn = !whitesTurn;
    lastMove = secondToLastMove;
  }
}
