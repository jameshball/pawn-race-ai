import org.junit.Test;

import static org.junit.Assert.*;

public class TestSuite {

  // TODO: Add tests for diagonal captures on black's starting line.
  @Test
  public void validMoveTest() {
    Board board = new Board();
    Move forwardWhite = new Move(new Vector(6, 0), new Vector(5, 0));
    Move doubleForwardWhite = new Move(new Vector(6, 0), new Vector(4, 0));

    assertTrue("Forward move not valid.",
      board.isValidMove(forwardWhite));
    assertTrue("Double forward move not valid.",
      board.isValidMove(doubleForwardWhite));

    board.makeHumanMove("a4");

    Move forwardBlack = new Move(new Vector(1, 0), new Vector(2, 0));
    Move doubleForwardBlack = new Move(new Vector(1, 0), new Vector(3, 0));

    assertTrue("Forward move not valid.",
      board.isValidMove(forwardBlack));
    assertTrue("Double forward move not valid.",
      board.isValidMove(doubleForwardBlack));

    board.makeHumanMove("b5");

    Move diagonalCaptureWhite = new Move(new Vector(4, 0), new Vector(3, 1));

    assertTrue("Diagonal capture not valid.",
      board.isValidMove(diagonalCaptureWhite));

    board.makeHumanMove("g3");

    Move diagonalCaptureBlack = new Move(new Vector(3, 1), new Vector(4, 0));

    assertTrue("Diagonal capture not valid.",
      board.isValidMove(diagonalCaptureBlack));
  }

  @Test
  public void unmakeMoveTest() {
    Board board = new Board();
    Move forwardWhite = new Move(new Vector(6, 0), new Vector(5, 0));
    Move doubleForwardWhite = new Move(new Vector(6, 0), new Vector(4, 0));

    assertTrue("Forward move not valid.",
      board.isValidMove(forwardWhite));
    assertTrue("Double forward move not valid.",
      board.isValidMove(doubleForwardWhite));

    board.makeHumanMove("a4");

    Move forwardBlack = new Move(new Vector(1, 0), new Vector(2, 0));
    Move doubleForwardBlack = new Move(new Vector(1, 0), new Vector(3, 0));

    assertTrue("Forward move not valid.",
      board.isValidMove(forwardBlack));
    assertTrue("Double forward move not valid.",
      board.isValidMove(doubleForwardBlack));

    board.makeHumanMove("b5");

    Move diagonalCaptureWhite = new Move(new Vector(4, 0), new Vector(3, 1));

    assertTrue("Diagonal capture not valid.",
      board.isValidMove(diagonalCaptureWhite));

    board.makeHumanMove("g3");

    Move diagonalCaptureBlack = new Move(new Vector(3, 1), new Vector(4, 0));

    assertTrue("Diagonal capture not valid.",
      board.isValidMove(diagonalCaptureBlack));
  }
}