package players;

import java.util.List;
import java.util.Random;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

public class RandomPlayer extends AbstractPlayer {

	public RandomPlayer(int depth) {
		super(depth);
	}
	
	
	@Override
	public BoardSquare play(int[][] tab) {
		OthelloGame game = new OthelloGame();
		Random r = new Random();
		List<Move> moves = game.getValidMoves(tab, getMyBoardMark());
		if (moves.size() > 0) {
			return moves.get(r.nextInt(moves.size())).getBoardPlace();
		} else {
			return new BoardSquare(-1, -1);
		}
		
		
	}

}
