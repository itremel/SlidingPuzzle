package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import model.PuzzleGame.action;

public class AStarSearchPlayer extends Player{

	class Node{
		Integer[][] board;
		int heuristic = 0;
		Node parent = null;
		action direction = null;
		
		Node(Integer[][] board2, int heuristic2, Node parent2, action dir){
			board = board2;
			heuristic = heuristic2;
			parent = parent2;
			direction = dir;
		}
		
		@Override
		public boolean equals(Object arg0) {
			// TODO Auto-generated method stub
			if (arg0 == this) return true;
			if(!(arg0 instanceof Node)){
				return false;
			}
			Node n = (Node) arg0;
			return java.util.Arrays.deepEquals(board,n.board);
		}
		
		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return Objects.hash(board);
		}
	}
	
	@Override
	public List<action> solve(PuzzleGame game) {
		List<action> result = new ArrayList<>();
		/**
		Integer[][] currentBoard = game.getGameBoard();
		for (int i=0; i<20; ++i) {
			action[] allowedActions = game.getPossibleActions(currentBoard);
			int nextAction =(int)Math.ceil(Math.random()* allowedActions.length)-1;			
			action next = allowedActions[nextAction];
			currentBoard = game.computeAction(next, currentBoard);
			result.add(next);
		}
		**/
		List<Node> open = new ArrayList<>();
		List<Node> closed = new ArrayList<>();
		
		Integer[][] currentBoard = game.getGameBoard();
		int heuristic = game.getHeuristicValue(currentBoard);
		Node root = new Node(currentBoard, heuristic, null, null);
		open.add(root);
		
		while(!open.isEmpty()){
			
			//best value in open
			order(open);
//			System.out.println("open: "+open.size());
			Node curNode = open.get(0);
			open.remove(0);
			closed.add(curNode);
			System.out.println("closed: "+closed.size());
			//check final state
			if(game.isSolution(curNode.board)){
				System.out.println("solution found");
				while(curNode!=null){
					result.add(curNode.direction);
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
		List<Node> neighbours = new ArrayList<>();
		
		action[] allowedActions = game.getPossibleActions(curNode.board);
		for (int i = 0; i < allowedActions.length; i++) {
			action next = allowedActions[i];
			Integer[][] b = game.computeAction(next, curNode.board);
			int h = game.getHeuristicValue(b);
			Node successor = new Node(b, h, curNode, next);
			if(!Arrays.asList(closed).contains(successor)){//node not in closed
				open.add(successor);
//				System.out.println("add to open");
			}else{
				System.out.println("contained in closed");
//				 Sonst: Aktualisiere gemerkten Elternknoten des Nachfolgers, wenn neuer Weg über n kostengünstiger ist als vorheriger.
//				if(successor.heuristic<closed.)
//				neighbours.get(i).parent
			}
		}
	}

	private static void order(List<Node> nodes){
		Collections.sort(nodes, new Comparator() {

			@Override
			public int compare(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				Integer heuristic1 = ((Node)arg0).heuristic;
				Integer heuristic2 = ((Node)arg1).heuristic;
				return heuristic1.compareTo(heuristic2);
			}
			
		});
	}
}
