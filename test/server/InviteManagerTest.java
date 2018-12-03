/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import Server.InviteManager;

import Server.Invite;

/**
 *
 * @author Nathann Hohnbaum
 */
public class InviteManagerTest
{
    private InviteManager i1;
    private InviteManager i2;
    private InviteManager i3;
    private InviteManager i4;
    
    @Before
    public void setup()
    {
        i1 = new InviteManager();
        i2 = new InviteManager();
        i3 = new InviteManager();
        i4 = new InviteManager();
        i2.openInvite("A", "B");
        i3.openInvite("A", "B");
        i3.openInvite("A", "C");
        i4.openInvite("A", "C");
        i4.openInvite("B", "C");
        
    }
    
    @Test
    public void testOpenInvite()
    {
        assertTrue(i1.openInvite("A", "B"));
        assertFalse(i2.openInvite("A","B"));
        assertTrue(i2.openInvite("A","C"));
        assertTrue(i2.openInvite("D","B"));
        assertTrue(i2.openInvite("A", "D"));
        assertTrue(i2.openInvite("C", "B"));
        assertTrue(i2.openInvite("B","A"));
    }
    
    @Test
    public void testCloseInvite()
    {
        assertFalse(i1.closeInvite("A", "B"));
        assertTrue(i2.closeInvite("A", "B"));
        assertFalse(i2.closeInvite("A", "B"));
        assertTrue(i3.closeInvite("A", "B"));
        assertTrue(i3.closeInvite("A", "C"));
        assertTrue(i4.closeInvite("A", "C"));
        assertTrue(i4.closeInvite("B", "C"));
        
    }
    
    @Test
    public void testCloseInvitesWithSender()
    {
        i1.closeInvitesWithSender("A");
        i2.closeInvitesWithSender("B");
        assertFalse(i2.openInvite("A", "B"));
        i2.closeInvitesWithSender("A");
        assertFalse(i2.closeInvite("A", "B"));
        i3.closeInvitesWithSender("A");
        assertFalse(i3.closeInvite("A", "B"));
        assertFalse(i3.closeInvite("A", "C"));
        i4.closeInvitesWithSender("A");
        assertFalse(i4.closeInvite("A", "C"));
        assertTrue(i4.closeInvite("B", "C"));
    }
    
    @Test
    public void testCloseInvitesWithRecipient()
    {
        i1.closeInvitesWithRecipient("A");
        i2.closeInvitesWithRecipient("A");
        assertFalse(i2.openInvite("A", "B"));
        i2.closeInvitesWithRecipient("B");
        assertFalse(i2.closeInvite("A", "B"));
        i3.closeInvitesWithRecipient("B");
        assertFalse(i3.closeInvite("A", "B"));
        assertTrue(i3.closeInvite("A", "C"));
        i4.closeInvitesWithRecipient("C");
        assertFalse(i4.closeInvite("A", "C"));
        assertFalse(i4.closeInvite("B", "C"));
    }
    
}
