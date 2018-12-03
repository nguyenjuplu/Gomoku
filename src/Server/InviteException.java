/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 * The InviteException class describes an exception involving an invite operation
 * @author Nathann Hohnbaum
 */
public class InviteException extends Exception
{
    /**
     * Creates an InviteException
     * @param message the message for the InviteException
     */
    public InviteException(String message)
    {
        super(message);
    }
}
