package model;

import java.util.ArrayList;
import java.util.List;

import model.PuzzleGame.action;

public class RandomPlayer extends Player {

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
		
		return result;
	}

}
