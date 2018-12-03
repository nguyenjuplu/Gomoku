package Client;

import common.ServerMessaging;
import static common.ServerMessaging.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.util.Random;

/**
 *
 * @author Nathann Hohnbaum
 * @author Jeremy Pallwitz EDIT: 4.13.18 Added functionality for handling
 * rejected, accepted, and canceled invites. EDIT: 4.15.18 Fixed game play port
 * for now, added logout method
 * EDIT: 5.9.18 Added sendDraw, edited methods to add opponent string to pass to 
 * GameModel so wins can be properly sent.
 */
public class LobbyModel extends Thread implements WinSender
{

    /**
     * The socket
     */
    private Socket sock;
    /**
     * The model's controller
     */
    private LobbyController cont;

    /**
     * The reader for incoming traffic
     */
    private BufferedReader in;
    /**
     * The reader for outgoing traffic
     */
    private OutputStream out;
    /**
     * ServerSocket and gameSocket
     */
    private ServerSocket servsock;
    private Socket gameSock;
    /**
     * Socket port for game play connectivity.
     */
    private int port;
    /**
     * True if the model is meant to be running
     */
    private boolean isRunning;

    public LobbyModel(Socket s, LobbyController cont)
    {
        sock = s;
        this.cont = cont;
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = sock.getOutputStream();
            isRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    

    /**
     * Creates and sends an invite command to the Server based on the string
     * "command" If Command == "Send" --> Send an invite to "username" ==
     * "Accept" --> Accept an invite from "username" == "Reject" --> Decline an
     * invite from "username" == "Cancel" --> Cancel the invite to "username"
     *
     * @param command The action that is to be taken
     * @param username The username to interact with
     */
    public void invite(String command, String username)
    {
        try {
            switch (command) {
                case IN_SEND_INVITE:
                    System.out.println("Sending to server");
                    out.write((IN_SEND_INVITE + ", " + username + "\n").getBytes());
                    out.flush();
                    break;
                case IN_ACCEPT_INVITE:
                    System.out.println("Model: IN_ACCEPT_INVITE");
                    out.write((IN_ACCEPT_INVITE + ", " + username + "\n").getBytes());
                    out.flush();
                    hostServerSock(username);
                    break;
                case IN_REJECT_INVITE:
                    out.write((IN_REJECT_INVITE + ", " + username + "\n").getBytes());
                    out.flush();
                    break;
                case IN_CANCEL_INVITE:
                    out.write((IN_CANCEL_INVITE + ", " + username + "\n").getBytes());
                    out.flush();
                    break;
                default:
                    out.write((command + ", " + username + "\n").getBytes());
                    out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends received invites to controller
     *
     * @param username
     */
    public void receiveInvite(String username)
    {
        cont.invite(OUT_RECEIVED_INVITE, username);
    }

    @Override
    public void run()
    {
        try {
            while (isRunning) {
                String message = in.readLine();
                String[] pieces = message.split(", ");
                if (pieces.length == 0) {
                    continue;   // Empty message
                }
                switch (pieces[0]) {
                    case ServerMessaging.OUT_PLAYERS:
                        String[] players = new String[pieces.length - 1];
                        for (int i = 0; i < players.length; i++) {
                            players[i] = pieces[i + 1];
                        }
                        cont.updatePlayerList(players);
                        out.write((ServerMessaging.IN_PLAYERS_RECEIVED+"\n").getBytes());
                        break;
                    case ServerMessaging.OUT_FLUSH:
                        break;
                    case OUT_RECEIVED_INVITE:
                        System.out.println("Model: Invite Received");
                        receiveInvite(pieces[1]);
                        break;
                    case OUT_ACCEPTED_INVITE:
                        System.out.println("Model: Accepted Invite from: " + pieces[1]);
                        inviteAccAndConnect(pieces[2], pieces[1]);
                        cont.invite(OUT_ACCEPTED_INVITE, pieces[1]);
                        break;
                    case OUT_REJECTED_INVITE:
                        System.out.println("Model: Rejected Invite from: " + pieces[1]);
                        cont.invite(OUT_REJECTED_INVITE, pieces[1]);
                        break;
                    case OUT_CANCELED_INVITE:
                        System.out.println("Model: Cancelled Invite from: " + pieces[1]);
                        cont.invite(OUT_CANCELED_INVITE, pieces[1]);
                        break;
                    case ServerMessaging.OUT_LOGOUT_SUCCESS:
                        close();
                        isRunning = false;
                }
            }
            cont.loggedOut();
        } catch (IOException e) {

        }
    }

    /**
     * Sends logout request to server, confirms logout for Controller.
     */
    public void logout()
    {
        try {
            out.write((ServerMessaging.IN_LOGOUT_REQUEST + "\n").getBytes());
        } catch (IOException ex) {
            Logger.getLogger(LobbyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        cont.loggedOut();
    }

    /**
     * Creates a ServerSocket to setup game play for accepted invite.
     *
     */
    private void hostServerSock(String opp)
    {
        try {
            servsock = new ServerSocket(GAMEPLAY_PORT);
            gameSock = servsock.accept();
            Random rand = new Random();
            int size = rand.nextInt(31) + 20;
            int first = rand.nextInt(2);
            // Size sent first followed by who goes first
            gameSock.getOutputStream().write(size);
            switch (first) {
                case 0:
                    // We go first, they go second
                    gameSock.getOutputStream().write(GameModel.PLAYER_2_FLAG);
                    startNetworkedGame(gameSock, GameModel.PLAYER_1_FLAG, size, opp);
                    break;
                case 1:
                    // We go second, they go first
                    gameSock.getOutputStream().write(GameModel.PLAYER_1_FLAG);
                    startNetworkedGame(gameSock, GameModel.PLAYER_2_FLAG, size, opp);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Handle the exception properly
        }
    }

    /**
     * Connects to a host and begins a game
     *
     * @param IPAddress the ip address of the host
     */
    private void inviteAccAndConnect(String IPAddress, String opp)
    {
        try {
            gameSock = new Socket(IPAddress, GAMEPLAY_PORT);
            int size = gameSock.getInputStream().read();
            int player = gameSock.getInputStream().read();
            startNetworkedGame(gameSock, player, size, opp);
        } catch (IOException e) {
            System.out.println("Could not connect to game" + e);
            // TODO: Properly handle the exception
        }

    }

    /**
     * Starts a network game
     *
     * @param s the socket connecting to the opponent
     * @param myPlayer a flag to indicate whether the local player is player 1
     * or player 2
     */
    private void startNetworkedGame(Socket s, int myPlayer, int boardSize, String o)
    {
        GameController gCont = new GameController(boardSize);
        NetworkPlayer opp = new NetworkPlayer(s);
        GameModel m = null;
        switch (myPlayer) {
            case GameModel.PLAYER_1_FLAG:
                m = new GameModel(boardSize, gCont, opp, this, o);
                break;
            case GameModel.PLAYER_2_FLAG:
                m = new GameModel(boardSize, opp, gCont, this, o);
        }
       
        gCont.setModel(m);
        opp.setModel(m);
        cont.addGameTab(gCont);
    }
    
    public void startOfflineGame(int boardSize, String difficulty){
        GameController gCont = new GameController(boardSize);
        AI opp;
        if(difficulty.equals("Hard"))
            opp = new AIPlayerHard(30);
        else if(difficulty.equals("Medium"))
            opp = new AIPlayerMedium(30);
        else
            opp = new AIPlayerEasy(30);
        
        
        GameModel mod = new GameModel(boardSize, gCont, opp, this, "AI: "+ difficulty);
        
        gCont.setModel(mod);
        opp.setModel(mod);
        cont.addGameTab(gCont);
    }



    @Override
    public void sendWin(String opp)
    {
        try {
            out.write((IN_GAME_WIN + ", " + opp + "\n").getBytes());
            System.out.println("Win has been sent.");
        } catch (IOException ex) {
            System.out.println("Couldn't Send a Win");
        } 
    }
    
    @Override
    public void sendDraw(String opp){
        try{
            out.write((IN_GAME_DRAW + ", " + opp + "\n").getBytes());
        } catch (IOException ex) {
            System.out.println("Failed to send draw.");
        }
    }
    
    public void close(){
        if(servsock != null){
        try{
            servsock.close();
            System.out.println("Server Socket closed");
    }   catch (IOException ex) {
            System.out.println("Couldn't Close Socket");
        } 
        }
        if(gameSock != null){
        try {
            gameSock.close();
            System.out.println("Closed GameSocket");
        } catch (IOException ex) {
           System.out.println("Couldn't Close GameSocket");
        }
        }
    }
}
