import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;


/*
 * Solves the 8-Puzzle Game (can be generalized to n-Puzzle)
 */

public class EightsPlayer {

	static Scanner scan = new Scanner(System.in);
	static int size=3; //size=3 for 8-Puzzle
	static int numiterations = 100;
	static int numnodes; //number of nodes generated
	static int nummoves; //number of moves required to reach goal


	public static void main(String[] args)
	{
		int boardchoice = getBoardChoice();
		int algchoice = getAlgChoice();

		int numsolutions = 0;

		Node initNode;

		if(boardchoice==0)
			numiterations = 1;

		for(int i=0; i<numiterations; i++){

			if(boardchoice==0)
				initNode = getUserBoard();
			else
				initNode = generateInitialState();//create the random board for a new puzzle

			boolean result=false; //whether the algorithm returns a solution

			switch (algchoice){
				case 0:
					result = runBFS(initNode); //BFS
					break;
				case 1:
					result = runAStar(initNode, 0); //A* with Manhattan Distance heuristic
					break;
				case 2:
					result = runAStar(initNode, 1); //A* with your new heuristic
					break;
			}


			//if the search returns a solution
			if(result){

				numsolutions++;


				System.out.println("Number of nodes generated to solve: " + numnodes);
				System.out.println("Number of moves to solve: " + nummoves);
				System.out.println("Number of solutions so far: " + numsolutions);
				System.out.println("_______");

			}
			else
				System.out.print(".");

		}//for



		System.out.println();
		System.out.println("Number of iterations: " +numiterations);

		if(numsolutions > 0){
			System.out.println("Average number of moves for "+numsolutions+" solutions: "+nummoves/numsolutions);
			System.out.println("Average number of nodes generated for "+numsolutions+" solutions: "+numnodes/numsolutions);
		}
		else
			System.out.println("No solutions in "+numiterations+" iterations.");

	}


	public static int getBoardChoice()
	{

		System.out.println("single(0) or multiple boards(1)");
		int choice = Integer.parseInt(scan.nextLine());

		return choice;
	}

	public static int getAlgChoice()
	{

		System.out.println("BFS(0) or A* Manhattan Distance(1) or A* <Your New Heuristic>(2)");
		int choice = Integer.parseInt(scan.nextLine());

		return choice;
	}


	public static Node getUserBoard()
	{

		System.out.println("Enter board: ex. 012345678");
		String stbd = scan.nextLine();

		int[][] board = new int[size][size];

		int k=0;

		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				//System.out.println(stbd.charAt(k));
				board[i][j]= Integer.parseInt(stbd.substring(k, k+1));
				k++;
			}
		}


		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				//System.out.println(board[i][j]);
			}
			System.out.println();
		}


		Node newNode = new Node(null,0, board);

		return newNode;


	}




	/**
	 * Generates a new Node with the initial board
	 */
	public static Node generateInitialState()
	{
		int[][] board = getNewBoard();

		Node newNode = new Node(null,0, board);

		return newNode;
	}


	/**
	 * Creates a randomly filled board with numbers from 0 to 8.
	 * The '0' represents the empty tile.
	 */
	public static int[][] getNewBoard()
	{

		int[][] brd = new int[size][size];
		Random gen = new Random();
		int[] generated = new int[size*size];
		for(int i=0; i<generated.length; i++)
			generated[i] = -1;

		int count = 0;

		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				int num = gen.nextInt(size*size);

				while(contains(generated, num)){
					num = gen.nextInt(size*size);
				}

				generated[count] = num;
				count++;
				brd[i][j] = num;
			}
		}

		/*
		//Case 1: 12 moves
		brd[0][0] = 1;
		brd[0][1] = 3;
		brd[0][2] = 8;

		brd[1][0] = 7;
		brd[1][1] = 4;
		brd[1][2] = 2;

		brd[2][0] = 0;
		brd[2][1] = 6;
		brd[2][2] = 5;
		*/

		return brd;

	}

	/**
	 * Helper method for getNewBoard()
	 */
	public static boolean contains(int[] array, int x)
	{
		int i=0;
		while(i < array.length){
			if(array[i]==x)
				return true;
			i++;
		}
		return false;
	}


	/**
	 * TO DO:
     * Prints out all the steps of the puzzle solution and sets the number of moves used to solve this board.
     */
    public static void printSolution(Node node) {

    	/*TO DO*/
			ArrayList<Node> path = new ArrayList<Node>();

			while(node.getparent() != null){
				path.add(node);
				node = node.getparent();
			}
			//add the root node that does not have a parent
			path.add(node);

			//prints boards to show solving progression
			for(int i = path.size()-1; i >= 0; i--){
				path.get(i).print(path.get(i));
			}

			nummoves = path.size();

    }




	/**
	 * TO DO:
	 * Runs Breadth First Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 */
	public static boolean runBFS(Node initNode)
	{
		Queue<Node> Frontier = new LinkedList<Node>();
		ArrayList<Node> Explored = new ArrayList<Node>();

		Frontier.add(initNode);
		numnodes++;
		int maxDepth = 13;

//		initNode.print(initNode);

		/*TO DO*/
		//while frontier is not empty

		while(Frontier.peek() != null){
			//remove node to explore
			Node current = Frontier.remove();
			//move that node to Explored
			Explored.add(current);
			//if we have found the goal, return it
			if(current.isGoal()){
				printSolution(current);
				return true;
			}else if(current.getdepth() > maxDepth){
				System.out.println("Exceeded max depth");
				return false;
			}else{
				// save all of the potential board states into array
				ArrayList<int[][]> boardOptions = current.expand();
				// get depth
				int depth = current.getdepth() + 1;
				//iterate through array and make nodes of all potential states
				Boolean addNode = true;
				for(int i = 0; i < boardOptions.size(); i++){
					int[][] temp = boardOptions.get(i);
					//if potential state has already been explored
					for(Node exploredNode : Explored){
						if(Arrays.deepEquals(exploredNode.getboard(), temp)){
							//don't add state to Frontier
							addNode = false;
						}
					}
					//if state has not been explored
					if(addNode == true){
					//add node to Frontier (because it is not in explored)
						Node option = new Node(current, depth, temp);
						Frontier.add(option);
						numnodes++;
					}
					addNode=true;
				}
			}
		}
	return false;
	}//BFS



	/***************************A* Code Starts Here ***************************/

	/**
	 * TO DO:
	 * Runs A* Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 * heuristic = 0 for Manhattan Distance, heuristic = 1 for your new heuristic
	 */
	public static boolean runAStar(Node initNode, int heuristic)
	{
		
		 PriorityQueue<Node> Frontier = new PriorityQueue<Node>(1, new Comparator<Node>(){
			 
			 public int compare (Node n1, Node n2){
				if(n1.getgvalue() + n1.gethvalue() <= n2.getgvalue() + n2.gethvalue()){
					return 1;
				}
				else if(n1.getgvalue() + n1.gethvalue() == n2.getgvalue() + n2.gethvalue()){
					return 0;
				}
				else{
					return -1;
				}
			 }
		 });
			 
		Queue<Node> Explored = new LinkedList<Node>();
		initNode.setgvalue(0);
		initNode.sethvalue(initNode.getManhattanDistanceSum( ));
		Frontier.add(initNode);
		
		
		numnodes++;
		int maxDepth = 13;

		while(!Frontier.isEmpty()){
			Node current = Frontier.poll();
			Explored.add(current);
			
			if(current.isGoal()){
				printSolution(current);
				return true;
			}else if(current.getdepth() > maxDepth){
				System.out.println("Exceeded max depth");
				return false;
			}else{
				// save all of the potential board states into array
				ArrayList<int[][]> boardOptions = current.expand();
				// get depth
				int depth = current.getdepth() + 1;
				//iterate through array and make nodes of all potential states
				Boolean addNode = true;
				
				
				for(int i = 0; i < boardOptions.size(); i++){
					int[][] temp = boardOptions.get(i);
					//if potential state has already been explored
					for(Node exploredNode : Explored){
						if(Arrays.deepEquals(exploredNode.getboard(), temp)){
							//don't add state to Frontier
							addNode = false;
							System.out.println("explored");
						}
					}
					//if state has not been explored
					if(addNode == true){
					//add node to Frontier (because it is not in explored)
						Node option = new Node(current, depth, temp);
						option.setgvalue(current.getgvalue()+1);
						option.sethvalue(option.getManhattanDistanceSum()); //double check 
						Frontier.add(option);
						numnodes++;
					}
					addNode=true;
				}
			}
		}
	return false;
	}

}
