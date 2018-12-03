/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.Objects;

/**
 * The invite class tracks an individual open invite on the server end. The
 * invite object stores the sender and the recipient.
 *
 * @author Nathann Hohnbaum
 */
public class Invite
{

    /**
     * The username of the user sending the invite
     */
    private String sender;
    /**
     * The username of the user receiving the invite
     */
    private String recipient;

    /**
     * Creates an Invite object
     *
     * @param sender the username of the sending user
     * @param recipient the username of the receiving user
     */
    public Invite(String sender, String recipient)
    {
        this.sender = sender;
        this.recipient = recipient;
    }
    
    /**
     * Returns the username of the sending user
     * @return the username of the sending user
     */
    public String getSender()
    {
        return sender;
    }
    
    /**
     * Returns the username of the receiving user
     * @return the username of the receiving user
     */
    public String getRecipient()
    {
        return recipient;
    }
    
    
    @Override
    public boolean equals(Object other)
    {
        try {
            Invite in = (Invite)other;
            return sender.equals(in.sender) && recipient.equals(in.recipient);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.sender);
        hash = 29 * hash + Objects.hashCode(this.recipient);
        return hash;
    }

}
