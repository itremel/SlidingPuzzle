package players;

import java.util.ArrayList;
import java.util.List;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

public class MiniMaxPlayer extends AbstractPlayer {
	
	public class Tree {
	    private Node root;

	    public Tree(int rootData, int player) {
	        root = new Node(null, rootData, player, null, new ArrayList<Node>());
	    }
	    
	    public void addNodes(List<Move> list, Node parent){
	    	int player = -parent.player;
	    	ArrayList<Node> nodes = new ArrayList<Node>();
	    	for(Move m:list){
	    		int score = getScore(player, m.getBoard());
	    		Node n = new Node(m, score, player, parent, new ArrayList<Node>());
	    		nodes.add(n);
	    	}
	    	parent.children.addAll(nodes);
	    }
	    
	    public Node createAndAddNode(Move move, Node parent){
	    	int player = -parent.player;
	    	int score = getScore(player, move.getBoard());
	    	Node n = new Node(move, score, player, parent, new ArrayList<Node>());
	    	parent.children.add(n);
	    	return n;
	    }

	    @Override
		public String toString() {
			return "Tree [root=" + root.nodeScore + "]" + "children=" + root.children;
		}

		public class Node {
			private Move move;
	        private int nodeScore;
	        private int player;
	        private Node parent;
	        private List<Node> children;
	        private int depth = 0;
	        
	        public Node(Move move, int data, int player, Node parent, List<Node> children){
	        	this.move = move;
	        	this.nodeScore = data;
	        	this.player = player;
	        	this.parent = parent;
	        	this.children = children;
	        	if(this.parent != null)this.depth = parent.depth+1;
	        }
	        
	        @Override
			public String toString() {
				return "Node:" + nodeScore + "children=" + children;
			}
	    }
	}


	public MiniMaxPlayer(int depth) {
		super(depth);
	}

	@Override
	public BoardSquare play(int[][] tab) {
		OthelloGame game = new OthelloGame();
		Tree tree = createTree(tab, game);
		Move move = minimax(tree);
		if(move != null) return move.getBoardPlace();
		else return new BoardSquare(-1, -1);
//		Random r = new Random();
//		List<Move> moves = game.getValidMoves(tab, getMyBoardMark());
//		if (moves.size() > 0) {
//			return moves.get(r.nextInt(moves.size())).getBoardPlace();
//		} else {
//			return new BoardSquare(-1, -1);
//		}
	}
	
	public Tree createTree(int[][] tab, OthelloGame game){
		Tree tree = new Tree(0, 1); //TODO check which player is root
		tree = buildTree(tree, tree.root, tab, game, 0);
		return tree;
	}
	
	public Tree buildTree(Tree tree, Tree.Node parentNode, int[][] tab, OthelloGame game, int depth){
		if(parentNode.depth < 7){
			List<Move> moves = game.getValidMoves(tab, getMyBoardMark());
			System.out.println(tree);
			for(Move m:moves){
				Tree.Node n = tree.createAndAddNode(m, parentNode);
				tree = buildTree(tree, n, m.getBoard(), game, depth);
			}
		}
		
		return tree;
	}
	
	public int getScore(int player, int[][] board){
		int score = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j] == player) score++;
			}
		}
		return score;
	}
	
	public Move minimax(Tree tree){
		Tree.Node curNode = tree.root;
		while(!curNode.children.isEmpty()){
			if(curNode.player == 1){
				int max = 0;
				for(Tree.Node child:curNode.children){
					if(child.nodeScore > max){
						max = child.nodeScore;
						curNode = child;
					}
				}
			}
			else{
				int min = Integer.MAX_VALUE;
				for(Tree.Node child:curNode.children){
					if(child.nodeScore < min){
						min = child.nodeScore;
						curNode = child;
					}
				}
			}
			
		}
		return curNode.move;
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

	