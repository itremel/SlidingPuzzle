package players;

import java.util.List;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

public class MiniMaxPlayer extends AbstractPlayer {

	private Move bestMove = null;
	private final static int maxDepth = 6;
	
	public MiniMaxPlayer(int depth) {
		super(depth);
	}

	@Override
	public BoardSquare play(int[][] tab) {
		OthelloGame game = new OthelloGame();
		minimax(tab, this.getMyBoardMark(), game);
		
		if(bestMove != null && game.validate_moviment(tab, bestMove.getBoardPlace(), this) == 0) return bestMove.getBoardPlace();
		else {
			System.out.println("no move found");
			return new BoardSquare(-1, -1);
		}
	}
	
	public int getScore(int player, int[][] board){
		int score = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j] == player) score++;
				if(board[i][j] == -player) score--;
			}
		}
		score /= 3;
		
		List<Move> moves = getGame().getValidMoves(board, player);
		if (player == getMyBoardMark()) {
			return score + moves.size();
		} else {
			return score - moves.size();
		}
	}
	
	public int[][] copyArray(int[][] a){
		int [][] myInt = new int[a.length][];
		for(int i = 0; i < a.length; i++)
		    myInt[i] = a[i].clone();
		return myInt;	
	}
	
	public int minimax(int[][] board, int player, OthelloGame game) {
		if (player == this.getMyBoardMark()) {
			return valueMax(board, player, maxDepth, game);
		} else {
			return valueMin(board, player, maxDepth, game);
		}
	}

	private int valueMax(int[][] board, int player, int depth, OthelloGame game) {
		int best = Integer.MIN_VALUE;
		if (depth <= 0 || game.noSpace(board)) {
			return getScore(player, board);
		}
		List<Move> moves = getGame().getValidMoves(board, player);
		int[][] subBoard = copyArray(board);
		for (Move move : moves) {
			if(depth == maxDepth) bestMove = move;
			subBoard = game.do_move(subBoard, move.getBoardPlace(), this);
			
			int value = valueMin(subBoard, this.getOpponentBoardMark(), depth - 1, game);
			subBoard = copyArray(board);
			if (value > best) {
				best = value;
			}
		}
		return best;
	}

	private int valueMin(int[][] board, int player, int depth, OthelloGame game) {
		int best = Integer.MAX_VALUE;
		if (depth <= 0 || game.noSpace(board)) {
			return getScore(player, board);
		}
		List<Move> moves = getGame().getValidMoves(board, player);
		int[][] subBoard = copyArray(board);
		for (Move move : moves) {
			subBoard = game.do_move(subBoard, move.getBoardPlace(), this);
			int value = valueMax(subBoard, this.getMyBoardMark(), depth - 1, game);
			subBoard = copyArray(board);
			if (value < best) {
				best = value;
			}
		}
		return best;
	}
}

	
