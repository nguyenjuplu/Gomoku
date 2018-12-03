/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import static org.junit.Assert.*;
import org.junit.Test;

import Server.Invite;


/**
 *
 * @author Nathann Hohnbaum
 */
public class InviteTest
{
    
    @Test
    public void testCreateInvite()
    {
        Invite i1 = new Invite("A","B");
        assertEquals("A",i1.getSender());
        assertEquals("B",i1.getRecipient());
    }
    
}
