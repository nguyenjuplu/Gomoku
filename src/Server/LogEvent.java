/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.Date;

/**
 * The LogEvent class stores details about an event that is to be logged.
 * Different subclasses correspond to different events.
 *
 * @author Nathann Hohnbaum
 */
public class LogEvent
{

    /**
     * The date and time of the event
     */
    private final Date eventDate;

    /**
     * The source of the event
     */
    private final LogSource source;
    
    /**
     * The message for this log event
     */
    private final String message;

    /**
     * Creates a new LogEvent that happened at the current date
     *
     * @param source the source of this LogEvent
     */
    public LogEvent(LogSource source, String message)
    {
        eventDate = new Date();
        this.source = source;
        this.message=message;
    }

    @Override
    public String toString()
    {
        return "[" + eventDate.toString() + ": " + source.getSourceName() + "]: " + message;
    }
}
