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

import Server.Account;

/**
 *
 * @author Nathann Hohnbaum
 */
public class AccountTest
{
    
    public AccountTest()
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
    }
    
    @After
    public void tearDown()
    {
    }

    // TODO: Update test when account statistics are pressent
    @Test
    public void testToString()
    {
        Account acc1 = new Account("Billy", "secure");
        Account acc2 = new Account("Michael", "abc123");
        Account acc3 = new Account("", "");
        
        assertEquals("Billy, secure", acc1.toString());
        assertEquals("Michael, abc123", acc2.toString());
        assertEquals(", ", acc3.toString());
    }
}
