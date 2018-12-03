package Client;

import common.ServerMessaging;
import static common.ServerMessaging.IN_GUEST_REQUEST;
import static common.ServerMessaging.IN_LOGIN_REQUEST;
import static common.ServerMessaging.IN_REGISTER_REQUEST;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

/**
 *
 * @author JimmyTN
 * EDIT: 4.15.18: Jeremy Pallwitz
 * Used string constants for model login operation
 */
public class AuthController implements ActionListener{
    AuthView authView;
    AuthModel authModel;
    MasterController mFrame;
    
    /**
     * Controller for the AuthController class
     * Takes in a socket and not sure what it does with it
     * @param m
     * @param sock 
     */
    public AuthController(MasterController m, Socket sock)
    {
        mFrame = m;
        authView = new AuthView(this);
        authModel = new AuthModel(sock, this);
    }
    
   
    /**
     * Listener for when an action is performed on the AuthView
     * If login, extract log in information
     * If register, switch to new register view
     * If guest, extract log in and send nothing
     * @param action The action that dictates what to do next
     */
    @Override
    public void actionPerformed(ActionEvent action){
        //Get Username and Password 
        String user = authView.getUser();
        String pass = authView.getPass();
        
        //3 Options
        if(action.getSource() == authView.getLoginButton() ||
                action.getSource() == authView.getUserField() ||
                action.getSource() == authView.getPass1Field() ){
            
            //If LogIn: Calls sendLogInfo and passes user and Pass
            System.out.println("Logging in");
            sendLogInfo(user, pass);
        }
        
        else if((action.getSource() == authView.getRegButton()) ||
                action.getSource() == authView.getPass2Field() && (authView.isRegFlag() == true)){
            //If Register: Calls sendReg, grabs pass2 by using getPass2 and
            //passes user, pass, and pass2    
            String pass2 = authView.getPass2();
            sendReg(user, pass, pass2);            
        }
            
        else if(action.getSource() == authView.getOfflineButton() ){
            //If Guest: Calls sendGuest and passes nothing
            guest();
        }
    }
    
    /**
     * Server has failed to identify the login or register
     * Method is called from AuthModel and asks the user to check the info.
     * Can have many cases but the String will be displayed
     * @param failMsg 
     */
    public void failure(String failMsg){
        //Prompt the user with the failMsg
        switch (failMsg) {
            case ServerMessaging.ERROR_BAD_LOGIN:
                authView.setErrorLabel("Incorrect username or password");
                break;
            case ServerMessaging.ERROR_DUPLICATE_LOGIN:
                authView.setErrorLabel("Already logged in elsewhere");
                break;
            case ServerMessaging.ERROR_ILLEGAL_CHAR:
                authView.setErrorLabel("Spaces and commas are not allowed in usernames or passwords");
                break;
            case ServerMessaging.ERROR_BAD_USERNAME:
            case ServerMessaging.ERROR_USERNAME_TAKEN:
                authView.setErrorLabel("Username is unavailable");
                break;
            case ServerMessaging.ERROR_CONNECTION:
                authView.setErrorLabel("Connection lost");
                break;
            default:
                authView.setErrorLabel("Unknown error");
        }
    }
    
    /**
     * Method that is called when either login or guest is picked.
     * The method then calls AuthModel to perform the login or guest
     * request with the server
     * @param user
     * @param pass 
     */
    public void sendLogInfo(String user, String pass){
        //Call authModel to login
        authModel.operation(IN_LOGIN_REQUEST, user, pass);  
    }

    /**
     * Method that is called when user hits register button.
     * The method then calls AuthModel to perform a register with the
     * server class
     * @param user
     * @param pass
     * @param conPass 
     */
    public void sendReg(String user, String pass, String conPass){
        //Call authModel to register the account
        if(pass.equals(conPass)){
            authModel.operation(IN_REGISTER_REQUEST, user, pass);
        } else{
            authView.setErrorLabel("Passwords do not match");
        }
        
    }
    
    /**
     * Method that is called when user hits play as guest.
     * The method will ask the AuthModel to send a guest account to the 
     * server and receive a guest account username from the server.
     */
    public void guest(){
        //Call authModel to login but with blank user and pass
        authModel.operation(IN_GUEST_REQUEST, "", "");
    }
    
    /**
     * Sends a message to the MasterController to set up the new Lobby,
     * since the Authentication of an account was successful
     * @param username the username of the account logged into
     */
    public void sendAuthSuccess(String username){
        mFrame.setUpLobby(username);
    }
}
