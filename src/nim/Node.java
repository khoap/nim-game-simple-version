package nim;

public class Node {
	int[] piles;
	Node parentNode;
	int heuristicValue;
	int atDepth;
	String player;

	public Node() {}
	public Node(int[] piles, Node parentNode, int heuristicValue, int atDepth) {
		this.piles = piles;
		this.parentNode = parentNode;
		this.heuristicValue = heuristicValue;
		this.atDepth = atDepth;
	}
	public Node(int[] piles) {
		this(piles,null,0,0);
	}
}
