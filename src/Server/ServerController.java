/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;

/**
 * The SrverController serves as the controller for the interface used by the
 * server administrator.
 *
 * @author Nathann Hohnbaum
 * @date 17 March 2018
 */
public class ServerController implements ServerLogListener
{

    /**
     * Writes to the log file
     */
    private PrintWriter logger;

    /**
     * This controller's view object
     */
    private ServerView view;

    /**
     * The directory for the server to store its server file and backups
     */
    private Path serverFilePath;

    /**
     * True if the server is running, false otherwise
     */
    private boolean running;

    /**
     * The server object
     */
    private Server server;

    /**
     * The root of the log file
     */
    public static final String SERVER_LOG_ROOT = "log";

    /**
     * The extension of the log file
     */
    public static final String SERVER_LOG_EXTENSION = ".txt";

    /**
     * Creates a new ServerController to run the application
     */
    public ServerController()
    {
        running = false;
        view = new ServerView(this);
        view.setVisible(true);
        serverFilePath = null;
        server = new Server();
        server.addListener(this);
    }

    /**
     * Called by the view when the admin presses the start/stop button
     */
    public void startButtonPressed()
    {
        if (running) {
            // Stop the server
            server.stopServer(serverFilePath);
            running = false;
            view.serverStopped();
            if (logger != null) {
                logger.close();
                logger = null;
            }
        } else {
            // Start the server// Setup the log file
            serverFilePath = view.getServerFilePath();
            if (serverFilePath == null)
                return;
            try {
                logger = new PrintWriter(logFilePath(serverFilePath).toFile());
            } catch (FileNotFoundException e) {

            }
            if (serverFilePath != null) {
                server.startServer(serverFilePath);
                running = true;
                view.serverStarted();
            }
        }
    }

    /**
     * Returns the path to the log file within the server file directory
     *
     * @param serverFilePath the directory of the server file
     * @return the path to the log file within the server file directory
     */
    private Path logFilePath(Path serverFilePath)
    {
        return serverFilePath.resolve(SERVER_LOG_ROOT + SERVER_LOG_EXTENSION);
    }

    @Override
    public void logMessage(String message)
    {
        view.writeLogMessage(message);
        logger.println(message);
    }

}
