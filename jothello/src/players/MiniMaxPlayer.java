package players;

import java.util.List;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

public class MiniMaxPlayer extends AbstractPlayer {
	
	public static final int maxDepth = 6;
	
	/*public class Tree {
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
*/

	public MiniMaxPlayer(int depth) {
		super(depth);
	}

	@Override
	public BoardSquare play(int[][] tab) {
		OthelloGame game = new OthelloGame();
		//System.out.println("1111111111111111111111111111111111");
		//Tree tree = createTree(tab, game);
		//System.out.println("2222222222222222222222222222222");
		//minimax(tab, game , this.getMyBoardMark());
		valueMax(tab, this.getMyBoardMark(), maxDepth, game);
		//System.out.println(" 4444444444444444444444444444444");
		if(bestMove != null && game.validate_moviment(tab, bestMove.getBoardPlace(), this) == 0) return bestMove.getBoardPlace();
		else {
			System.out.println("no move found");
			printArray(bestMove.getBoard());
			System.out.println("\n" + bestMove.getBoardPlace().toString());
			return new BoardSquare(-1, -1);
		}
//		Random r = new Random();
//		List<Move> moves = game.getValidMoves(tab, getMyBoardMark());
//		if (moves.size() > 0) {
//			return moves.get(r.nextInt(moves.size())).getBoardPlace();
//		} else {
//			return new BoardSquare(-1, -1);
//		}
	}
	
	/*public Tree createTree(int[][] tab, OthelloGame game){
		Tree tree = new Tree(0, 1);
		tree = buildTree(tree, tree.root, tab, game, 0);
		return tree;
	}
	
	public Tree buildTree(Tree tree, Tree.Node parentNode, int[][] tab, OthelloGame game, int depth){
		if(parentNode.depth < 7){
			List<Move> moves = game.getValidMoves(tab, getMyBoardMark());
			System.out.println(tree);
			for(Move m:moves){
				Tree.Node n = tree.createAndAddNode(m, parentNode);
				tree = buildTree(tree, n, game.do_move(m.getBoard(), m.getBoardPlace(), this), game, depth);
			}
		}
		
		return tree;
	}
	*/
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
	/*
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
	*/
	public int[][] copyArray(int[][] a){
		int [][] myInt = new int[a.length][];
		for(int i = 0; i < a.length; i++)
		    myInt[i] = a[i].clone();
		return myInt;
	
	}
	
	private Move bestMove = null;
	
	public int minimax(int[][] board, OthelloGame game, int player) {
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
			/*
			System.out.println("\noriginal: ");
			printArray(board);
			System.out.println("\nnew: ");
			printArray(subBoard);
			*/
			int value = valueMin(subBoard, this.getOpponentBoardMark(), depth - 1, game);
			subBoard = copyArray(board);
			if (value > best) {
				best = value;
				//if(depth == maxDepth) bestMove = move;
			}
		}
		return best;
	}
	
	public void printArray(int[][] a){
		for (int i = 0; i < a.length; i++){
			for (int j = 0; j < a[i].length; j++){
				System.out.print(a[i][j]);
			}
			System.out.print("\n");
		}
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

	
