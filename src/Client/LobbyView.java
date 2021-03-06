/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import static common.ServerMessaging.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

/**
 *
 * @author John
 * 
 * Edit 4.14.18 @author Jimmy Nguyen
 *      Updated the invite button actionListener to have a check if a Username
 *      existed before it called the sent Invite method.
 *      The view still needs ways to handle incoming "my invite was rejected"
 *      or "they canceled their invite to me"
 */
public class LobbyView extends javax.swing.JPanel {

    private LobbyController controller;
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPane = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        invButton = new javax.swing.JButton();
        offlineDrop = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        logoutButton = new javax.swing.JButton();
        sentList = new javax.swing.JScrollPane();
        recvList = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        playerList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        yourWins = new javax.swing.JLabel();
        yourLosses = new javax.swing.JLabel();
        yourDraws = new javax.swing.JLabel();
        yourMoves = new javax.swing.JLabel();
        playerStatsPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        playerMoves = new javax.swing.JLabel();
        playerLosses = new javax.swing.JLabel();
        playerDraws = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        playerWins = new javax.swing.JLabel();
        playerName = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        leaderboard = new javax.swing.JTextArea();

        tabPane.setMaximumSize(new java.awt.Dimension(1920, 1080));
        tabPane.setMinimumSize(new java.awt.Dimension(640, 480));
        tabPane.setPreferredSize(new java.awt.Dimension(640, 480));

        invButton.setText("Invite");
        invButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invButtonActionPerformed(evt);
            }
        });

        offlineDrop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AI", "Easy", "Medium", "Hard" }));
        offlineDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offlineDropActionPerformed(evt);
            }
        });

        logoutButton.setText("Log Out");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(logoutButton))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(logoutButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sentList.setAutoscrolls(true);
        sentList.setHorizontalScrollBar(null);

        recvList.setToolTipText("");

        playerList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                playerListMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(playerList);

        jLabel1.setText("Sent Invites");

        jLabel2.setText("Received Invites");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(offlineDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                            .addComponent(invButton, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                        .addComponent(recvList, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(sentList, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(203, 203, 203)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recvList, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(offlineDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sentList, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addGap(33, 33, 33))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        tabPane.addTab("Play                            ", jPanel3);

        jPanel1.setPreferredSize(new java.awt.Dimension(640, 480));

        jLabel3.setText("Your Stats");

        jLabel4.setText("Wins:");

        jLabel5.setText("Losses:");

        jLabel6.setText("Draws:");

        jLabel7.setText("Moves:");

        jLabel10.setText("Wins:");

        jLabel8.setText("Draws:");

        playerName.setText("Player's Stats");

        jLabel11.setText("Losses:");

        jLabel12.setText("Moves:");

        javax.swing.GroupLayout playerStatsPanelLayout = new javax.swing.GroupLayout(playerStatsPanel);
        playerStatsPanel.setLayout(playerStatsPanelLayout);
        playerStatsPanelLayout.setHorizontalGroup(
            playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerStatsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerName)
                    .addGroup(playerStatsPanelLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel8)
                            .addComponent(jLabel12))
                        .addGap(37, 37, 37)
                        .addGroup(playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(playerMoves)
                            .addComponent(playerDraws)
                            .addComponent(playerLosses)
                            .addComponent(playerWins))))
                .addContainerGap())
        );
        playerStatsPanelLayout.setVerticalGroup(
            playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerStatsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(playerName)
                .addGap(18, 18, 18)
                .addGroup(playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(playerWins))
                .addGap(18, 18, 18)
                .addGroup(playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(playerLosses))
                .addGap(18, 18, 18)
                .addGroup(playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(playerDraws))
                .addGap(18, 18, 18)
                .addGroup(playerStatsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(playerMoves))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(yourMoves)
                            .addComponent(yourDraws)
                            .addComponent(yourLosses)
                            .addComponent(yourWins)))
                    .addComponent(playerStatsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(503, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(yourWins))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(yourLosses))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(yourDraws))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(yourMoves))
                .addGap(76, 76, 76)
                .addComponent(playerStatsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        tabPane.addTab("Stats                           ", jPanel4);

        leaderboard.setColumns(20);
        leaderboard.setRows(5);
        leaderboard.setFocusable(false);
        jScrollPane2.setViewportView(leaderboard);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );

        tabPane.addTab("Leaderboard                     ", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void offlineDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offlineDropActionPerformed
        controller.startOfflineGame(offlineDrop.getSelectedItem().toString());
    }//GEN-LAST:event_offlineDropActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        controller.logout();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void invButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invButtonActionPerformed
        sendInvite(playerList.getSelectedValue());     
    }//GEN-LAST:event_invButtonActionPerformed

    private void playerListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playerListMousePressed
        // TODO add your handling code here:
        if( SwingUtilities.isRightMouseButton(evt)){
            playerList.setSelectedIndex(playerList.locationToIndex(evt.getPoint()));
            
            JPopupMenu menu = new JPopupMenu();
            JMenuItem viewStats = new JMenuItem("View Stats");
            viewStats.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                     playerName.setText(playerList.getSelectedValue() + "'s Stats");                    
                     playerStatsPanel.setVisible(true);
                     tabPane.setSelectedIndex(1);
                }
            });
            
            JMenuItem invite = new JMenuItem("Invite");
            invite.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    sendInvite(playerList.getSelectedValue());
                }
            });

            menu.add(viewStats);
            menu.add(invite);
            menu.show(playerList, evt.getPoint().x, evt.getPoint().y);
        }
    }//GEN-LAST:event_playerListMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton invButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea leaderboard;
    private javax.swing.JButton logoutButton;
    private javax.swing.JComboBox<String> offlineDrop;
    private javax.swing.JLabel playerDraws;
    private javax.swing.JList<String> playerList;
    private javax.swing.JLabel playerLosses;
    private javax.swing.JLabel playerMoves;
    private javax.swing.JLabel playerName;
    private javax.swing.JPanel playerStatsPanel;
    private javax.swing.JLabel playerWins;
    private javax.swing.JScrollPane recvList;
    private javax.swing.JScrollPane sentList;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JLabel yourDraws;
    private javax.swing.JLabel yourLosses;
    private javax.swing.JLabel yourMoves;
    private javax.swing.JLabel yourWins;
    // End of variables declaration//GEN-END:variables
    //Views of the invites
    private JPanel sentListPanel;
    private JPanel recvListPanel;
    //All of the invites
    private HashMap<String, SentInvite> sentInvitations;
    private HashMap<String, RecvInvite> recvInvitations;
    
    /**
     * Creates new form LobbyView
     * @param controller
     */
    public LobbyView(LobbyController controller) {
        initComponents();
        this.controller=controller; 
        sentListPanel = new JPanel();
        sentListPanel.setLayout(new BoxLayout(sentListPanel, BoxLayout.Y_AXIS));
        
        recvListPanel = new JPanel();
        recvListPanel.setLayout(new BoxLayout(recvListPanel, BoxLayout.Y_AXIS));
        
        sentInvitations = new HashMap<>();
        recvInvitations = new HashMap<>();
        
        playerStatsPanel.setVisible(false);
        leaderboard.setEditable(false);
    }
    
    
    /**
     * Updates the view to display all of the online players
     * @param players a list of strings containing the names of all online players
     */
    public void displayActivePlayers(String[] players){
        playerList.setListData(players);
    }
    
    //When a user rejects an invite, removes from list and
    //sends reject command to controller
    public void reject(String username, RecvInvite inv){
        //remove from recvList
        recvListPanel.remove(inv);
        recvList.getViewport().removeAll();
        recvList.getViewport().add(recvListPanel);
        
        //controller reject
        controller.invite(IN_REJECT_INVITE, username);
    }
    
    //When a user accepts an invite, removes from list and 
    //sends accept command to controller
    public void accept(String username, RecvInvite inv){
        //remove from recvList
        recvListPanel.remove(inv);
        recvList.getViewport().removeAll();
        recvList.getViewport().add(recvListPanel);
        
        //controller accept
        controller.invite(IN_ACCEPT_INVITE, username);   
    }
    
    //When a user clicks another username and invite, 
    //creates invite object in sentList and sends invite command to controller
    private void sendInvite(String username){
        try{
            //Check if user has selected someone to invite
            if(!(username.equals(""))){ //Username must exist for following code to execute
                //new sentinvite
                //add to sentList
                if (sentInvitations.containsKey(username))
                    return; // Avoid sending duplicate invites
                SentInvite invite = new SentInvite(username, this);
                sentInvitations.put(username, invite);           
                
                invite.setPreferredSize(new Dimension(200, 23));
                invite.setMaximumSize(new Dimension(200, 23));
                invite.setMinimumSize(new Dimension(200, 23));

                sentListPanel.add(invite, BorderLayout.NORTH);
                sentList.getViewport().removeAll();
                sentList.getViewport().add(sentListPanel);

                //controller send invite
                controller.invite(IN_SEND_INVITE, username);
            }
        } catch (NullPointerException e){
            System.out.println("Tried inviting when no one existed");
        }
        
    }
    
    //When the user recieves an invite, a new invite is created 
    //and added to the list
    public void receiveInvite(String username){
        System.out.println("VIEW: receiveInvite WAS CALLED");
        //new recvInvite
        RecvInvite invite = new RecvInvite(username, this);
        recvInvitations.put(username, invite); 
        
        invite.setPreferredSize(new Dimension(280, 50));
        invite.setMaximumSize(new Dimension(280, 50));
        invite.setMinimumSize(new Dimension(280, 50));
        
        //add to recvList
        recvListPanel.add(invite, BorderLayout.NORTH);
        recvList.getViewport().removeAll();
        recvList.getViewport().add(recvListPanel);
    }
    
    //When the user clicks cancel invite, this method will remove it from the 
    //UI and send cancel command to controller
    public void cancelInvite(String username, SentInvite inv){
        //remove from sentList
        sentListPanel.remove(inv);
        sentList.getViewport().removeAll();
        sentList.getViewport().add(sentListPanel);
        sentInvitations.remove(username);
        //controller cancel
        controller.invite(IN_CANCEL_INVITE, username);
    }
    
    public void removeSentInvite(String username){
        SentInvite invite = sentInvitations.get(username);
        sentListPanel.remove(invite);
        sentList.getViewport().removeAll();
        sentList.getViewport().add(sentListPanel);
        
        sentInvitations.remove(username);
    }
    
    public void removeRecvInvite(String username){
        RecvInvite invite = recvInvitations.get(username);
        recvListPanel.remove(invite);
        recvList.getViewport().removeAll();
        recvList.getViewport().add(recvListPanel);
        
        recvInvitations.remove(username);
    }
    
    //Adds a game view in a new tab. This is where the player 
    //will continue to play the game
    public void toGame(GameController controller){
        
        tabPane.addTab("Game", controller.getView());
    }
    
    public void endGame(GameView gameView){
        tabPane.remove(gameView);
    }
}
