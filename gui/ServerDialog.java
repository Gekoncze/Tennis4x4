
package gui;

import core.GameException;
import core.GameListener;
import core.Tennis4x4;
import core.network.Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author gekoncze
 */
public class ServerDialog extends javax.swing.JDialog {
    private static ServerDialog dialog;
    private final Timer timer;
    private Server server;
    
    private final ActionListener stepEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(server != null){
                try {
                    server.update();
                } catch (IOException e) {
                    ioException(e);
                }
            }
        }
    };
    
    private final GameListener gameEvent = new GameListener() {
        @Override
        public void event(String message) {
            log(message);
        }
    };
    
    private final GameListener inputEvent = new GameListener() {
        @Override
        public void event(String message) {
            logInputLine(message);
        }
    };
    
    private final GameListener outputEvent = new GameListener() {
        @Override
        public void event(String message) {
            logOutputLine(message);
        }
    };
    
    public static void create(JFrame parent){
        dialog = new ServerDialog(parent, false);
        dialog.updateLocks();
    }
    
    public static void ssetVisible(boolean value){
        if(dialog != null) dialog.setVisible(value);
    }
    
    public static void destroy(){
        if(dialog != null){
            dialog.cclose();
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
        }
    }
    
    public static int getStepId(){
        if(dialog == null || dialog.server == null){
            return 0;
        } else {
            return dialog.server.getStepId();
        }
    }

    private ServerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        timer = new Timer(Tennis4x4.STEP_TIME, stepEvent);
        timer.start();
    }
    
    private int getPort() {
        if(jCheckBoxPort.isSelected()){
            return Tennis4x4.DEFAULT_PORT;
        } else {
            return Integer.parseInt(jTextFieldPort.getText());
        }
    }
    
    private int getPlayer(){
        if(jRadioButton1.isSelected()){
            return 0;
        } else if(jRadioButton2.isSelected()){
            return 1;
        } else if(jRadioButton3.isSelected()){
            return 2;
        } else {
            return 3;
        }
    }
    
    private void ccreate(){
        if(server == null){
            try {
                server = new Server(getPort());
                server.setGameListener(gameEvent);
                updateLogging();
                log("Server created!");
                updateLocks();
            } catch (SocketException ex) {
                log("Could not create server: " + ex.getMessage());
            }
        }
    }
    
    private void cclose(){
        if(server != null){
            server.close();
            server = null;
            log("Server closed!");
            updateLocks();
        }
    }
    
    private void kick(){
        if(server != null){
            try {
                server.kickPlayer(getPlayer());
            } catch (IOException e) {
                ioException(e);
            }
        }
    }
    
    private void start(){
        if(server == null) return;
        try {
            server.startNewGame();
            log("Server started!");
        } catch (IOException e) {
            log("Could not start new game: " + e.getMessage());
        }
    }
    
    private void updateLogging(){
        if(server == null) return;
        if(jCheckBoxLogging.isSelected()){
            server.setInputListener(inputEvent);
            server.setOutputListener(outputEvent);
        } else {
            server.setInputListener(null);
            server.setOutputListener(null);
        }
    }
    
    private void gameException(GameException e){
        log("An error occured. Shutting down... (" + e.getMessage() + ")");
        cclose();
    }
    
    private void ioException(IOException e) {
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
    
    private void updateLocks(){
        boolean useDefaultPort = jCheckBoxPort.isSelected();
        jLabelPort.setEnabled(!useDefaultPort);
        jTextFieldPort.setEnabled(!useDefaultPort);
        
        boolean created = server != null;
        jButtonCreate.setEnabled(!created);
        jButtonClose.setEnabled(created);
        jButtonStart.setEnabled(created);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupPlayers = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxPort = new javax.swing.JCheckBox();
        jCheckBoxLogging = new javax.swing.JCheckBox();
        jLabelPort = new javax.swing.JLabel();
        jTextFieldPort = new javax.swing.JTextField();
        jButtonClose = new javax.swing.JButton();
        jButtonCreate = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButtonKick = new javax.swing.JButton();
        jButtonStart = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        iOPanelInput = new gui.IOPanel();
        iOPanelOutput = new gui.IOPanel();

        setTitle("Server");
        setPreferredSize(new java.awt.Dimension(320, 512));
        getContentPane().setLayout(new java.awt.GridLayout(3, 1, 4, 4));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jPanel1.setLayout(new java.awt.BorderLayout(4, 4));

        jPanel2.setLayout(new java.awt.GridLayout(3, 2, 4, 4));

        jCheckBoxPort.setSelected(true);
        jCheckBoxPort.setText("Default port");
        jCheckBoxPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPortActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBoxPort);

        jCheckBoxLogging.setText("P. logging");
        jCheckBoxLogging.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLoggingActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBoxLogging);

        jLabelPort.setText("Port");
        jLabelPort.setEnabled(false);
        jPanel2.add(jLabelPort);

        jTextFieldPort.setText("56789");
        jTextFieldPort.setEnabled(false);
        jPanel2.add(jTextFieldPort);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonClose);

        jButtonCreate.setText("Create");
        jButtonCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonCreate);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridLayout(5, 1, 4, 4));

        buttonGroupPlayers.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Player 1");
        jPanel3.add(jRadioButton1);

        buttonGroupPlayers.add(jRadioButton2);
        jRadioButton2.setText("Player 2");
        jPanel3.add(jRadioButton2);

        buttonGroupPlayers.add(jRadioButton3);
        jRadioButton3.setText("Player 3");
        jPanel3.add(jRadioButton3);

        buttonGroupPlayers.add(jRadioButton4);
        jRadioButton4.setText("Player 4");
        jPanel3.add(jRadioButton4);

        jButtonKick.setText("Kick");
        jButtonKick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKickActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonKick);

        jPanel1.add(jPanel3, java.awt.BorderLayout.EAST);

        jButtonStart.setText("Start");
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonStart, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel1);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jTextAreaLog.setColumns(20);
        jTextAreaLog.setRows(5);
        jScrollPane1.setViewportView(jTextAreaLog);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel4);

        jPanel5.setLayout(new java.awt.GridLayout(1, 2, 4, 4));
        jPanel5.add(iOPanelInput);
        jPanel5.add(iOPanelOutput);

        getContentPane().add(jPanel5);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateActionPerformed
        ccreate();
    }//GEN-LAST:event_jButtonCreateActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        cclose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonKickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKickActionPerformed
        kick();
    }//GEN-LAST:event_jButtonKickActionPerformed

    private void jCheckBoxPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPortActionPerformed
        updateLocks();
    }//GEN-LAST:event_jCheckBoxPortActionPerformed

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        start();
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void jCheckBoxLoggingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLoggingActionPerformed
        updateLogging();
    }//GEN-LAST:event_jCheckBoxLoggingActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupPlayers;
    private gui.IOPanel iOPanelInput;
    private gui.IOPanel iOPanelOutput;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonCreate;
    private javax.swing.JButton jButtonKick;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JCheckBox jCheckBoxLogging;
    private javax.swing.JCheckBox jCheckBoxPort;
    private javax.swing.JLabel jLabelPort;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaLog;
    private javax.swing.JTextField jTextFieldPort;
    // End of variables declaration//GEN-END:variables
}
