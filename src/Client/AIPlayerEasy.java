/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.util.HashSet;

/**
 *
 * @author John
 */
public class AIPlayerEasy extends AI{
    
    public AIPlayerEasy(int size){
        board = new int[size][size];
        this.size = size;
    }
    
    
    
     /**
     * Returns an ArrayList containing all, non-taken moves
     * @return 
     */
    HashSet<int[]> getAvailableMoves(){
        HashSet<int[]> empty = new HashSet<int[]>();
        
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j] != 0){
                    addSurrounding(i, j, empty);
                }
            }
        }
        
        return empty;
    }

    @Override
    int[] predictHelper() {
        int[] move = null;
        for(int[] temp : (HashSet<int[]>)availableMoves){
            move = temp;
            break;
        }
        return move;
    }

    private void addSurrounding(int row, int col, HashSet<int[]> empty) {
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if((row + i < size && row + i > 0) && (col + j < size && col + j > 0)){
                    if (board[row + i][col + j] == 0) {
                        empty.add(new int[]{row + i, col + j,0});
                    }
                }
            }
        }
    }
    
}
