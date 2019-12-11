package players;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

/**
 * @author Ivo
 *
 */
public class MCTSNode  {

	private Move move;
	private int[][] state;
	private MCTSNode parent;
	LinkedList<MCTSNode> children;
    // hashmap to store the frequency of element 
	Map<Integer, Integer> results;
	private int number_of_visits;
	List<Move> untried_actions;
	//dummy players for do_move function as it needs AbstractPlayer with opposite Boardmarks
	AbstractPlayer player; 
	AbstractPlayer opponent; 
	
	
	
	public MCTSNode(Move m, int[][] state, AbstractPlayer abstractPlayer, MCTSNode parent) {
		move = m;
		this.state = state;
		this.parent = parent;
		children = new LinkedList<>();
        number_of_visits = 0;
			
		player=new RandomPlayer(abstractPlayer.getDepth());
		player.setBoardMark(abstractPlayer.getBoardMark());
		player.setOpponentBoardMark(abstractPlayer.getOpponentBoardMark());

		opponent=new RandomPlayer(abstractPlayer.getDepth());
		opponent.setBoardMark(abstractPlayer.getOpponentBoardMark());
		opponent.setOpponentBoardMark(abstractPlayer.getBoardMark());
		
		OthelloGame game = new OthelloGame();
		untried_actions = game.getValidMoves(state, player.getBoardMark());
		
		results = new HashMap<Integer, Integer>(); 
        //initialize wins, loss, draw with 0
        results.put(1, 0); //wins
        results.put(-1, 0); //loss
        results.put(2, 0); //draw

	}
	
	/**
	 * if child has playerboardmark "1", its parent has playerboardmark "-1" 
	 * 		--> wins are games ending with "-1" result, losses are games ending with "1" result
	 * if child has playerboardmark "-1", its parent has playerboardmark "1" -->
	 * 		--> wins are games ending with "1" result, losses are games ending with "-1" result
	 * 
	 * @return wins from parent boardmark (colour/X/O/"1"/"-1") perspective 
	 */
	public int wins_nominator() {
		if(player.getBoardMark() == -1) { //player is child as function call goes child.wins_nominator
			int wins = results.get(1);
			int loss = results.get(-1);
			return wins - loss;
		} else {
			int loss = results.get(1);
			int wins = results.get(-1);
			return wins - loss;

		}
			
	}
	
	/**
	 * @return 	1 if boardmark 1 won
	 * 			-1 if boardmark -1 won
	 * 			2 for draw
	 */
	public int rollout() {
		int[][] current_rollout_state = MCTSPlayer.copyArray(state);
		OthelloGame game = new OthelloGame();
		AbstractPlayer activePlayer = player;
		int isEvenRun = 0;
		while(game.testing_end_game(current_rollout_state, activePlayer.getMyBoardMark())==0) {
			//check whether even or odd runthrough https://www.geeksforgeeks.org/3-ways-check-number-odd-even-without-using-modulus-operator-set-2-can-merge-httpswww-geeksforgeeks-orgcheck-whether-given-number-even-odd/
			if((isEvenRun & 1) == 0)
				activePlayer = player; //even
			else
				activePlayer = opponent; //odd
				
			
			List<Move> possible_moves = game.getValidMoves(current_rollout_state, activePlayer.getMyBoardMark());
			Move action = rollout_policy(possible_moves, current_rollout_state);
			current_rollout_state = game.do_move(current_rollout_state, action.getBoardPlace(), activePlayer);
			
			//switch player perspective for next rollout step
			isEvenRun++;
		}
		if(game.testing_end_game(current_rollout_state, 1)==1) { //boardmark 1 won
			return 1;
		} else if(game.testing_end_game(current_rollout_state, -1)==1) { //boardmark -1 won
			return -1;
		}
		assertEquals(2, game.testing_end_game(current_rollout_state, player.getMyBoardMark()));
		assertEquals(2, game.testing_end_game(current_rollout_state, opponent.getMyBoardMark()));
		return 2;
	}

	private Move rollout_policy(List<Move> possible_moves, int[][] board) {
		Random rand = new Random(); 
		if (possible_moves.size() > 0) {
			return possible_moves.get(rand.nextInt(possible_moves.size())); 
		} else {
			return new Move(board,new BoardSquare(-1, -1));
		}
	}

	/**
	 * @param simulation_result	1 if boardmark 1 won
	 * 							-1 if boardmark -1 won
	 * 							2 for draw
	 */
	public void backpropagate(int simulation_result) {
		number_of_visits++;		
		
		if(simulation_result ==1) { //increase wins
			Integer wins = results.get(1); 
			results.put(1, (wins == null) ? 1 : wins + 1); 
		} else if(simulation_result == -1) { //increase loss
			Integer loss = results.get(-1); 
			results.put(-1, (loss == null) ? 1 : loss + 1); 
		} else { //draw
			Integer draw = results.get(2); //get draws
			results.put(2, (draw == null) ? 1 : draw + 1); 
			
		}
		if(parent !=null )parent.backpropagate(simulation_result);
	}

	public MCTSNode move_with_most_simulations() {
		MCTSNode most_sim = children
			      .stream()
			      .max(Comparator.comparing(MCTSNode::getNumber_of_visits))
			      .orElseThrow(NoSuchElementException::new);
		return most_sim;
	}

	public void setNumber_of_visits(int number_of_visits) {
		this.number_of_visits = number_of_visits;
	}
	
	public int getNumber_of_visits() {
		return number_of_visits;
	}
	
	public MCTSNode expand() {
		Move action = untried_actions.get(0);
		untried_actions.remove(0);
		OthelloGame game = new OthelloGame();
		int[][] next_state = MCTSPlayer.copyArray(state);
		next_state = game.do_move(next_state, action.getBoardPlace(), player);
		MCTSNode child_node = new MCTSNode(action, next_state, opponent, this);
		children.add(child_node);

		return child_node;
	}
	
	public boolean is_terminal_node() {
		OthelloGame game = new OthelloGame();
		return (game.testing_end_game(state, player.getMyBoardMark()) != 0);
		
	}

	public boolean is_fully_expanded() {
		return untried_actions.size() == 0;
	}

	public MCTSNode best_child() {
		LinkedList<Double> choices_weights = new LinkedList<>();
		for(MCTSNode child : children) {
			double e =  ((double) child.wins_nominator() / (double) child.number_of_visits) + 1.4 * Math.sqrt(Math.log(number_of_visits) / (double) child.number_of_visits);
			choices_weights.add(e);
		}
		
		int index = 0;
        try { 
        	//find children with max choices weight index
        	Double max = choices_weights
    	    	      .stream()
    	    	      .mapToDouble(v -> v)
    	    	      .max().orElseThrow(NoSuchElementException::new);
        	index = choices_weights.indexOf(max);
        } 
        catch (Exception e) { 
        	e.printStackTrace();
        	System.out.println(Arrays.toString(choices_weights.toArray()));
        } 
		return children.get(index);
	}

	public Move getMove() {
		return move;
	}

}