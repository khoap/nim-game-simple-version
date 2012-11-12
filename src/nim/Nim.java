/*
 * Simple version of NIM.
 * Program based on the winning strategy which is available on Wiki.
 */

package nim;
import java.util.*;
import java.io.*;

public class Nim {
	
	protected static final int INFINITY = 100;
	
	public Nim() {}
	
	public Node initializeNIM(int[] piles) {
		Node root = new Node(piles);
		root.heuristicValue = this.evaluateHeuristicValue(piles);
		return root;
	}
	
	public Node initializeNIMFromInput() {
		int[] NimPiles = this.getNimPiles();
		return this.initializeNIM(NimPiles);
	}
	
	public int[] getNimPiles() {
		int numberOfPiles;
		Scanner scanNumberOfPiles = new Scanner(System.in);
		Scanner scanPiecesInEachPile = new Scanner(System.in);
		
		System.out.print("Input the number of piles: ");
		numberOfPiles = scanNumberOfPiles.nextInt();
		
		int[] NimPiles = new int[numberOfPiles];
		for(int i = 0; i < numberOfPiles; i++) {
			System.out.print("Input the value for pile " + i + " : ");
			NimPiles[i] = scanPiecesInEachPile.nextInt();
		}
		System.out.print("Your Nim you entered: "); this.printPiles(NimPiles);
		return NimPiles;
	}
	
	// Get a successor from a current node; return null if there is no more successor.
	public Node getSuccessor(Node currentNode,int removeAtPile, int NumberOfPiecesRemoved) {
		if(this.isLegalMove(currentNode, removeAtPile, NumberOfPiecesRemoved) == false) return null;
		int[] newNimPiles = new int[currentNode.piles.length];
		System.arraycopy(currentNode.piles, 0, newNimPiles, 0, newNimPiles.length);
		newNimPiles[removeAtPile] = newNimPiles[removeAtPile] - NumberOfPiecesRemoved;
		int heuristicValue = this.evaluateHeuristicValue(newNimPiles);
		int atDepth = currentNode.atDepth + 1;		
		return new Node(newNimPiles,currentNode,heuristicValue,atDepth);
	}
	
	// Get all successors from a current Node; return a vector of node.
	public Vector<Node> getAllSuccessorsFromNode(Node currentNode) {
		Vector<Node> vectorNode = new Vector<Node>();	
			for(int i = 0; i < currentNode.piles.length; i ++) {
				int NumberOfPiecesRemoved = 0;
				while(currentNode.piles[i] >= 0) {
					NumberOfPiecesRemoved++;
					Node successor = getSuccessor(currentNode,i,1);
					if(successor != null) vectorNode.addElement(successor);
					currentNode.piles[i]--;
				}
				currentNode.piles[i] = currentNode.piles[i] + NumberOfPiecesRemoved++;
			}
			return vectorNode;	
	}
	
	public int evaluateHeuristicValue(int[] NimPiles) {
		int xor = 0;
		for(int i = 0; i < NimPiles.length; i ++) {xor ^= NimPiles[i];}
		if(xor == 0) return 1;	// Win 
		else return -1;	//Lose 
	}
	
	public boolean isLegalMove(Node currentNode, int removeAtPile, int NumberOfPiecesRemoved) {
		if(NumberOfPiecesRemoved <= 0 || NumberOfPiecesRemoved > currentNode.piles[removeAtPile] 
				|| this.isLeafNode(currentNode.piles) == true)
			return false;
		return true;	
	}
	public boolean isLeafNode(int[] NimPiles) {
		for(int i = 0; i < NimPiles.length; i++) { if(NimPiles[i] != 0) return false; }
		return true;	
	}
	
	/*
	 * Game playing
	 */
	
	public void gamePlay() {
		Node root = this.initializeNIMFromInput();
		int player = this.getPlayer();
		if(player == 1) this.humanMove(root);
		else this.machineMove(root);
	}
	public void humanMove(Node currentNode) {
		Node newNode = new Node();
		int[] readPiecesToRemove = this.getCorrectInputFromHumanMove(currentNode);
		int removeAtPile = readPiecesToRemove[0];
		int numberOfPiecesRemoved = readPiecesToRemove[1];	
		newNode = this.initializeNIM(this.updateNimPiles(currentNode.piles, removeAtPile, numberOfPiecesRemoved));
		this.printPiles(newNode.piles);
		if(this.isLeafNode(newNode.piles)==true) System.out.println("Congratulations. You won!");
		else this.machineMove(newNode);
	}
	public void machineMove(Node currentNode) {
		Node newNode = this.getNextNodeMachineMove(currentNode);
		this.printPiles(newNode.piles);
		if(this.isLeafNode(newNode.piles) == true) System.out.println("Computer won!");
		else this.humanMove(newNode);
	}
	/*
	 * Extra-method
	 */
	public void printPiles(int[] NimPiles) {
		System.out.print("Current Piles: ");
		for(int i = 0; i < NimPiles.length; i++) System.out.print(NimPiles[i] + " ");
		System.out.println();
	}
	public int[] updateNimPiles(int[] NimPiles, int removeAtPile, int NumberOfPiecesRemoved) {
		NimPiles[removeAtPile] = NimPiles[removeAtPile]-NumberOfPiecesRemoved;
		return NimPiles;
	}
	public Node getNextNodeMachineMove(Node currentNode) {
		return this.getMaxInVector(this.getAllSuccessorsFromNode(currentNode));
	}
	public Node getMaxInVector(Vector<Node> aVector) {
		for(int i = 0; i<aVector.size();i++) if(aVector.get(i).heuristicValue == 1) return aVector.get(i);
		return aVector.firstElement();
	}
	public int[] readPiecesToRemove() {
		int[] piecesToRemove = new int[2];
		Scanner pile = new Scanner(System.in); Scanner pieces = new Scanner(System.in);
		System.out.print("Enter which pile you want to remove: "); 
		piecesToRemove[0] = pile.nextInt();
		System.out.print("Please enter number of pieces in pile that you want to remove: "); 
		piecesToRemove[1] = pieces.nextInt();
		return piecesToRemove;
	}
	public int[] getCorrectInputFromHumanMove(Node currentNode) {
		int[] readPiecesToRemove = this.readPiecesToRemove();
		while(this.isLegalMove(currentNode, readPiecesToRemove[0], readPiecesToRemove[1])!=true) {
			System.out.println("Sorry. Your move is not correct.");
			readPiecesToRemove = this.readPiecesToRemove();
		}
		return readPiecesToRemove;
	}
	public int getPlayer(){
		Scanner player = new Scanner(System.in);
		System.out.print("Do you want to play first? Yes (1). No (0): ");
		return player.nextInt();
	}
	/*
	 * Main function
	 */
	public static void main(String args[]) throws IOException {
		Nim NimGame = new Nim();
		NimGame.gamePlay();
	}
}	
