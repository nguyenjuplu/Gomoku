/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 * The account class stores the data for the account of a single user
 *
 * @author Nathann Hohnbaum
 */
public class Account
{

    /**
     * The username for this account
     */
    private String username;
    /**
     * The password for this account
     */
    private String password;

    /**
     * The number of wins acquired by this account
     */
    private int wins;

    /**
     * The number of losses acquired by this account
     */
    private int losses;

    /**
     * The number of draws acquired by this account
     */
    private int draws;

    /**
     * Creates an account with a specified number of wins, losses, and draws
     *
     * @param username the username of the account
     * @param password the password of the account
     * @param wins the total number of wins on the account
     * @param losses the total number of losses on the account
     * @param draws the total number of draws on the account
     */
    public Account(String username, String password, int wins, int losses, int draws)
    {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    /**
     * Creates a brand new account with a given username and password and no
     * previously recorded statistics
     *
     * @param username the username for the account
     * @param password the password for the account
     */
    public Account(String username, String password)
    {
        this(username, password, 0, 0, 0);
    }

    /**
     * Returns the username of this account
     *
     * @return the username of this account
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Returns the password of this account
     *
     * @return the password of this account
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Returns the total number of wins on this account
     *
     * @return the total number of wins on this account
     */
    public int getWins()
    {
        return wins;
    }

    /**
     * Returns the total number of losses on this account
     *
     * @return the total number of losses on this account
     */
    public int getLosses()
    {
        return losses;
    }

    /**
     * Returns the total number of draws on this account
     *
     * @return the total number of draws on this account
     */
    public int getDraws()
    {
        return draws;
    }

    /**
     * Adds one win to this account
     */
    public void addWin()
    {
        wins++;
    }

    /**
     * Adds one loss to this account
     */
    public void addLoss()
    {
        losses++;
    }

    /**
     * Adds one draw to this account
     */
    public void addDraw()
    {
        draws++;
    }

    /**
     * Returns a string representation of this account to write to the server
     * file
     *
     * @return a string representation of this account
     */
    @Override
    public String toString()
    {
        return username + ", " + password + ", " + wins + ", " + losses + ", " + draws;
    }

}
