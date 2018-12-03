package Client;

import static common.ServerMessaging.*;
import java.net.Socket;

/**
 *
 * @author JimmyTN
 * 
 * EDIT: 4.12.18 Jeremy Pallwitz:
 * Added if statement for invite method to check
 * for received invites from model.
 * EDIT: 4.13.18 Jimmy Nguyen & John Smith:
 * Fixed the implementation of invite to use static Strings and
 * implemented addGameTab.
 * EDIT: 4.13.18
 * Changed if statements to switch, added cases for 
 * rejected, canceled invites. 
 */
public class LobbyController
{

    /**
     * The lobby model
     * The model is responsible for all server communication and 
     * data management.
     */
    private LobbyModel lModel;
    
    /**
     * The lobby view used for connection with the Interface. The user 
     * interacts with this view.
     */
    private LobbyView lView;
    
    /**
     * The master controller
     * The controller that holds the most important data and calls for
     * all the set up methods. This is necessary for the Lobby to be created
     * and communicate with the Master for when the user wants to exit.
     */
    private MasterController master;

    /**
     * The constructor for the lobby class that is passed a socket sock from the
     * MasterController
     * Creates a new LobbyModel with controller as this and passes the 
     * socket from MasterController. 
     * Creates a new LobbyView with controller as this for interaction.
     *
     * @param s the socket from MasterController that connects to the Server
     * @param mc the master controller
     */
    public LobbyController(MasterController mc, Socket s)
    {
        lModel = new LobbyModel(s, this);
        lView = new LobbyView(this);
        master = mc;
        mc.setContentPane(lView);
        mc.pack();
        lModel.start();
    }

    /**
     * Passes on the invite command to the LobbyModel for interaction with
     * the Server.
     * 
     * This method is called from many different sources. It will pass on
     * command = "Send" if lView's sendInvite was triggered
     *         = "Accept" if lView's accept was triggered
     *         = "Reject" if lView's reject was triggered
     *         = "Cancel" if lView's cancelInvite was triggered
     *         = "Received" if lModel's receiveInvite was triggered
     * @param command The command to be sent to the LobbyModel
     * @param username The username to interact with
     */
    public void invite(String command, String username){
        switch(command) {
            case OUT_RECEIVED_INVITE:
                lView.receiveInvite(username);
                break;
            case OUT_REJECTED_INVITE:
                //TODO: Handle a rejected invite to the user
                //that sent the invite.
                lView.removeSentInvite(username);
                break;
            case OUT_CANCELED_INVITE:
                //TODO:Handle a cancelled invite to the user
                //that received the invite.
                lView.removeRecvInvite(username);
                break;
            case OUT_ACCEPTED_INVITE:
                //TODO:Handle an accepted invite to the user
                //that received the invite.
                //lView.accept(username, inv); <---- Inv is type recvInvite and can't be handled
                lView.removeSentInvite(username);
                break;
            default:
                lModel.invite(command, username);
                System.out.println("Controller: invite called, command: " + command);
                break;
        }
    }
    
    /**
     * Updates the active players list and displays it in the LobbyView
     * @param players a list of all players in the lobby
     */
    public void updatePlayerList(String[] players)
    {
        System.out.println("Updating player list");
        lView.displayActivePlayers(players);
    }

    /**
     * This is for further implementation.
     * It will be called from the LobbyModel once the connection is set.
     *      This can be either the hostSocket has found a connection or
     *      the peerSocket has found a connection.
     * Creates a new tab in the LobbyView for the game play.
     */
    public void addGameTab(PlayerController pController){
        System.out.println("LobControl's addGame was called");
        lView.toGame((GameController) pController);
    }
    
    /**
     * Returns the view
     * @return the view
     */
    public LobbyView getView()
    {
        return lView;
    }
    
    public void startOfflineGame(String difficulty)
    {
        lModel.startOfflineGame(30, difficulty);
    }
    
    public void loggedOut()
    {
        master.setUpAuth();
    }
    
    public void logout()
    {
        lModel.logout();
    }
}
