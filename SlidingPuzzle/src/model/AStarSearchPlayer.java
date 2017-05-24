package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import model.PuzzleGame.action;

public class AStarSearchPlayer extends Player{

	class Node{
		Integer[][] board;
		int steps = 0;
		int heuristic = 0;
		Node parent = null;
		action previousDirection = null;
		
		Node(Integer[][] board, int steps, int heuristic, Node parent, action dir){
			this.board = board;
			this.steps = steps;
			this.heuristic = heuristic;
			this.parent = parent;
			this.previousDirection = dir;
		}
		
		@Override
		public boolean equals(Object arg0) {
			if (arg0 == this) return true;
			if(!(arg0 instanceof Node)){
				return false;
			}
			Node n = (Node) arg0;
			return java.util.Arrays.deepEquals(board,n.board);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash((Object)board);
		}
	}
	
	@Override
	public List<action> solve(PuzzleGame game) {
		List<action> result = new ArrayList<>();
		List<Node> open = new ArrayList<>();
		List<Node> closed = new ArrayList<>();
		
		Integer[][] currentBoard = game.getGameBoard();
		int heuristic = game.getHeuristicValue(currentBoard);
		Node root = new Node(currentBoard, 0, heuristic, null, null);
		open.add(root);
		
		while(!open.isEmpty()){
			
			//best value in open
			order(open);
			Node curNode = open.get(0);
			open.remove(0);
			closed.add(curNode);
			//check final state
			if(game.isSolution(curNode.board)){
				
				System.out.println("Solution with " + curNode.steps + " steps found");
				
				while(curNode.parent!=null){
					result.add(curNode.previousDirection);
					curNode = curNode.parent;
				}
				Collections.reverse(result);
				return result;
			}
			
			//expand neighbours
			expand(game, curNode, open, closed);
			
		}
		
		
		return result;
	}
	
	private void expand(PuzzleGame game ,Node curNode, List<Node> open, List<Node> closed) {
		//expand successors
		
		action[] allowedActions = game.getPossibleActions(curNode.board);
		for (int i = 0; i < allowedActions.length; i++) {
			action next = allowedActions[i];
			Integer[][] b = game.computeAction(next, curNode.board);
			int h = game.getHeuristicValue(b);
			Node successor = new Node(b, curNode.steps + 1, h, curNode, next);
			
			if(!closed.contains(successor)){
				//node not in closed
				open.add(successor);
			}
			
		}
	}

	private static void order(List<Node> nodes){
		Collections.sort(nodes, new Comparator<Node>() {

			@Override
			public int compare(Node arg0, Node arg1) {
				Integer value1 = arg0.heuristic + arg0.steps;
				Integer value2 = arg1.heuristic + arg1.steps;
				return value1.compareTo(value2);
			}
			
		});
	}
}
