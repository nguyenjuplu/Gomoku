/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The InviteManager class manages the open invites for the server. It supports
 * opening invites, closing individual invites, and closing invites involving a
 * particular user.
 *
 * @author Nathann Hohnbaum
 */
public class InviteManager
{

    /**
     * The open invites
     */
    private final Set<Invite> invites;

    /**
     * Creates an InviteManager
     */
    public InviteManager()
    {
        invites = new HashSet<>();
    }

    /**
     * Opens a new invite with a given sender and recipient
     *
     * @param sender the username of the sending user
     * @param recipient the username of the receiving user
     * @return true if the invite is opened, false otherwise
     */
    public boolean openInvite(String sender, String recipient)
    {
        synchronized (invites) {
            return invites.add(new Invite(sender, recipient));
        }
    }

    /**
     * Closes an invite with the given sender and recipient
     *
     * @param sender the username of the sending user
     * @param recipient the username of the receiving user
     * @return true if the invite is closed, false otherwise
     */
    public boolean closeInvite(String sender, String recipient)
    {
        synchronized (invites) {
            return invites.remove(new Invite(sender, recipient));
        }
    }

    /**
     * Closes all invites with a given sender
     *
     * @param sender the username of the sending user
     * @return a list of all of the recipients of invites with the given sender
     */
    public List<String> closeInvitesWithSender(String sender)
    {
        LinkedList<String> ret = new LinkedList<>();
        synchronized (invites) {
            Set<Invite> toRemove = new HashSet<>();
            for (Invite i : invites) {
                if (i.getSender().equals(sender)) {
                   toRemove.add(i);
                    ret.add(i.getRecipient());
                }
            }
            for (Invite i: toRemove) {
                invites.remove(i);
            }
        }

        return ret;
    }

    /**
     * Closes all invites with a given recipient
     *
     * @param sender the username of the receiving user
     * @return a list of all of the senders of invites with the given recipient
     */
    public List<String> closeInvitesWithRecipient(String recipient)
    {
        LinkedList<String> ret = new LinkedList<>();
        synchronized (invites) {
            Set<Invite> toRemove = new HashSet<>();
            for (Invite i : invites) {
                if (i.getRecipient().equals(recipient)) {
                   toRemove.add(i);
                    ret.add(i.getSender());
                }
            }
            for (Invite i: toRemove) {
                invites.remove(i);
            }
        }

        return ret;
    }
}
