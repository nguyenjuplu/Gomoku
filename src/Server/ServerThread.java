/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import common.ServerMessaging;

/**
 * The ServerThread runs on a loop accepting new clients until the server is
 * turned off. Those new clients are passed to the Server object to be processed
 *
 * @author Nathann Hohnbaum
 */
public class ServerThread extends Thread implements LogSource
{

    /**
     * The server socket to accept new clients until the server is turned off
     */
    private ServerSocket serverSocket;

    /**
     * The server to receive new clients
     */
    private Server server;

    /**
     * The logging object
     */
    private LogManager logger;

    /**
     * Creates a ServerThread with a given server to receive clients
     *
     * @param server the server to receive new clients
     * @param logger the object to handle logging events from this object
     * @throws IOException if the server is unable to start
     */
    public ServerThread(Server server, LogManager logger) throws IOException
    {
        this.server = server;
        this.logger = logger;
        serverSocket = new ServerSocket(ServerMessaging.SERVER_PORT);
    }

    @Override
    public void run()
    {
        if (logger != null) {
            logger.log(new LogEvent(this,"Server thread started."));
        }
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientThread client = new ClientThread(clientSocket, server, logger);
                server.connect(client);
            }
        } catch (IOException e) {

        }
        if (logger != null) {
            logger.log(new LogEvent(this, "Server thread stopped."));
        }
    }

    /**
     * Stops accepting new connections
     */
    public void stopAccepting()
    {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            if (logger != null) {
                logger.log(new LogEvent(this,"Error: "+ex.getMessage()));
            }
        }
    }

    @Override
    public String getSourceName()
    {
        return "Server Thread";
    }
}
