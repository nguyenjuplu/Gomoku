/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.Date;

/**
 * The LogManager interface allows a class to identify itself as a source of
 * logs. If a reference to a LogManager is null, then the containing instance is
 * not to be logged.
 *
 * @author Nathann Hohnbaum
 */
public interface LogManager
{
    /**
     * Logs a given LogEvent
     * @param e the event to log
     */
    void log(LogEvent e);
}
