package SlidingPuzzle;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import sac.StateFunction;
import sac.graph.AStar;
import sac.graph.GraphSearchAlgorithm;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;
 

public class SlidingPuzzle extends GraphStateImpl {
	

	private static Random random = new Random(123);

	public static  int n=3;
	byte [][] board = null;
	byte emptyIndexX=0;
	byte emptyIndexY=0;
	
	public static final int UP = 0;
	public static final int RIGHT=1;
	public static final int DOWN=2;
	public static final int LEFT=3;
	private static final String [] MOVE = {"U", "R", "D", "L"};
	
 
	public SlidingPuzzle() {
		board = new byte [n][n];
		int k = 0;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
			board [i][j] = (byte) (k++);
	}
	
	
 
	public SlidingPuzzle(SlidingPuzzle parent) {
		board = new byte [n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
			board [i][j] = parent.board[i][j];
		emptyIndexX = parent.emptyIndexX;
		emptyIndexY = parent.emptyIndexY;
	}
 
 
 
 
	public byte[][] getBoard() {
		return board;
	}
 
	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				temp.append(board[i][j]);
				temp.append((j<n-1)?",":"\n");
			}
		return temp.toString();
 
	}
 
	public boolean move (int move) {
		switch(move) {
			case UP:
				if  (emptyIndexX > 0) {
					board[emptyIndexX][emptyIndexY] = board [emptyIndexX -1][emptyIndexY];
					board[emptyIndexX -1][emptyIndexY] = 0;
					emptyIndexX--;
					return true;
				}; break;
			case DOWN:
				if  (emptyIndexX < n - 1) {
					board[emptyIndexX][emptyIndexY] = board [emptyIndexX + 1][emptyIndexY];
					board[emptyIndexX + 1][emptyIndexY] = 0;
					emptyIndexX++;
					return true;
			}; break;
			case LEFT:
				if  (emptyIndexY > 0) {
					board[emptyIndexX][emptyIndexY] = board [emptyIndexX][emptyIndexY -1];
					board[emptyIndexX][emptyIndexY - 1] = 0;
					emptyIndexY--;
					return true;
				}; break;
			case RIGHT:
				if  (emptyIndexY < n - 1) {
					board[emptyIndexX][emptyIndexY] = board [emptyIndexX][emptyIndexY + 1];
					board[emptyIndexX][emptyIndexY + 1] = 0;
					emptyIndexY++;	
					return true;
				}; break;
		}
		return false;
	}
 
	public void shuffle (int Moves) {
		int i = 0;
		while (i<Moves) 
			if (move(random.nextInt(4))) {
				i++;
			}
		}
 
 
	@Override
	public List<GraphState> generateChildren() {
		List<GraphState> children = new ArrayList<GraphState>();
		for (int i = 0;i<4;i++) {
			SlidingPuzzle child = new SlidingPuzzle(this);
			if (child.move(i)) {
				child.setMoveName(MOVE[i]);
				children.add(child);
			}
		}
		return children;
	}
 
 
	@Override
	public boolean isSolution() {
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] != k++) {
					return false;	
				}
			}			
		}	
		return true;
	}
 
 
 
 
	@Override
	public int hashCode() {
 
		return toString().hashCode();
	}
	
	//used for misplaced tiles
	public int[] getCoordinates(int misplacedtile) {
		 
		 
 		int[] currposition = new int[2];

 		for (int y = 0; y < n; y++) {
 			for (int x = 0; x < n; x++) {
 				if (board[x][y] == misplacedtile) {
 					currposition[0] = x;
 					currposition[1] = y;

 					break;
 				}
 			}
 		}
 		return currposition;
 	}
	
	//used for Manhattan 
	public int[] getCoordinatesGoal(int tile) {
		 
		 
 		int[] position = new int[2];
 		
 		byte [][] goalstate = null;
		goalstate = new byte [n][n];
		int k = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
			goalstate [i][j] = (byte) (k++);
			}
		}

 		for (int y = 0; y < n; y++) {
 			for (int x = 0; x < n; x++) {
 				if (goalstate[x][y] == tile) {
 					position[0] = x;
 					position[1] = y;

 					break;
 				}
 			}
 		}
 		return position;
 	}
 
 
 
	public static void main(String[] args) {
		SlidingPuzzle sp= new SlidingPuzzle();
		Scanner s=new Scanner(System.in);  
		System.out.println("Enter 1 - Run Program without heuristics averages \nEnter 2 - Run Program with heuristics averages \nEnter 3 - Run Program with heuristics averages with 4 by 4 board");
		int choice = s.nextInt();
		
		switch(choice) {
		case 1:
			
			int num = 1;
			
			sp.shuffle(1000);
			System.out.println("Sliding Puzzle to be solved: \n" + sp);
	 
			StateFunction[] heuristics = { new HFunctionMisplacedTiles(), new HFunctionManhattan() };
			for (StateFunction h : heuristics)
			{
				SlidingPuzzle.setHFunction(h);
				GraphSearchAlgorithm algorithm = new AStar(sp);
				algorithm.execute();
				SlidingPuzzle solution = (SlidingPuzzle) algorithm.getSolutions().get(0);
				
				
				if (num == 1)
				{
					System.out.println("SOLUTION: \n" + solution);
					System.out.println("Name of heuristic: HFunctionMisplacedTiles");
				}
	 
				else if(num==2)
				{
					System.out.println("Name of heuristic: HFunctionManhattan");
				}
				System.out.println("PATH LENGTH:  " + solution.getPath().size());
				System.out.println("MOVES ALONG PATH:  " + solution.getMovesAlongPath());
				System.out.println("CLOSED STATES:  " + algorithm.getClosedStatesCount());
				System.out.println("OPEN STATES: " + algorithm.getOpenSet().size());
				System.out.println("DURATION TIME: " + algorithm.getDurationTime() + "ms \n");
				num++;
	 
		}
		break;
		case 2:
			
			sp.shuffle(1000);
			
			System.out.println("Sliding Puzzle to be solved: \n" + sp);
			
			double[] MTPathLength = new double[100];
			double[] MTClosedStates= new double[100];
			double[] MTOpenStates = new double[100];
			double[] MTtimes = new double[100];
			
			double[] MPathLength = new double[100];
			double[] MClosedStates= new double[100];
			double[] MOpenStates = new double[100];
			double[] Mtimes = new double[100];
			
			double MTPathLengthTot=0;
			double MTClosedStatesTot=0;
			double MTOpenStatesTot=0;
			double MTtimesTot=0;
			
			double MPathLengthTot=0;
			double MClosedStatesTot=0;
			double MOpenStatesTot=0;
			double MtimesTot=0;
			
			
			
			
				int num1 = 1;
				int num2 =1;
				
				
				if (num1 == 1)
				{
					StateFunction[] heuristics1 = { new HFunctionMisplacedTiles(), new HFunctionManhattan()};
					for (StateFunction h : heuristics1)
					{
						random.setSeed(1);
						System.out.println("Puzzle size = 3 by 3 ");
						System.out.println("HFunctionMisplacedTiles Average Performance after 100 runs ");
						for(int i=0;i<100;i++){
						sp = new SlidingPuzzle();
						sp.shuffle(1000);
						System.out.println("puzzle " + i + ": \n" + sp);
						SlidingPuzzle.setHFunction(h);
						GraphSearchAlgorithm algorithm = new AStar(sp);
						algorithm.execute();
						SlidingPuzzle solution = (SlidingPuzzle) algorithm.getSolutions().get(0);
						
						
						 MTPathLength[i] =  solution.getPath().size();
						 MTClosedStates[i] = algorithm.getClosedStatesCount();
						 MTOpenStates[i] =  algorithm.getOpenSet().size();
						 MTtimes[i] = algorithm.getDurationTime();
						 
						 
						}
					
						MTPathLengthTot = 0;
						MTClosedStatesTot = 0;
						MTOpenStatesTot= 0;
						MTtimesTot = 0;
						
					for(int i=0;i<100;i++){
						
						MTPathLengthTot = (MTPathLength[i]+MTPathLengthTot);
						MTClosedStatesTot = (MTClosedStates[i] + MTClosedStatesTot);
						MTOpenStatesTot= (MTOpenStates[i]+MTOpenStatesTot);
						MTtimesTot = (MTtimes[i] + MTtimesTot);

						}
					
					System.out.println("PATH LENGTH:  " + (MTPathLengthTot/100.0));
					System.out.println("CLOSED STATES:  " + (MTClosedStatesTot/100.0));
					System.out.println("OPEN STATES: " + (MTOpenStatesTot/100.0));
					System.out.println("DURATION TIME: " + (MTtimesTot/100.0) + "ms \n");
					
				
					
				}
					
				}
				
				
				if(num2==100)
				{
					
					StateFunction[] heuristics2 = { new HFunctionManhattan() };
					for (StateFunction h : heuristics2)
					{
						System.out.println("HFunctionManhattan Average Performance after 100 runs ");
						for(int i=0;i<100;i++){
						sp.shuffle(1000);
						SlidingPuzzle.setHFunction(h);
						GraphSearchAlgorithm algorithm = new AStar(sp);
						algorithm.execute();
						SlidingPuzzle solution = (SlidingPuzzle) algorithm.getSolutions().get(0);
						
						
						
					
						
							
							MPathLength[i] =  solution.getPath().size();
							MClosedStates[i] = algorithm.getClosedStatesCount();
							MOpenStates[i] =  algorithm.getOpenSet().size();
							Mtimes[i] = algorithm.getDurationTime();
							
							}
						
						
					for(int i=0;i<100;i++){
					MPathLengthTot = (MPathLength[i]+MPathLengthTot);
					MClosedStatesTot = (MClosedStates[i] + MClosedStatesTot);
					MOpenStatesTot= (MOpenStates[i]+MOpenStatesTot);
					MtimesTot = (Mtimes[i] + MtimesTot);
					
					}

					

				
				System.out.println("PATH LENGTH:  " + (MPathLengthTot/100));
				System.out.println("CLOSED STATES:  " + (MClosedStatesTot/100));
				System.out.println("OPEN STATES: " + (MOpenStatesTot/100));
				System.out.println("DURATION TIME: " + (MtimesTot/100) + "ms \n"); 

					
				}
								
				
				}
				break;
		case 3:
			
			
			System.out.println("Puzzle size = 4 by 4 ");
			System.out.println("Sliding Puzzle to be solved: \n" + sp);
			sp.shuffle(100);
			double[] MTPathLength1 = new double[10];
			double[] MTClosedStates1= new double[10];
			double[] MTOpenStates1 = new double[10];
			double[] MTtimes1 = new double[10];
			
			double[] MPathLength1 = new double[10];
			double[] MClosedStates1= new double[10];
			double[] MOpenStates1 = new double[10];
			double[] Mtimes1 = new double[10];
			
			double MTPathLengthTot1=0;
			double MTClosedStatesTot1=0;
			double MTOpenStatesTot1=0;
			double MTtimesTot1=0;
			
			double MPathLengthTot1=0;
			double MClosedStatesTot1=0;
			double MOpenStatesTot1=0;
			double MtimesTot1=0;
			
			
			
			
				int numb1 = 1;
				int numb2 =1;
				
				
				if (numb1 == 1)
				{
					StateFunction[] heuristics1 = { new HFunctionMisplacedTiles() };
					for (StateFunction h : heuristics1)
					{
						SlidingPuzzle.setHFunction(h);
						GraphSearchAlgorithm algorithm = new AStar(sp);
						algorithm.execute();
						SlidingPuzzle solution = (SlidingPuzzle) algorithm.getSolutions().get(0);
						
						
					
					System.out.println("HFunctionMisplacedTiles Average Performance after 10 runs ");
					
					for(int i=0;i<10;i++){
						 MTPathLength1[i] =  solution.getPath().size();
						 MTClosedStates1[i] = algorithm.getClosedStatesCount();
						 MTOpenStates1[i] =  algorithm.getOpenSet().size();
						 MTtimes1[i] = algorithm.getDurationTime();
						 
						}
					
					
					for(int i=0;i<10;i++){
						MTPathLengthTot1 = (MTPathLength1[i]+MTPathLengthTot1);
						MTClosedStatesTot1 = (MTClosedStates1[i] + MTClosedStatesTot1);
						MTOpenStatesTot1= (MTOpenStates1[i]+MTOpenStatesTot1);
						MTtimesTot1 = (MTtimes1[i] + MTtimesTot1);
						
						
						}
					
					System.out.println("PATH LENGTH:  " + (MTPathLengthTot1/10));
					System.out.println("CLOSED STATES:  " + (MTClosedStatesTot1/10));
					System.out.println("OPEN STATES: " + (MTOpenStatesTot1/10));
					System.out.println("DURATION TIME: " + (MTtimesTot1/10) + "ms \n");
					
				
					
				}
					
				}
				
				
				if(numb2==1)
				{
					
					StateFunction[] heuristics2 = { new HFunctionManhattan() };
					for (StateFunction h : heuristics2)
					{
						SlidingPuzzle.setHFunction(h);
						GraphSearchAlgorithm algorithm = new AStar(sp);
						algorithm.execute();
						SlidingPuzzle solution = (SlidingPuzzle) algorithm.getSolutions().get(0);
						
						
						System.out.println("HFunctionManhattan Average Performance after 10 runs ");
					
						for(int i=0;i<10;i++){
							MPathLength1[i] =  solution.getPath().size();
							MClosedStates1[i] = algorithm.getClosedStatesCount();
							MOpenStates1[i] =  algorithm.getOpenSet().size();
							Mtimes1[i] = algorithm.getDurationTime();
							}
						
					for(int i=0;i<10;i++){
					MPathLengthTot1 = (MPathLength1[i]+MPathLengthTot1);
					MClosedStatesTot1 = (MClosedStates1[i] + MClosedStatesTot1);
					MOpenStatesTot1= (MOpenStates1[i]+MOpenStatesTot1);
					MtimesTot1 = (Mtimes1[i] + MtimesTot1);
					}

					
				System.out.println("PATH LENGTH:  " + (MPathLengthTot1/10));
				System.out.println("CLOSED STATES:  " + (MClosedStatesTot1/10));
				System.out.println("OPEN STATES: " + (MOpenStatesTot1/10));
				System.out.println("DURATION TIME: " + (MtimesTot1/10) + "ms \n"); 

					
				}
								
				
				}
			
			break;
			default:
				System.out.println("Invalid Input");
			}
					
	
}
}
	
