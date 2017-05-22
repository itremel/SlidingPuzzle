package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.PuzzleGame.action;

public class AStarSearchPlayer extends Player{

	@Override
	public List<action> solve(PuzzleGame game) {
		List<action> result = new ArrayList<>();
		
		Integer[][] currentBoard = game.getGameBoard();
		for (int i=0; i<20; ++i) {
			action[] allowedActions = game.getPossibleActions(currentBoard);
			int nextAction =(int)Math.ceil(Math.random()* allowedActions.length)-1;			
			action next = allowedActions[nextAction];
			currentBoard = game.computeAction(next, currentBoard);
			result.add(next);
		}
		/**

		class Node{
			Integer[][] board;
			int heuristic = 0;
			
			Node(Integer[][] board2, int heuristic2){
				board = board2;
				heuristic = heuristic2;
			}
		}
		Integer[][] currentBoard = game.getGameBoard();
		List<Integer[][]> open = new ArrayList<>();
		
		List<Integer[][]> closed = new ArrayList<>();
		open.add(currentBoard);
		while(!open.isEmpty()){
			
			//best value in open
			int bestvalue = 4000;//infinity
			int bestindex = 4000;
			for (int j = 0; j < open.size(); j++) {
				Integer[][] board = open.get(j);
				int heuristic = game.getHeuristicValue(board);
				if(heuristic < bestvalue) {
					bestvalue = heuristic;
					bestindex = j;
				}
			}
			currentBoard = open.get(bestindex);
			closed.add(currentBoard);
			
			//check final state
			if(game.isSolution(currentBoard)){
				break;
			}
			
			//expand neighbours
			List<Integer[][]> neighbours = new ArrayList<>();
			
			action[] allowedActions = game.getPossibleActions(currentBoard);
			for (int i = 0; i < allowedActions.length; i++) {
				action next = allowedActions[i];
				neighbours.add(game.computeAction(next, currentBoard));
			}
			
			//heuristic
			for (int i = 0; i < neighbours.size(); i++) {
				if(!Arrays.asList(closed).contains(neighbours.get(i))){//node not in closed
					open.add(neighbours.get(i));
				}
			}
		}
		**/
		
		return result;
	}
}
