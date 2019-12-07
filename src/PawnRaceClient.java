public class PawnRaceClient {
  public static void main(String[] args) {
    Board board = new Board();

    board.print();

    while (board.isPlaying()) {
      //board.makeHumanMove();
      //board.print();
      board.makeAIMove(6);
      board.print();
    }
  }
}
