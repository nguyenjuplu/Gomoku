package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Jeremy
 */
public class NetworkPlayer extends PlayerController
{


    /**
     * True if the game is over, so disconnects are expected
     */
    private boolean gameOverFlag;
    
    /**
     * The thread to handle messages from this object
     */
    private NetworkPlayerThread oppThread;
    

    
    public NetworkPlayer(Socket sock)
    {
        oppThread=new NetworkPlayerThread(sock,this);
        
        
    }
    
    /**
     * This method takes a parameter x and y and sends to the model to make
     * a move. I don't think this one is ever called.
     * 
     * @param x
     * @param y 
     */
    public void makeMove(int x, int y)
    {
        model.makeMove(x, y, this);
        myTurn = false;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param playerFlag 
     */
    @Override
    public void moveMade(int x, int y, int playerFlag)
    {
        oppThread.sendMove(x, y);
    }

    /**
     * Called by GameModel when the game is over to close the socket and send 
     * the end-game result
     *
     * @param resultFlag
     */
    @Override
    public void gameOver(int resultFlag)
    {
        oppThread.endGame();
        oppThread.sendResult(resultFlag);
    }
    
    /**
     * Called when the player disconnects unexpectedly
     */
    public void unexpectedDisconnect()
    {
        model.resign(this);
    }
    
    /**
     * Helper method
     * 
     * Called when the other user has resigned and this NetworkPlayer class
     * must now notify the GameModel to tell the PlayerController to tell the 
     * GameView that the game is over
     */
    public void receivedWin()
    {
        model.resign(this);
    }

    /**
     * Update GameView to indicate turn
     */
    @Override
    public void yourTurn()
    {
        oppThread.sendYourTurn();
        myTurn = true;
    }
    
    public void setYourTurn(){
        model.setTurn(this);
    }
 
    @Override
    public void setModel(GameModel m) {
        model = m;
        
        //I think this fixes our occasional move not sending.
        oppThread.start();
    }
    
    public void draw() {
        model.setDraw();
    }
}
