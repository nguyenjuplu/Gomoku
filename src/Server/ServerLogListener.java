/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 * Allows an object to receive notifications from the server.
 *
 * @author Nathann Hohnbaum
 */
public interface ServerLogListener
{

    /**
     * Called when the server writes a message to the log.
     *
     * @param message the message bing logged.
     */
    void logMessage(String message);
}
