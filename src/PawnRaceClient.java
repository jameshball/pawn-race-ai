public class PawnRaceClient {
  public static void main(String[] args) {
    Board board = new Board();

    board.print();

    int thing = 5;
    int i = 1;

    while (board.isPlaying()) {
      Move lastMove = board.getLastMove();
      board.makeHumanMove();
      board.print();
      if (i % thing == 0) {
        System.out.println("Move reverted: ");
        board.revertMove(lastMove);
        board.print();
      }

      i++;
    }
  }
}
