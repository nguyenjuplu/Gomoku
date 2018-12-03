/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.ServerMessaging;

/**
 * Each connected user has a ClientThread that processes messages from the the
 * client and sends messages back to the client
 *
 * @author Nathann Hohnbaum
 * @date 17 March 2018
 */
public class ClientThread extends Thread implements LogSource
{

    // Object fields -----------------------------------------------------------
    /**
     * The stream for incoming messages
     */
    private final BufferedReader in;
    /**
     * The stream for outgoing messages
     */
    private final PrintWriter out;

    /**
     * The socket for communication with the client
     */
    private final Socket socket;

    /**
     * The server object that listens for messages from the clients
     */
    private final Server server;

    /**
     * The logging object for this ClientThread
     */
    private final LogManager logger;

    /**
     * Informs the client thread that the server has just started sending the
     * players. This value ensures that updates sent close together make it
     * across.
     */
    private boolean startedSendingPlayers;

    /**
     * Notifies the server that the player list has been received.
     */
    private boolean sendingPlayers;

    /**
     * The account that this user is currently logged into
     */
    private Account account;

    /**
     * How long to wait before resending the player list if an attempt fails
     */
    public static final long SENDING_WAIT_TIME = 100;
    
    /**
     * Creates a ClientThread with a given socket and server
     *
     * @param socket the socket for the connection
     * @param server the server to listen to messages
     * @param logger the object to manage this ClientThread's logs
     * @throws IOException if the Thread is unable to connect to the client
     */
    public ClientThread(Socket socket, Server server, LogManager logger) throws IOException
    {
        this.socket = socket;
        this.server = server;
        this.logger = logger;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        account = null;
        sendingPlayers = false;
        startedSendingPlayers = false;
    }

    /**
     * Sets the account logged into by this ClienThread
     *
     * @param account the account logged into by this ClientThread
     */
    public void setAccount(Account account)
    {
        this.account = account;
    }

    /**
     * Returns the account logged into by this ClientThread
     *
     * @return the account logged into by this ClientThread
     */
    public Account getAccount()
    {
        return account;
    }

    /**
     * Sends the client a list of online players
     *
     * @param names the names of the online players
     */
    public synchronized void sendPlayerList(Collection<String> names)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ServerMessaging.OUT_PLAYERS);
        for (String s : names) {
            sb.append(", ");
            sb.append(s);
        }
        sendingPlayers = true;
        startedSendingPlayers=true;
        do {
            out.println(sb.toString());
            startedSendingPlayers=false;
            try {
                Thread.sleep(SENDING_WAIT_TIME);
            } catch (InterruptedException e) {
                logger.log(new LogEvent (this, "Interrupted sending player list"));
            }
        } while (sendingPlayers);
    }

    @Override
    public void run()
    {
        if (logger != null) {
            logger.log(new LogEvent(this, "Client connected."));
        }
        try {
            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                String[] args = message.split(", ");
                if (args.length == 0) {
                    out.println(ServerMessaging.ERROR_UNKNOWN_MESSAGE);
                }
                switch (args[0]) {
                    case ServerMessaging.IN_LOGIN_REQUEST:
                        if (args.length < 3 || message.endsWith(", ")) {
                            // Either username or password contains commas or
                            // login request is improperly formatted
                            if (message.endsWith(", ")) {
                                out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + ServerMessaging.ERROR_ILLEGAL_CHAR);
                            } else {

                                out.println(ServerMessaging.OUT_LOGIN_FAILURE
                                        + ", " + ServerMessaging.ERROR_MESSAGE_FORMAT);
                            }
                        } else if (args.length > 3) {
                            out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + ServerMessaging.ERROR_ILLEGAL_CHAR);
                        } else if (account != null) {
                            out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + ServerMessaging.ERROR_ALREADY_LOGGED_IN);
                        } else {
                            try {
                                // Authenticate
                                server.login(this, args[1], args[2]);
                                if (account != null) {
                                    out.println(ServerMessaging.OUT_LOGIN_SUCCESS + ", " + account.getUsername());
                                    logger.log(new LogEvent(this, "Logged in as " + account.getUsername() + "."));
                                    server.refreshPlayers();
                                } else {
                                    out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + ServerMessaging.ERROR_UNKNOWN);
                                }
                            } catch (AuthenticateException e) {
                                logger.log(new LogEvent(this, "Login failure logigng into " + args[1] + "."));
                                out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + e.getMessage());
                            }
                        }
                        break;
                    case ServerMessaging.IN_REGISTER_REQUEST:
                        if (args.length < 3 || message.endsWith(", ")) {
                            // Either username or password contains commas or
                            // login request is improperly formatted
                            if (message.endsWith(", ")) {
                                out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + ServerMessaging.ERROR_ILLEGAL_CHAR);
                            } else {
                                out.println(ServerMessaging.OUT_LOGIN_FAILURE
                                        + ", " + ServerMessaging.ERROR_MESSAGE_FORMAT);
                            }
                        } else if (args.length > 3) {
                            out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + ServerMessaging.ERROR_ILLEGAL_CHAR);
                        } else if (account != null) {
                            out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + ServerMessaging.ERROR_ALREADY_LOGGED_IN);
                        } else {
                            try {
                                // Register
                                server.register(this, args[1], args[2]);
                                if (account != null) {
                                    out.println(ServerMessaging.OUT_REGISTER_SUCCESS + ", " + account.getUsername());
                                    logger.log(new LogEvent(this, "Registered as " + account.getUsername() + "."));
                                    server.refreshPlayers();
                                } else {
                                    out.println(ServerMessaging.OUT_REGISTER_FAILURE + ", " + ServerMessaging.ERROR_UNKNOWN);
                                }
                            } catch (AuthenticateException e) {
                                logger.log(new LogEvent(this, "Failure to register as " + args[1] + "."));
                                out.println(ServerMessaging.OUT_REGISTER_FAILURE + ", " + e.getMessage());
                            }
                        }
                        break;
                    case ServerMessaging.IN_GUEST_REQUEST:
                        if (args.length != 1) {
                            // Bad message format
                            out.println(ServerMessaging.ERROR_MESSAGE_FORMAT);
                        } else if (account != null) {
                            out.println(ServerMessaging.OUT_LOGIN_FAILURE + ", " + ServerMessaging.ERROR_ALREADY_LOGGED_IN);
                        } else {
                            server.playAsGuest(this);
                            if (account != null) {
                                if (account != null) {
                                    out.println(ServerMessaging.OUT_LOGIN_SUCCESS + ", " + account.getUsername());
                                    logger.log(new LogEvent(this, "Logged in as guest: " + account.getUsername() + "."));
                                    server.refreshPlayers();
                                } else {
                                    logger.log(new LogEvent(this, "Error playing as guest."));
                                    out.println(ServerMessaging.OUT_REGISTER_FAILURE + ", " + ServerMessaging.ERROR_UNKNOWN);
                                }
                            }
                        }
                        break;
                    case ServerMessaging.IN_LOGOUT_REQUEST:
                        if (account != null) {
                            server.logout(this);
                            out.println(ServerMessaging.OUT_LOGOUT_SUCCESS);
                            logger.log(new LogEvent(this, "Logged out."));
                            server.refreshPlayers();
                        } else {
                            out.println(ServerMessaging.OUT_ERROR + ", " + ServerMessaging.ERROR_LOGIN);
                        }
                        break;
                    case ServerMessaging.IN_FLUSH:
                        out.println(ServerMessaging.OUT_FLUSH);
                        break;
                    case ServerMessaging.IN_SEND_INVITE:
                        System.out.println("Message Received");
                        if (args.length != 2) {
                            out.println(ServerMessaging.OUT_ERROR + ", " + ServerMessaging.ERROR_MESSAGE_FORMAT);
                        } else if (account == null) {
                            out.println(ServerMessaging.OUT_INVITE_ERROR + ", " + ServerMessaging.ERROR_LOGIN);
                        } else {
                            try {
                                server.sendInvite(account.getUsername(), args[1]);
                                logger.log(new LogEvent(this, "Invite sent to " + args[1] + "."));
                            } catch (InviteException e) {
                                logger.log(new LogEvent(this, "Unable to invite " + args[1] + "."));
                                out.println(ServerMessaging.OUT_INVITE_ERROR + ", " + ServerMessaging.ERROR_SENDING_INVITE + ", " + args[1]);
                            }
                        }
                        break;
                    case ServerMessaging.IN_CANCEL_INVITE:
                        if (args.length != 2) {
                            out.println(ServerMessaging.OUT_ERROR + ", " + ServerMessaging.ERROR_MESSAGE_FORMAT);
                        } else if (account == null) {
                            out.println(ServerMessaging.OUT_INVITE_ERROR + ", " + ServerMessaging.ERROR_LOGIN);
                        } else {
                            try {
                                server.cancelInvite(account.getUsername(), args[1]);
                                logger.log(new LogEvent(this, "Invite to " + args[1] + " cancelled."));
                            } catch (InviteException e) {
                                logger.log(new LogEvent(this, "Unable to cancel invite to " + args[1] + "."));
                                out.println(ServerMessaging.OUT_INVITE_ERROR + ", " + ServerMessaging.ERROR_CANCELLING_INVITE + ", " + args[1]);
                            }
                        }
                        break;
                    case ServerMessaging.IN_ACCEPT_INVITE:
                        if (args.length != 2) {
                            out.println(ServerMessaging.OUT_ERROR + ", " + ServerMessaging.ERROR_MESSAGE_FORMAT);
                        } else if (account == null) {
                            out.println(ServerMessaging.OUT_INVITE_ERROR + ", " + ServerMessaging.ERROR_LOGIN);
                        } else {
                            try {
                                server.acceptInvite(args[1], account.getUsername());
                                logger.log(new LogEvent(this, "Invite from " + args[1] + " accepted."));
                            } catch (InviteException e) {
                                logger.log(new LogEvent(this, "Unable to accept invite from " + args[1] + "."));
                                out.println(ServerMessaging.OUT_INVITE_ERROR + ", " + ServerMessaging.ERROR_ACCEPTING_INVITE + ", " + args[1]);
                            }
                        }
                        break;
                    case ServerMessaging.IN_REJECT_INVITE:
                        if (args.length != 2) {
                            out.println(ServerMessaging.OUT_ERROR + ", " + ServerMessaging.ERROR_MESSAGE_FORMAT);
                        } else if (account == null) {
                            out.println(ServerMessaging.OUT_INVITE_ERROR + ", " + ServerMessaging.ERROR_LOGIN);
                        } else {
                            try {
                                server.rejectInvite(args[1], account.getUsername());
                                logger.log(new LogEvent(this, "Invite from " + args[1] + " rejected."));
                            } catch (InviteException e) {
                                logger.log(new LogEvent(this, "Unable to reject invite from " + args[1] + "."));
                                out.println(ServerMessaging.OUT_INVITE_ERROR + ", " + ServerMessaging.ERROR_REJECTING_INVITE + ", " + args[1]);
                            }
                        }
                        break;
                    case ServerMessaging.IN_GAME_WIN:
                        if (args.length != 2) {
                            out.println(ServerMessaging.OUT_ERROR + "," + ServerMessaging.ERROR_MESSAGE_FORMAT);
                        } else if (account == null) {
                            out.println(ServerMessaging.OUT_ERROR + ", " + ServerMessaging.ERROR_LOGIN);
                        } else {
                            server.receiveWin(account.getUsername(), args[1]);
                            logger.log(new LogEvent(this, "Result sent: " + account.getUsername() + " beat " + args[1]));
                        }
                        break;
                    case ServerMessaging.IN_GAME_DRAW:
                        if (args.length != 2) {
                            out.println(ServerMessaging.OUT_ERROR + "," + ServerMessaging.ERROR_MESSAGE_FORMAT);
                        } else if (account == null) {
                            out.println(ServerMessaging.OUT_ERROR + ", " + ServerMessaging.ERROR_LOGIN);
                        } else {
                            server.receiveDraw(account.getUsername(), args[1]);
                            logger.log(new LogEvent(this, "Result sent: " + account.getUsername() + " drew against " + args[1]));
                        }
                        break;
                    case ServerMessaging.IN_PLAYERS_RECEIVED:
                        if (!startedSendingPlayers) {
                            sendingPlayers = false;
                        }
                    default:
                        out.println(ServerMessaging.ERROR_UNKNOWN_MESSAGE);
                }
            }
        } catch (IOException e) {

        }

        server.disconnectClient(this);

    }

    /**
     * Returns the IP address of the client
     *
     * @return the IP address of the client
     */
    public String getIP()
    {
        return socket.getInetAddress().toString().substring(1); // Remove the / returned with the IP
    }

    /**
     * Pushes an invite to the client
     *
     * @param sender the username of the sending user
     */
    public void pushInvite(String sender)
    {
        out.println(ServerMessaging.OUT_RECEIVED_INVITE + ", " + sender);
    }

    /**
     * Notifies the sender of an invite of the invitee's acceptance
     *
     * @param recipient the username of the receiving user
     * @param recipientIP the IP address of the receiving user
     */
    public void inviteAccepted(String recipient, String recipientIP)
    {
        out.println(ServerMessaging.OUT_ACCEPTED_INVITE + ", " + recipient + ", " + recipientIP);
    }

    /**
     * Notifies the sender that an invite was closed without being accepted
     *
     * @param recipient the username of the receiving user
     */
    public void inviteRejected(String recipient)
    {
        out.println(ServerMessaging.OUT_REJECTED_INVITE + ", " + recipient);
    }

    /**
     * Notifies the recipient that an invite was closed without being accepted
     *
     * @param sender the username of the sending user
     */
    public void inviteCancelled(String sender)
    {
        out.println(ServerMessaging.OUT_CANCELED_INVITE + ", " + sender);
    }

    /**
     * Disconnects the client from the server
     */
    public void disconnect()
    {
        try {
            out.println(ServerMessaging.OUT_DISCONNECTING);
            socket.close();
        } catch (IOException ex) {

        }
        logger.log(new LogEvent(this, "Client disconnected."));
    }

    @Override
    public String getSourceName()
    {
        if (account == null) {
            return "[Unauthenticated Client Thread, IP: " + getIP() + "]";
        } else {
            return "[Client Thread, Account: " + account.getUsername() + ", IP: " + getIP() + "]";
        }
    }

}
