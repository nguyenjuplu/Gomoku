/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JFrame;

/**
 * This class controlls the offline view. It is created when the user starts the
 * program and cannot connect to the server.
 *
 * @author snake
 */
public class OfflineController
{
    /**
     * The view for this controller
     */
    private OfflineView view;
    /**
     * The master controller
     */
    private MasterController master;
    
    /**
     * Creates an OfflineController with a given master controller
     * @param master the master  controller
     */
    public OfflineController(MasterController master)
    {
        this.master = master;
        view = new OfflineView(this);
    }
    
    /**
     * Places this controller's view within a given frame
     * @param target the frame to take over
     */
    public void takeover(JFrame target)
    {
        target.setContentPane(view);
    }
    
    
    /**
     * When the connect button is pushed, the master controller's connect
     * method is called to retry connection to the server.
     */
    public void connectPushed(){
        Properties prop = new Properties();
        InputStream input = null;
        try{
            input = new FileInputStream("src/Client/config.properties");
            
            prop.load(input);
            
            
        } catch (FileNotFoundException ex) {
           System.out.println("File not found");
        } catch (IOException ex) {
           System.out.println("Failed to read file.");
        }
        if(prop.isEmpty())
            master.connect("localhost");
        
        master.connect(prop.getProperty("IP_ADDRESS"));
    }
}
