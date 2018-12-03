/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import Server.AccountManager;
import Server.AuthenticateException;
import Server.Account;

/**
 *
 * @author Nathann Hohnbaum
 */
public class AccountManagerTest
{

    private AccountManager accountTest1;
    private Account test1Account1;
    private Account test1Account2;
    private Account test1Account3;
    private Account test1Account4;

    private AccountManager accountTest2;
    private Account test2Account1;
    private Account test2Account2;
    private Account test2Account3;
    private Account test2Account4;
    private Account test2GuestAccount1;
    private Account test2GuestAccount2;

    public AccountManagerTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
        accountTest1 = new AccountManager();
        accountTest2 = new AccountManager();
        try {
            test1Account1 = accountTest1.createAccount("test1", "abc123");
            test1Account2 = accountTest1.createAccount("test2", "pass");
            test1Account3 = accountTest1.createAccount("test3", "A");
            test1Account4 = accountTest1.createAccount("test4", "Supers3cur3!");

            test2Account1 = accountTest2.createAccount("test1", "abc123");
            test2Account2 = accountTest2.createAccount("test2", "pass");
            test2Account3 = accountTest2.createAccount("test3", "A");
            test2Account4 = accountTest2.createAccount("test4", "Supers3cur3!");

            test2GuestAccount1 = accountTest2.createGuestAccount();
            test2GuestAccount2 = accountTest2.createGuestAccount();

        } catch (AuthenticateException e) {
            fail();
        }

    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test successful authentication and ensure that authenticating returns
     * references to accounts stored in the account manager
     */
    @Test
    public void testSuccessfulAuthenticate()
    {
        try {
            assertTrue(test1Account1 == accountTest1.authenticate("test1", "abc123"));
            assertTrue(test1Account2 == accountTest1.authenticate("test2", "pass"));
            assertTrue(test1Account3 == accountTest1.authenticate("test3", "A"));
            assertTrue(test1Account4 == accountTest1.authenticate("test4", "Supers3cur3!"));

            assertTrue(test2Account1 == accountTest2.authenticate("test1", "abc123"));
            assertTrue(test2Account2 == accountTest2.authenticate("test2", "pass"));
            assertTrue(test2Account3 == accountTest2.authenticate("test3", "A"));
            assertTrue(test2Account4 == accountTest2.authenticate("test4", "Supers3cur3!"));
        } catch (AuthenticateException e) {
            fail();
        }
    }

    @Test
    public void testUnsuccessfulAuthenticate()
    {
        // Bad password
        try {
            accountTest1.authenticate("test1", "abc");
            fail();
        } catch (AuthenticateException e) {

        }
        // Bad username
        try {
            accountTest1.authenticate("test0", "abc");
            fail();
        } catch (AuthenticateException e) {

        }
        // Ensure that passwords are case sensitive
        try {
            accountTest1.authenticate("test3", "a");
            fail();
        } catch (AuthenticateException e) {

        }
        // Ensure user cannot log into guest account
        try {
            accountTest2.authenticate("Guest_0", "");
            fail();
        } catch (AuthenticateException e) {

        }
    }

    /**
     * Ensure that successful registration works
     */
    @Test
    public void testSuccessfulRegister()
    {
        try {
            Account newAccount1 = accountTest1.createAccount("test0", "simple");
            Account newAccount2 = accountTest2.createAccount("test0", "simple");
            assertTrue(newAccount1 == accountTest1.authenticate("test0", "simple"));
            assertTrue(newAccount2 == accountTest2.authenticate("test0", "simple"));
        } catch (AuthenticateException e) {
            fail();
        }

    }

    /**
     * Ensure that unsuccessful registration attempts fail
     */
    @Test
    public void testUnsuccessfulRegister()
    {
        // Forbid duplicate usernames
        try {
            accountTest1.createAccount("test1", "abc123");
            fail();
        } catch (AuthenticateException e) {

        }
        // Forbid commas in username
        try {
            accountTest1.createAccount("a,b", "abc123");
            fail();
        } catch (AuthenticateException e) {

        }
        // Forbid commas in password
        try {
            accountTest1.createAccount("abc", "abc1,23");
            fail();
        } catch (AuthenticateException e) {

        }
        // Forbid spaces in username
        try {
            accountTest1.createAccount("a b", "abc123");
            fail();
        } catch (AuthenticateException e) {

        }
        // Forbid spaces in password
        try {
            accountTest1.createAccount("abc", "abc1 23");
            fail();
        } catch (AuthenticateException e) {

        }
        // Forbid creating accounts with guest-reserved usernames
        try {
            accountTest1.createAccount("Guest_12", "abc123");
            fail();
        } catch (AuthenticateException e) {

        }
        // Forbid empty username
        try {
            accountTest1.createAccount("", "abc123");
            fail();
        } catch (AuthenticateException e) {

        }
        // Forbid empty password
        try {
            accountTest1.createAccount("test0", "");
            fail();
        } catch (AuthenticateException e) {

        }
    }

    /**
     * Ensure that guest accounts are different objects and have different
     * usernames
     */
    @Test
    public void testCreateGuestAccount()
    {
        assertFalse(test2GuestAccount1 == test2GuestAccount2);
        assertFalse(test2GuestAccount1.getUsername().equals(test2GuestAccount2.getUsername()));
    }

}
