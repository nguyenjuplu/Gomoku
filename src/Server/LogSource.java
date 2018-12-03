/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 * The LogSOurce interface allows an object to server as a source of log
 * messages. If an object is created with an instance of a LogSource, it should
 * be prepared for the event that its instance of a LogSource is null. In this
 * case, the object is not to be logged.
 *
 * @author Nathann Hohnbaum
 */
public interface LogSource
{

    /**
     * Returns a String representing how this LogSource will identify itself to
     * the log manager
     *
     * @return a String representing how this LogSource will identify itself to
     * the log manager. The source should be enclosed in square brackets if it
     * is not generic.
     */
    String getSourceName();
}
