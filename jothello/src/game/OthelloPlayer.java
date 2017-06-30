package game;

/**
 *
 * @author iuri
 */
public class OthelloPlayer extends AbstractPlayer {

    public OthelloPlayer(int depth) {
        super(depth);
        // TODO Auto-generated constructor stub
    }

    @Override
    public BoardSquare play(int[][] tab) {
        BoardSquare boardPlace = new BoardSquare();
        Boolean validBoardPlace = false;
        loop:
        for (int i = 0; i < getGame().size; i++) {
            for (int j = 0; j < getGame().size; j++) {

                if (getGame().validate_moviment(tab, new BoardSquare(i, j), this) == 0) {
                	validBoardPlace=true;
                }

                if (tab[i][j] == 0 && validBoardPlace) {
                    boardPlace.getRow(i);
                    boardPlace.setCol(j);
                    break loop;
                }
            }
        }
        return boardPlace;
    }

    public Boolean checkRowCol(int[][] board, int row, int cow) {
        Boolean valid = false;
        for (int i = 0; i < getGame().size; i++) {
            if (board[row][i] == getBoardMark()
                    || board[i][cow] == getBoardMark()) {
                valid = true;
                break;
            }
            if (row - i >= 0 && cow - i >= 0) {
                if (board[row - i][cow - i] == getBoardMark()) {
                    valid = true;
                    break;
                }
            }
            if (row + i < getGame().size && cow + i < getGame().size) {
                if (board[row + i][cow + i] == getBoardMark()) {
                    valid = true;
                    break;
                }
            }
            if (row + i < getGame().size && cow - i >= 0) {
                if (board[row + i][cow - i] == getBoardMark()) {
                    valid = true;
                    break;
                }
            }
            if (row - i >= 0 && cow + i < getGame().size) {
                if (board[row - i][cow + i] == getBoardMark()) {
                    valid = true;
                    break;
                }
            }
        }

        return valid;
    }
}
