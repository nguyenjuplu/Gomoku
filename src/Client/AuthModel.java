package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.InputStreamReader;

import common.ServerMessaging;
import static common.ServerMessaging.IN_GUEST_REQUEST;
import static common.ServerMessaging.IN_LOGIN_REQUEST;
import static common.ServerMessaging.IN_REGISTER_REQUEST;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeremy
 */
public class AuthModel
{

    Socket sock;        //Socket from MasterController
    InputStream in;     //InputStream and OutputStream to 
    OutputStream out;   //send and receive from Server
    AuthController aC;  //Controller to communicate to authView
    BufferedReader buf; //Used to read data sent by Server

    /**
     * AuthModel takes in socket from MasterController and instantiates
     * variables.
     *
     * @param s
     * @param aC
     */
    AuthModel(Socket s, AuthController aC)
    {
        //Instatiate Variables
        sock = s;

        this.aC = aC;
        try {
            in = sock.getInputStream();

            out = sock.getOutputStream();
            buf = new BufferedReader(new InputStreamReader(in));
        } catch (IOException e) {
            e.printStackTrace();
            sendFailure("Failed to connect to server.");
        }

    }

    /**
     * Method that takes in command of either 'login', 'register', or 'guest' as
     * well as username and password to send server for Authentication. Message
     * received back is then processed to determine if login was successful and
     * tell that to the controller.
     *
     * @param command
     * @param user
     * @param pass
     */
    public void operation(String command, String user, String pass)
    {
        byte[] b;//byte array to send server
        String msg = ""; //message received from server
        String line; //line to read from BufferedReader buf
        String s = command + ", "
                + user + ", " + pass + "\n";  //String with username and password

        b = s.getBytes();

        try { //Send login info to Server
            out.write(b);
        } catch (IOException e) { //Could not send data to server
            sendFailure("Connection lost");
        }

        try { //Read back from server
            msg = buf.readLine();
            System.out.println(msg);
            String[] pieces = msg.split(", ");
            //Check for Authentification from Server
            switch (pieces[0]) {
                case ServerMessaging.OUT_LOGIN_SUCCESS:
                    sendSuccess(pieces[1]);
                    break;
                case ServerMessaging.OUT_REGISTER_FAILURE:
                case ServerMessaging.OUT_LOGIN_FAILURE:
                    sendFailure(pieces[1]);
            }
        } catch (IOException e) {//Could not receive data to server
            sendFailure(ServerMessaging.ERROR_CONNECTION);
        }
    }

    /**
     * Sends failure message to controller if login/register was unsuccessful.
     *
     * @param error
     */
    private void sendFailure(String error)
    {
        aC.failure(error);
    }

    /**
     * Sends success message to controller if login is successful.
     *
     * @param username the username of the account logged into
     */
    private void sendSuccess(String username)
    {

        aC.sendAuthSuccess(username);

    }
}
