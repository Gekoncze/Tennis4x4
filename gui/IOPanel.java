
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author gekoncze
 */
public class IOPanel extends javax.swing.JPanel {
    
    private static final int IO_ROW_COUNT = 10;

    private final LinkedList<String> lines = new LinkedList<>();
    private char ch = ':';
    
    public IOPanel() {
        initComponents();
    }
    
    public void log(String str){
        lines.addLast(ch + " " + str);
        
        if(ch == ':') ch = '|';
        else ch = ':';
        
        if(lines.size() > IO_ROW_COUNT){
            lines.removeFirst();
        }
        
        jPanel.repaint();
    }
    
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, jPanel.getWidth(), jPanel.getHeight());
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        int y = 1;
        for(String line : lines){
            g.drawString(line, 0, 14*y);
            y++;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                draw(g);
            }
        };

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new java.awt.BorderLayout());
        add(jPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel;
    // End of variables declaration//GEN-END:variables
}