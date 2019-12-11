package players;

import java.util.List;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

public class AlphaBetaPrunePlayer extends AbstractPlayer {

	private Move bestMove;
	private RandomPlayer opponent;
	
	public AlphaBetaPrunePlayer(int depth) {
		super(depth);
		System.out.println(depth);
		opponent = new RandomPlayer(depth);
	}

	@Override
	public BoardSquare play(int[][] tab) {
		bestMove = null;
		opponent.setBoardMark(this.getOpponentBoardMark());
		opponent.setOpponentBoardMark(this.getBoardMark());
		
		OthelloGame game = new OthelloGame();
		int[][] board = copyArray(tab);
		int score = minimax(board, this.getMyBoardMark(), game);
//		System.out.println("score: " + score);
		if(bestMove != null) return bestMove.getBoardPlace();
		else {
			System.out.println("no move found");
			return new BoardSquare(-1, -1);
		}
	}
	
	public int getScore(int player, int[][] board){
		int score = 0;
//		//Punktedifferenz
//		if(player == getBoardMark()) {
//			for(int i = 0; i < board.length; i++){
//				for(int j = 0; j < board[i].length; j++){
//					if(board[i][j] == player) score++;
//					if(board[i][j] == -player) score--;
//				}
//			}
//		}else {
//			for(int i = 0; i < board.length; i++){
//				for(int j = 0; j < board[i].length; j++){
//					if(board[i][j] == player) score--;
//					if(board[i][j] == -player) score++;
//				}
//			}
//		}
////		score /= 3;
//		
		//Eckpunkte
		if(player == getBoardMark()) {
			if(board[0][0] == player)score+=20;
			if(board[0][getGame().size-1] == player)score+=20;
			if(board[getGame().size-1][0] == player)score+=20;
			if(board[getGame().size-1][getGame().size-1] == player)score+=20;
			
			if(board[0][0] == -player)score-=20;
			if(board[0][getGame().size-1] == -player)score-=20;
			if(board[getGame().size-1][0] == -player)score-=20;
			if(board[getGame().size-1][getGame().size-1] == -player)score-=20;
		}else {
			if(board[0][0] == player)score-=20;
			if(board[0][getGame().size-1] == player)score-=20;
			if(board[getGame().size-1][0] == player)score-=20;
			if(board[getGame().size-1][getGame().size-1] == player)score-=20;
			
			if(board[0][0] == -player)score+=20;
			if(board[0][getGame().size-1] == -player)score+=20;
			if(board[getGame().size-1][0] == -player)score+=20;
			if(board[getGame().size-1][getGame().size-1] == -player)score+=20;
		}
		
		
		//Mobilität
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
			return valueMax(board, player, this.getDepth(), game, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	private int valueMax(int[][] board, int player, int depth, OthelloGame game, int alpha, int beta) {
		if (depth == 0 || game.getValidMoves(board, player).size() == 0) {
			return getScore(player, board);
		}
		int maxWert = alpha;
		List<Move> moves = game.getValidMoves(board, player);
		int[][] subBoard = copyArray(board);
		for (Move move : moves) {
			subBoard = game.do_move(subBoard, move.getBoardPlace(), this);
			int value = valueMin(subBoard, this.getOpponentBoardMark(), depth - 1, game, maxWert, beta);
			subBoard = copyArray(board);
			if (value > maxWert) { 
				maxWert = value;
				if(depth == this.getDepth()) 
					bestMove = move;
				if(maxWert >= beta) 
					break;
			}
		}
		return maxWert;
	}

	private int valueMin(int[][] board, int player, int depth, OthelloGame game, int alpha, int beta) {
		if (depth == 0 || game.getValidMoves(board, player).size() == 0) {
			return getScore(player, board);
		}
		int minWert = beta;
		List<Move> moves = game.getValidMoves(board, player);
		int[][] subBoard = copyArray(board);
		for (Move move : moves) {
			subBoard = game.do_move(subBoard, move.getBoardPlace(), opponent);
			int value = valueMax(subBoard, this.getBoardMark(), depth - 1, game, alpha, minWert);
			subBoard = copyArray(board);
			if (value < minWert) {
				minWert = value;
				if(minWert <= alpha)
					break;
			}
		}
		return minWert;
	}
}

	
