/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import common.ServerMessaging;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * The model object for the Server application
 *
 * This class processes the server activities of clients including connections,
 * disconnections, logins, invites, and matchmaking
 *
 * @date 17 March 2018
 * @author Nathann Hohnbaum
 */
public class Server implements LogManager, LogSource
{

    /**
     * The root of the file name for the server file
     */
    public static final String SERVER_FILE_ROOT = "accounts";

    /**
     * Appended to render a file a backup
     */
    public static final String SERVER_FILE_BACKUP_TOKEN = "_backup";

    /**
     * The number of backups of the server file to keep
     */
    public static final int SERVER_FILE_BACKUP_COUNT = 10;

    /**
     * The extension for the server file
     */
    public static final String SERVER_FILE_EXTENSION = ".txt";


    /**
     * Account manager for the server
     */
    private AccountManager accounts;

    /**
     * Stores the ClientThread objects for clients who have already logged in
     */
    private final HashMap<String, ClientThread> users;

    /**
     * Stores the ClientThread objects for clients who have not yet logged in
     */
    private final HashSet<ClientThread> newUsers;

    /**
     * The currently running server thread
     */
    private ServerThread serverThread;

    /**
     * Manages the invites on the server
     */
    private InviteManager invites;


    /**
     * The listeners of this server
     */
    private ArrayList<ServerLogListener> listeners;

    /**
     * Initializes the server
     */
    public Server()
    {
        accounts = new AccountManager();
        users = new HashMap<>();
        newUsers = new HashSet<>();
        invites = new InviteManager();
        listeners = new ArrayList<>();
    }

    /**
     * Adds a ServerLogListener to this server
     *
     * @param l the listener to add
     */
    public void addListener(ServerLogListener l)
    {
        listeners.add(l);
    }

    /**
     * Process a request to update the player list
     */
    public void refreshPlayers()
    {
        Collection<String> playerNames;
        synchronized (users) {
            playerNames = users.keySet();
            for (ClientThread c : users.values()) {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        c.sendPlayerList(playerNames);
                    }
                }.start();
            }
        }

    }

    @Override
    public synchronized void log(LogEvent e)
    {
        for (ServerLogListener l : listeners) {
            l.logMessage(e.toString());
        }
    }

    /**
     * Called when a new client has been connected
     *
     * @param newClient the client who has just been connected
     */
    public void connect(ClientThread newClient)
    {
        synchronized (newUsers) {
            newUsers.add(newClient);
        }
        newClient.start();
    }

    /**
     * Starts the server and loads the contents from the server file
     *
     * @param serverFilePath the path to the directory containing the server
     * file and its backups
     */
    public void startServer(Path serverFilePath)
    {
       
        log(new LogEvent(this, "Starting server.,,"));
        // Load the server file
        boolean loaded = false;
        for (int i = 0; i <= SERVER_FILE_BACKUP_COUNT && !loaded; i++) {
            try {
                File serverFile;
                if (i == 0) {
                    // Normal case: load the server file
                    serverFile = currentServerFilePath(serverFilePath).toFile();
                } else {
                    // Abnormal case: server file is corrupt, so load a backup
                    serverFile = backupPath(serverFilePath, i).toFile();
                }
                accounts.loadAccounts(serverFile);
                loaded = true; // Successfully loaded a file, so stop loading
            } catch (CorruptServerFileException e) {
                log(new LogEvent(this, "The server file is corrupt.  Attempting to load backup."));
            }
        }
        // Start the server thread
        try {
            serverThread = new ServerThread(this, this);
            serverThread.start();
            log(new LogEvent(this, "Server successfully started."));
        } catch (IOException e) {
            log(new LogEvent(this, "Unable to start server.  Error: " + e.getMessage()));
        }
    }

    /**
     * Stops the server and saves the account data to a given file
     *
     * @param serverFilePath the path to the directory containing the server
     * file and its backups
     */
    public void stopServer(Path serverFilePath)
    {
        log(new LogEvent(this, "Stopping server..."));
        // Disconnect everyone
        for (ClientThread client : newUsers) {
            client.disconnect();
        }
        for (ClientThread client : users.values()) {
            client.disconnect();
        }
        users.clear();
        newUsers.clear();
        // Stop the server
        if (serverThread != null) {
            serverThread.stopAccepting();
            serverThread = null;
        }
        saveData(serverFilePath);
        
    }

    /**
     * Returns the backup file path for the server file
     *
     * @param serverFilePath the path to the directory containing the server
     * file
     * @param backup the backup number
     * @return the path to the specified number backup of the server file
     */
    private Path backupPath(Path serverFilePath, int backup)
    {
        return serverFilePath.resolve(SERVER_FILE_ROOT + SERVER_FILE_BACKUP_TOKEN + backup + SERVER_FILE_EXTENSION);
    }

    /**
     * Returns the path of the server file within the targeted directory
     *
     * @param serverFilePath the path to the server file directory
     * @return the path to the server file within the specified directory
     */
    private Path currentServerFilePath(Path serverFilePath)
    {
        return serverFilePath.resolve(SERVER_FILE_ROOT + SERVER_FILE_EXTENSION);
    }



    /**
     * Creates backups for the server file
     *
     * @param serverFilePath the path to the server file directory
     */
    private void createBackups(Path serverFilePath)
    {
        File oldestBackup = backupPath(serverFilePath, SERVER_FILE_BACKUP_COUNT).toFile();
        if (oldestBackup.exists()) {
            oldestBackup.delete();
        }
        for (int i = SERVER_FILE_BACKUP_COUNT - 1; i >= 1; i--) {
            File targetBackup = backupPath(serverFilePath, i).toFile();
            if (targetBackup.exists()) {
                File shiftBackup = backupPath(serverFilePath, i + 1).toFile();
                targetBackup.renameTo(shiftBackup);
            }
        }
        File currentServerFile = currentServerFilePath(serverFilePath).toFile();
        File currentBackup = backupPath(serverFilePath, 1).toFile();
        if (currentServerFile.exists()) {
            currentServerFile.renameTo(currentBackup);
        }
    }

    /**
     * Saves the current state of the server to a server file in the specified
     * directory
     *
     * @param serverFilePath the path to the directory to store the server file
     */
    public void saveData(Path serverFilePath)
    {

        log(new LogEvent(this, "Saving server data..."));

        createBackups(serverFilePath);
        File serverFile = currentServerFilePath(serverFilePath).toFile();
        try {
            accounts.saveAccounts(serverFile);
            log(new LogEvent(this, "Server data saved successfully."));
        } catch (IOException e) {
            log(new LogEvent(this, "Unable to save server data.  Error: " + e.getMessage()));
        }
    }

    /**
     * Called by a ClientThread for a client who has disconencted
     *
     * @param sender the ClientThread that has disconnected
     */
    public void disconnectClient(ClientThread sender)
    {
        sender.disconnect();
        if (sender.getAccount() != null) {
            synchronized (users) {
                users.remove(sender.getAccount().getUsername());
            }
            closeAllInvitesWithUser(sender.getAccount().getUsername());
            refreshPlayers();
        } else {
            synchronized (newUsers) {
                newUsers.remove(sender);
            }
        }
    }

    @Override
    public String getSourceName()
    {
        return "Server";
    }

    // -------------------------------------------------------------------------
    // Authentication operations
    // -------------------------------------------------------------------------
    /**
     * Performs a login request
     *
     * @param sender the ClientThread sending the login request
     * @param username the username of the account
     * @param password the password of the account
     * @throws AuthenticateException if the login attempt is unsuccessful
     */
    public void login(ClientThread sender, String username, String password)
            throws AuthenticateException
    {

        Account account = accounts.authenticate(username, password);
        if (users.containsKey(account.getUsername())) {
            throw new AuthenticateException(ServerMessaging.ERROR_DUPLICATE_LOGIN);
        }
        loginClient(sender, account);
    }

    /**
     * Performs a register request and logs the ClientThread in upon success
     *
     * @param sender the ClientThread sending the register request
     * @param username the username of the new account
     * @param password the password of the new account
     * @throws AuthenticateException if the registration is unsuccessful
     */
    public void register(ClientThread sender, String username,
            String password) throws AuthenticateException
    {
        Account account = accounts.createAccount(username, password);
        loginClient(sender, account);
    }

    /**
     * Performs a play as guest request
     *
     * @param sender the ClientThread sending the play as guest request
     */
    public void playAsGuest(ClientThread sender)
    {
        Account account = accounts.createGuestAccount();
        loginClient(sender, account);
    }

    /**
     * Logs a given client into a given account
     *
     * @param client the client logging in
     * @param account the account being logged into
     */
    private void loginClient(ClientThread client, Account account)
    {
        synchronized (newUsers) {
            newUsers.remove(client);
        }
        synchronized (users) {
            users.put(account.getUsername(), client);
        }
        client.setAccount(account);
    }

    /**
     * Logs a given user out
     *
     * @param sender the user logging out
     */
    public void logout(ClientThread sender)
    {
        synchronized (users) {
            if (!users.containsValue(sender)) {
                return;
            }
            users.remove(sender.getAccount().getUsername());
        }
        closeAllInvitesWithUser(sender.getAccount().getUsername());
        synchronized (newUsers) {
            newUsers.add(sender);
        }
        sender.setAccount(null);
    }

    /**
     * Closes all invites involving a particular user
     *
     * @param username the username whose invites are to be closed
     */
    private void closeAllInvitesWithUser(String username)
    {
        // Close the outgoing invites
        List<String> outgoing = invites.closeInvitesWithSender(username);
        for (String recipient : outgoing) {
            synchronized (users) {
                users.get(recipient).inviteCancelled(username);
            }
        }
        // Close the incoming invites
        List<String> incoming = invites.closeInvitesWithRecipient(username);
        for (String sender : incoming) {
            synchronized (users) {
                users.get(sender).inviteRejected(username);
            }
        }

    }

    // -------------------------------------------------------------------------
    // Invite operations
    // -------------------------------------------------------------------------
    /**
     * Opens an invite from one user to another
     *
     * @param sender the username of the sending user
     * @param recipient the username of the receiving user
     * @throws InviteException if either user is offline or if an invite is
     * already open
     */
    public void sendInvite(String sender, String recipient) throws InviteException
    {
        synchronized (users) {
            if (!users.containsKey(sender) || !users.containsKey(recipient)) {
                throw new InviteException("User not online");
            }
            if (!invites.openInvite(sender, recipient)) {
                throw new InviteException("Invite already open");
            }
        }
        ClientThread target = users.get(recipient);
        target.pushInvite(sender);
    }

    /**
     * Cancels an invite from one user to another
     *
     * @param sender the username of the sending user
     * @param recipient the username of the receiving user
     * @throws InviteException if no such invite is open
     */
    public void cancelInvite(String sender, String recipient) throws InviteException
    {
        if (!invites.closeInvite(sender, recipient)) {
            throw new InviteException("No such invite");
        }
        ClientThread target = users.get(recipient);
        target.inviteCancelled(sender);
    }

    /**
     * Accepts an invite between two users
     *
     * @param sender the username of the sending user
     * @param recipient the username of the receiving user
     * @throws InviteException if no such invite is open
     */
    public void acceptInvite(String sender, String recipient) throws InviteException
    {
        if (!invites.closeInvite(sender, recipient)) {
            throw new InviteException("No such invite");
        }
        ClientThread target = users.get(sender);
        String recIP = users.get(recipient).getIP();
        target.inviteAccepted(recipient, recIP);
    }

    /**
     * Rejects an invite between two users
     *
     * @param sender the username of the sending user
     * @param recipient the username of the receiving user
     * @throws InviteException if no such invite is open
     */
    public void rejectInvite(String sender, String recipient) throws InviteException
    {
        if (!invites.closeInvite(sender, recipient)) {
            throw new InviteException("No such invite");
        }
        ClientThread target = users.get(sender);
        target.inviteRejected(recipient);
    }

    // -------------------------------------------------------------------------
    // Gameplay operations
    // -------------------------------------------------------------------------
    /**
     * Called when one user beats another
     *
     * @param winner the winner of the game
     * @param loser the loser of the game
     */
    public void receiveWin(String winner, String loser)
    {
        synchronized (users) {
            if (!(users.containsKey(winner) && accounts.hasAccount(loser))) {

            } else {
                users.get(winner).getAccount().addWin();
                accounts.getAccount(loser).addLoss();
            }
        }
    }

    /**
     * Called when a game ends in a draw
     *
     * @param p1 one player in the game
     * @param p2 the other player in the game
     */
    public void receiveDraw(String p1, String p2)
    {
        synchronized (users) {
            if (!(users.containsKey(p1) && accounts.hasAccount(p2))) {

            } else {
                users.get(p1).getAccount().addDraw();
                accounts.getAccount(p2).addDraw();
            }
        }
    }

}
