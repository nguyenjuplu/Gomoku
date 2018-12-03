package Client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeremy
 * Edit: 5.9.2018 - Added sendDraw
 */
public interface WinSender {
    
    /**
     * Allows the winner to tell the server the
     * game result.
     **/
    public void sendWin(String opp);
    /**
     * Allows a player to tell the server a draw
     * has occurred
     */
    public void sendDraw(String opp);
}
