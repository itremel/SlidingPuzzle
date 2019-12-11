package players;

import java.util.List;
import java.util.Random;


import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

/**
 * @author Ivo
 *	https://int8.io/monte-carlo-tree-search-beginners-guide/
 */
public class MCTSPlayer extends AbstractPlayer {

	private final static int simulations_number = 1000;
	
	public MCTSPlayer(int depth) {
		super(depth);
	}
	
	
	@Override
	public BoardSquare play(int[][] tab) {
		int[][] subBoard = copyArray(tab);
		MCTSNode root = new MCTSNode(null, subBoard, this, null);
		MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(root);
		MCTSNode best_node = mcts.best_action(simulations_number);
		Move m = best_node.getMove();
		if(m != null) return m.getBoardPlace();
		else {
			System.out.println("no move found");
			return new BoardSquare(-1, -1);
		}
		
	}
	
	public static int[][] copyArray(int[][] a){
		int [][] myInt = new int[a.length][];
		for(int i = 0; i < a.length; i++)
		    myInt[i] = a[i].clone();
		return myInt;	
	}


}
