/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import common.ServerMessaging;

/**
 * The AccountManager class manages all of the accounts that have been created
 *
 * @author Nathann Hohnbaum
 * @date 17 March 2018
 */
public class AccountManager
{

    /**
     * Beginning of the usernames for all guest accounts
     */
    public static final String GUEST_PREFIX = "Guest_";
    /**
     * Stores the accounts and provides lookup by username
     */
    private HashMap<String, Account> accounts;

    /**
     * Stores a unique ID for the next guest account to appear
     */
    private long nextGuestID;

    /**
     * The number of strings to expect when splitting the account strings out of
     * the server file
     */
    public static final int ACCOUNT_LENGTH = 5;
    /**
     * The index within the split array of strings where to find the username
     * when reading the server file
     */
    public static final int USERNAME_INDEX = 0;
    /**
     * The index within the split array of strings where to find the password
     * when reading the server file
     */
    public static final int PASSWORD_INDEX = 1;
    /**
     * The index within the split array of strings where to find the win count
     * when reading the server file
     */
    public static final int WINS_INDEX = 2;
    /**
     * The index within the split array where to find the loss count when
     * reading the server file
     */
    public static final int LOSSES_INDEX = 3;
    /**
     * The index within the split array where to find the draw count when
     * reading the server file
     */
    public static final int DRAWS_INDEX = 4;

    /**
     * Creates a new AccountManager with no accounts loaded
     */
    public AccountManager()
    {
        accounts = new HashMap<>();
        nextGuestID = 0;
    }

    /**
     * Creates a new account with a given username and password and no previous
     * statistical data
     *
     * @param username the username for the account
     * @param password the password for the account
     * @return the newly created account
     * @throws AuthenticateException if the username is already taken
     */
    public Account createAccount(String username, String password)
            throws AuthenticateException
    {
        if (username.contains(" ") || username.contains(",")
                || password.contains(" ") || password.contains(",")
                || username.length() == 0 || password.length() == 0) {
            throw new AuthenticateException(ServerMessaging.ERROR_ILLEGAL_CHAR);
        }
        if (username.startsWith(GUEST_PREFIX)) {
            throw new AuthenticateException(
                    ServerMessaging.ERROR_BAD_USERNAME);
        }
        if (accounts.containsKey(username)) {
            throw new AuthenticateException(ServerMessaging.ERROR_USERNAME_TAKEN);
        }
        Account ret = new Account(username, password);
        accounts.put(username, ret);
        return ret;
    }

    /**
     * Authenticates a given username and password
     *
     * @param username the username of the account
     * @param password the password of the account
     * @return the account with the given username and password
     * @throws AuthenticateException if either the password or username is
     * incorrect
     */
    public Account authenticate(String username, String password)
            throws AuthenticateException
    {
        if (username.contains(" ") || username.contains(",")
                || password.contains(" ") || password.contains(",")) {
            throw new AuthenticateException(ServerMessaging.ERROR_ILLEGAL_CHAR);
        }
        if (username.startsWith(GUEST_PREFIX)) {
            throw new AuthenticateException(ServerMessaging.ERROR_BAD_LOGIN);
        }
        if (!accounts.containsKey(username)) {
            throw new AuthenticateException(ServerMessaging.ERROR_BAD_LOGIN);
        }
        Account ret = accounts.get(username);

        if (!ret.getPassword().equals(password)) {
            throw new AuthenticateException(ServerMessaging.ERROR_BAD_LOGIN);
        }
        return ret;
    }

    /**
     * Creates a new guest account and returns it
     *
     * @return a new guest account added to the manager
     */
    public Account createGuestAccount()
    {
        Account ret = new Account(GUEST_PREFIX + nextGuestID, "");
        nextGuestID++;
        return ret;
    }

    /**
     * Loads the accounts from a file
     *
     * @param serverFile the file to read accounts from
     * @throws CorruptServerFileException if the given server file could not be
     * loaded
     */
    public void loadAccounts(File serverFile) throws CorruptServerFileException
    {
        accounts.clear();
        try (Scanner reader = new Scanner(serverFile)) {
            while (reader.hasNext()) {
                String accountData = reader.nextLine();
                if (accountData.length() == 0) {
                    continue;
                }
                String[] details = accountData.split(", ");
                if (details.length != ACCOUNT_LENGTH) {
                    throw new CorruptServerFileException();
                }
                try {
                    String username = details[USERNAME_INDEX];
                    String password = details[PASSWORD_INDEX];
                    int wins = Integer.parseInt(details[WINS_INDEX]);
                    int losses = Integer.parseInt(details[LOSSES_INDEX]);
                    int draws = Integer.parseInt(details[DRAWS_INDEX]);
                    Account account = new Account(username, password, wins, losses, draws);
                    accounts.put(username, account);
                } catch (NumberFormatException e) {
                    throw new CorruptServerFileException();
                }
            }
        } catch (FileNotFoundException e) {
            throw new CorruptServerFileException();
        }
    }

    /**
     * Saves the accounts to a file
     *
     * @param serverFile the file to save the accounts to
     * @throws FileNotFoundException if the file does not exist
     */
    public void saveAccounts(File serverFile) throws FileNotFoundException
    {
        try (PrintWriter pw = new PrintWriter(serverFile)) {
            for (Account account : accounts.values()) {
                if (account.getUsername().startsWith(GUEST_PREFIX)) {
                    continue; // Do not save guest accounts to the file
                }
                pw.println(account.toString());
            }
        }
    }

    /**
     * Returns true if there is an account with a given username
     *
     * @param username the username of the account
     * @return true if an account with the specified username exists
     */
    public boolean hasAccount(String username)
    {
        return accounts.containsKey(username);
    }

    /**
     * Returns the account with the specified username
     *
     * @param username the username of the account
     * @return the account with the specified username, null otherwise
     */
    public Account getAccount(String username)
    {
        if (!hasAccount(username)) {
            return null;
        } else {
            return accounts.get(username);
        }
    }

}
