package players;

import java.util.List;
import java.util.Random;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

public class MiniMaxPlayer extends AbstractPlayer {

	public MiniMaxPlayer(int depth) {
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
	
	/* Test implementation */
//	public int minimax(int[][] board, Evaluation function) {
//		if (player == Player.WHITE) {	/* White is the maximizing player */
//			return valueMax(board, player, depth, function);
//		} else {			/* Black is the minimizing player */
//			return valueMin(board, player, depth, function);
//		}
//	}
//
//	private int valueMax(int[][] board, Player player, int depth, Evaluation function) {
//		int best = Integer.MIN_VALUE;
//		if (depth <= 0 || isEndState(board)) {
//			return function.evaluate(board, player);
//		}
//		Set<Point> possibleMoves = MoveExplorer.explore(board, player.color());
//		Board subBoard = board.clone();
//		for (Point move : possibleMoves) {
//			subBoard.makeMove(move, player.color());
//			int value = valueMin(board, player.opponent(), depth - 1, function);
//			subBoard = board.clone();
//			if (value > best) {
//				best = value;
//			}
//		}
//		return best;
//	}
//
//	private int valueMin(Board board, Player player, int depth, Evaluation function) {
//		int best = Integer.MAX_VALUE;
//		if (depth <= 0 || isEndState(board)) {
//			return function.evaluate(board, player);
//		}
//		Set<Point> possibleMoves = MoveExplorer.explore(board, player.color());
//		Board subBoard = board.clone();
//		for (Point move : possibleMoves) {
//			subBoard.makeMove(move, player.color());
//			int value = valueMax(board, player.opponent(), depth - 1, function);
//			subBoard = board.clone();
//			if (value < best) {
//				best = value;
//			}
//		}
//		return best;
//	}
}

	
