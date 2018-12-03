/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * The NetworkPlayerThread manages communications over the network when it comes
 * to network games
 *
 * @author Nathann Hohnbaum
 */
public class NetworkPlayerThread extends Thread
{

    /**
     * The socket to communicate with the opponent
     */
    private Socket socket;

    /**
     * The input stream
     */
    private InputStream in;
    /**
     * The output stream
     */
    private OutputStream out;

    /**
     * Flag for move made
     */
    public static final int MOVE_FLAG = 1;

    /**
     * Flag for game over
     */
    public static final int WIN_FLAG = 2;

    /**
     * Flag for game is a draw
     */
    public static final int DRAW_FLAG = 3;
    
    /**
     * Flag for opp turn to start
     */
    public static final int OPP_START_FLAG = 4;
    /**
     * The parent network player to receive the messages
     */
    private NetworkPlayer networkPlayer;

    /**
     * True indicates that the game is over, so a disconnect is not a loss
     */
    private boolean gameOver;

    /**
     * Creates a network player thread
     *
     * @param s the socket
     * @param n the parent network player
     */
    public NetworkPlayerThread(Socket s, NetworkPlayer n)
    {
        socket = s;
        networkPlayer = n;
        
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Failed to make networkPlayer");
        }
    }

    @Override
    public void run()
    {   
       
      
        try {
            while (true) {
                int flag = in.read();

                
                switch (flag) {
                    case MOVE_FLAG:
                        int x = in.read();
                        int y = in.read();
                        networkPlayer.makeMove(x, y);
                        break;
                    case WIN_FLAG:
                        // Resignation
                        
                        if (in.read() == GameModel.RESULT_WIN) {
                            System.out.println("Game over in network player");
                            networkPlayer.receivedWin();
                        }
                        break;
                    case DRAW_FLAG:
                        networkPlayer.draw();
                        break;
                    case OPP_START_FLAG:
                        networkPlayer.setYourTurn();
                    default:
                    // TODO: Handle other (including unknown messages)
                }

            }
        } catch (IOException e) {
            if (!gameOver) {
                networkPlayer.unexpectedDisconnect();
            }
        } 
    }

    /**
     * Called to inform the thread that the game is now over.
     */
    public void endGame()
    {
        gameOver = true;
    }

    public void sendMove(int x, int y)
    {
        try {
            out.write(MOVE_FLAG);
            out.write(x);
            out.write(y);
        } catch (IOException e) {
            System.out.println("Couldn't send a move");
        }
    }

    /**
     * When a user has resigned on their client, the networkThread must send out
     * a short circuit that shows the opponent that they (user1) have resigned.
     *
     * This method is called by the NetworkPlayer class for when a user has
     * resigned.
     *
     * @param resultFlag
     */
    public void sendResult(int resultFlag)
    {
        try {
            out.write(WIN_FLAG);
            out.write(resultFlag);
        } catch (IOException e) {
            // TODO: Properly handle the exception
        }
    }
    
    public void sendYourTurn()
    {
        try{
            out.write(OPP_START_FLAG);
        } catch (IOException e){
            //TODO: Handle
        }
    }

}
