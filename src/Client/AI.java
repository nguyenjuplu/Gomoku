/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.util.ArrayList;

/**
 *
 * @author John
 */
public abstract class AI extends PlayerController{
    
    int[][] board;
    int size;
    Object availableMoves;
    
    /**
     * Transmits the best move to the controller
     */
    public void makeMove(){
        int x, y;
        
        availableMoves = getAvailableMoves();
        
        int[] prediction = predict();

        x = prediction[0];
        y = prediction[1];
        
        model.makeMove(x, y, this);
        
        myTurn = false;
    }
    
    /**
     * Finds best move possible and returns that move
     * @return the move
     */
    private int[] predict(){
        return predictHelper();
    }
    

    public int getOpponentFlag(int playerFlag){        
        if(playerFlag == 1)
            return 2;
        return 1;        
    }
    
    @Override
    public void moveMade(int x, int y, int playerFlag) {
        board[x][y] = playerFlag;
        availableMoves = getAvailableMoves();
        if(playerFlag == getOpponentFlag(model.playerFlagFor(this)))
            makeMove();
    }

    @Override
    public void gameOver(int resultFlag) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    abstract int[] predictHelper();
    
    abstract Object getAvailableMoves();
    
}
