package SlidingPuzzle;

import sac.State;
import sac.StateFunction;

 public class HFunctionMisplacedTiles extends StateFunction {

	 @Override
	public double calculate(State State) {
		
		SlidingPuzzle slidingPuzzle = ( SlidingPuzzle ) State;
		double h = 0.0;
		int k = 0;
//		int n=slidingPuzzle .board.length;
//		byte [][] goalstate = null;
//		goalstate = new byte [n][n];
//		
//		
//		for (int i = 0; i < n; i++) {
//			for (int j = 0; j < n; j++) {
//			goalstate [i][j] = (byte) (k++);
//			}
//		}
//		
//		for (int i = 0; i < slidingPuzzle .board.length; i++) {
//			for (int j = 0; j < slidingPuzzle .board.length; j++) {
//				if (slidingPuzzle .board[i][j] != goalstate[i][j]) {
//					h= h+1.0;
//				}
//			}
//		}
//		return h;
		
		
		for (int i = 0; i < slidingPuzzle .board.length; i++) {
			for (int j = 0; j < slidingPuzzle .board.length; j++,k++) {
				if (slidingPuzzle .board[i][j] != 0 && slidingPuzzle .board[i][j] != k ) {
					h= h+1.0;
				}
			}
		}
		return h;
	}
}