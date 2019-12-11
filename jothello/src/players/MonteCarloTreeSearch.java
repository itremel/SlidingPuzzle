package players;

public class MonteCarloTreeSearch  {

	private MCTSNode root;
	
	public MonteCarloTreeSearch(MCTSNode node) {
		root = node;
		
	}

	/**
	 * @param simulation_steps number of simulations performed to get the best action
	 * @return MCTSNode containing the best Move
	 */
	public MCTSNode best_action(int simulation_steps) {
		for(int i = 0; i < simulation_steps; i++) {
			MCTSNode unvisited_node = selection();
			int simulation_result = unvisited_node.rollout();
			unvisited_node.backpropagate(simulation_result);
		}
		return root.move_with_most_simulations();
	}
	



	/**
	 * selects node to run rollout/playout for
	 * @return
	 */
	public MCTSNode selection() {
		MCTSNode current_node = root;
		while(!current_node.is_terminal_node()) {
			if(!current_node.is_fully_expanded()) {
				return current_node.expand();
			}else if(current_node.children.size()==0) {
				return current_node;
			} else {
				current_node = current_node.best_child();
			}
		}
		return current_node;
		
	}
}