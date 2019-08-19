
package gui;

import core.Game;
import core.GameException;
import core.GameListener;
import core.Tennis4x4;
import core.network.Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author gekoncze
 */
public class ClientDialog extends javax.swing.JDialog {
    private static ClientDialog dialog;
    private Client client;
    private final Timer timer;
    
    private final ActionListener stepEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(client != null){
                try {
                    client.update();
                } catch (GameException e) {
                    gameException(e);
                } catch (IOException e) {
                    ioException(e);
                }
            }
        }
    };
    
    private final GameListener gameEvent = new GameListener() {
        @Override
        public void event(String message) {
            dialog.log(message);
        }
    };
    
    private final GameListener inputEvent = new GameListener() {
        @Override
        public void event(String message) {
            dialog.logInputLine(message);
        }
    };
    
    private final GameListener outputEvent = new GameListener() {
        @Override
        public void event(String message) {
            dialog.logOutputLine(message);
        }
    };
    
    public static void create(JFrame parent){
        if(dialog == null){
            dialog = new ClientDialog(parent, false);
        }
    }
    
    public static void ssetVisible(boolean value){
        if(dialog != null){
            dialog.setVisible(value);
        }
    }
    
    public static void destroy(){
        if(dialog != null){
            dialog.disconnect();
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
        }
    }
    
    public static Game getGame(){
        if(dialog == null) return null;
        if(dialog.client == null){
            return null;
        } else {
            return dialog.client.getGame();
        }
    }
    
    public static int ggetPlayer(){
        if(dialog == null) return 0;
        return dialog.getPlayer();
    }
    
    public static boolean isShowStepIds(){
        if(dialog == null) return false;
        else return dialog.jCheckBoxShowStep.isSelected();
    }
    
    public static void sendMoveLeft(){
        if(dialog == null) return;
        if(dialog.client != null){
            try {
                dialog.client.sendPlayerActionMoveLeft(dialog.getPlayer());
            } catch (IOException e) {
                dialog.ioException(e);
            } catch (GameException e) {
                dialog.gameException(e);
            }
        }
    }
    
    public static void sendMoveRight(){
        if(dialog == null) return;
        if(dialog.client != null){
            try {
                dialog.client.sendPlayerActionMoveRight(dialog.getPlayer());
            } catch (IOException e) {
                dialog.ioException(e);
            } catch (GameException e) {
                dialog.gameException(e);
            }
        }
    }
    
    public static void sendWave(){
        if(dialog == null) return;
        if(dialog.client != null){
            try {
                dialog.client.sendPlayerActionWave(dialog.getPlayer());
            } catch (IOException e) {
                dialog.ioException(e);
            } catch (GameException e) {
                dialog.gameException(e);
            }
        }
    }
    
    public static int getStepId(){
        if(dialog == null || dialog.client == null){
            return 0;
        } else {
            return dialog.client.getStepId();
        }
    }
    
    private ClientDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        timer = new Timer(Tennis4x4.STEP_TIME, stepEvent);
        timer.start();
    }
    
    private void updateLocks(){
        boolean defaultPort = jCheckBoxPort.isSelected();
        boolean connected = client != null;
        
        jLabelPort.setEnabled(!defaultPort);
        jTextFieldPort.setEnabled(!defaultPort);
        
        jButtonConnect.setEnabled(!connected);
        jButtonDisconnect.setEnabled(connected);
        jLabelPlayer.setEnabled(connected);
        jComboBoxPlayer.setEnabled(connected);
        jButtonJoin.setEnabled(connected);
    }
    
    private void connect(){
        if(client == null){
            try {
                client = new Client(InetAddress.getByName(getAddress()), getPort());
                client.setGameListener(gameEvent);
                updateLogging();
                updateLocks();
                log("Port setup complete!");
            } catch (UnknownHostException | SocketException e) {
                log("Could not connect: " + e.getMessage());
            }
        }
    }
    
    private void disconnect(){
        if(client != null){
            client.close();
            client = null;
            updateLocks();
        }
    }
    
    private void join(){
        if(client != null) {
            try {
                client.sendPlayerRegistration(getPlayer());
            } catch (IOException e) {
                ioException(e);
            } catch (GameException e) {
                gameException(e);
            }
        }
    }
    
    private void updateLogging(){
        if(client == null) return;
        if(jCheckBoxLogging.isSelected()){
            client.setInputListener(inputEvent);
            client.setOutputListener(outputEvent);
        } else {
            client.setInputListener(null);
            client.setOutputListener(null);
        }
    }
    
    public void gameException(GameException e){
        log("An error occured. Disconnecting... (" + e.getMessage() + ")");
        disconnect();
    }
    
    public void ioException(IOException e) {
        // nothing to see, no health-kit here
        System.out.println("IOException: " + e.getMessage());
    }
    
    private void log(String str){
        jTextAreaLog.append(Other.currentTimeAsString() + " " + str + "\n");
    }
    
    private void logInputLine(String line){
        iOPanelInput.log(line);
    }
    
    private void logOutputLine(String line){
        iOPanelOutput.log(line);
    }
    
    private String getAddress(){
        return jTextFieldAddress.getText();
    }
    
    private int getPort(){
        if(jCheckBoxPort.isSelected()){
            return Tennis4x4.DEFAULT_PORT;
        } else {
            return Integer.parseInt(jTextFieldPort.getText());
        }
    }
    
    private int getPlayer(){
        switch(jComboBoxPlayer.getSelectedItem().toString()){
            case "Player 1":
                return 0;
            case "Player 2":
                return 1;
            case "Player 3":
                return 2;
            case "Player 4":
                return 3;
        }
        
        return 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelAddress = new javax.swing.JLabel();
        jTextFieldAddress = new javax.swing.JTextField();
        jCheckBoxPort = new javax.swing.JCheckBox();
        jCheckBoxLogging = new javax.swing.JCheckBox();
        jLabelPort = new javax.swing.JLabel();
        jTextFieldPort = new javax.swing.JTextField();
        jButtonConnect = new javax.swing.JButton();
        jButtonDisconnect = new javax.swing.JButton();
        jLabelPlayer = new javax.swing.JLabel();
        jComboBoxPlayer = new javax.swing.JComboBox();
        jCheckBoxShowStep = new javax.swing.JCheckBox();
        jButtonJoin = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jPanelLogging = new javax.swing.JPanel();
        iOPanelInput = new gui.IOPanel();
        iOPanelOutput = new gui.IOPanel();

        setTitle("Client");
        setPreferredSize(new java.awt.Dimension(320, 512));
        getContentPane().setLayout(new java.awt.GridLayout(3, 1, 4, 4));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jPanel1.setLayout(new java.awt.GridLayout(6, 2, 4, 4));

        jLabelAddress.setText("Address");
        jPanel1.add(jLabelAddress);

        jTextFieldAddress.setText("localhost");
        jPanel1.add(jTextFieldAddress);

        jCheckBoxPort.setSelected(true);
        jCheckBoxPort.setText("Use default port");
        jCheckBoxPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPortActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBoxPort);

        jCheckBoxLogging.setText("Packet logging");
        jCheckBoxLogging.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLoggingActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBoxLogging);

        jLabelPort.setText("Port");
        jLabelPort.setEnabled(false);
        jPanel1.add(jLabelPort);

        jTextFieldPort.setText("56789");
        jTextFieldPort.setEnabled(false);
        jPanel1.add(jTextFieldPort);

        jButtonConnect.setText("Connect");
        jButtonConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConnectActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonConnect);

        jButtonDisconnect.setText("Disconnect");
        jButtonDisconnect.setEnabled(false);
        jButtonDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisconnectActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDisconnect);

        jLabelPlayer.setText("Player");
        jLabelPlayer.setEnabled(false);
        jPanel1.add(jLabelPlayer);

        jComboBoxPlayer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Player 1", "Player 2", "Player 3", "Player 4" }));
        jComboBoxPlayer.setEnabled(false);
        jPanel1.add(jComboBoxPlayer);

        jCheckBoxShowStep.setText("Show step IDs");
        jPanel1.add(jCheckBoxShowStep);

        jButtonJoin.setText("Join");
        jButtonJoin.setEnabled(false);
        jButtonJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJoinActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonJoin);

        getContentPane().add(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jTextAreaLog.setEditable(false);
        jTextAreaLog.setColumns(20);
        jTextAreaLog.setRows(5);
        jScrollPane1.setViewportView(jTextAreaLog);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2);

        jPanelLogging.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jPanelLogging.setLayout(new java.awt.GridLayout(1, 2, 4, 4));
        jPanelLogging.add(iOPanelInput);
        jPanelLogging.add(iOPanelOutput);

        getContentPane().add(jPanelLogging);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPortActionPerformed
        updateLocks();
    }//GEN-LAST:event_jCheckBoxPortActionPerformed

    private void jButtonConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConnectActionPerformed
        connect();
    }//GEN-LAST:event_jButtonConnectActionPerformed

    private void jButtonDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisconnectActionPerformed
        disconnect();
    }//GEN-LAST:event_jButtonDisconnectActionPerformed

    private void jButtonJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJoinActionPerformed
        join();
    }//GEN-LAST:event_jButtonJoinActionPerformed

    private void jCheckBoxLoggingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLoggingActionPerformed
        updateLogging();
    }//GEN-LAST:event_jCheckBoxLoggingActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.IOPanel iOPanelInput;
    private gui.IOPanel iOPanelOutput;
    private javax.swing.JButton jButtonConnect;
    private javax.swing.JButton jButtonDisconnect;
    private javax.swing.JButton jButtonJoin;
    private javax.swing.JCheckBox jCheckBoxLogging;
    private javax.swing.JCheckBox jCheckBoxPort;
    private javax.swing.JCheckBox jCheckBoxShowStep;
    private javax.swing.JComboBox jComboBoxPlayer;
    private javax.swing.JLabel jLabelAddress;
    private javax.swing.JLabel jLabelPlayer;
    private javax.swing.JLabel jLabelPort;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelLogging;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaLog;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldPort;
    // End of variables declaration//GEN-END:variables
}
