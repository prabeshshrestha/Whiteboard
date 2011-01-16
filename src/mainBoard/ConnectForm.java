/*
 * Connect.java
 *
 * Created on April 27, 2007, 1:10 AM
 */
package mainBoard;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ConnectForm extends javax.swing.JFrame {

    mainGUI mainBoard;

    /**
     * Creates new form Connect
     * @param app Parent JFrame
     */
    public ConnectForm(mainGUI app) {

        mainBoard = app;
        initComponents();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //this.setUndecorated(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ConnectForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ConnectForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ConnectForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        remoteServerLabel = new javax.swing.JLabel();
        remoteServerTexttField = new javax.swing.JTextField();
        portNumberLabel = new javax.swing.JLabel();
        portNumberTexttField = new javax.swing.JTextField();
        userNameLabel = new javax.swing.JLabel();
        userNameTexttField = new javax.swing.JTextField();
        connectToServerButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Connect Form");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        remoteServerLabel.setText("Remote Server");

        portNumberLabel.setText("Port Number");

        userNameLabel.setText("User Name");

        connectToServerButton.setText("Connect To Server");
        connectToServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectToServerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(portNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userNameTexttField, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(remoteServerTexttField, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(portNumberTexttField, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)))
                    .addComponent(remoteServerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(110, Short.MAX_VALUE)
                .addComponent(connectToServerButton)
                .addGap(119, 119, 119))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(remoteServerLabel)
                    .addComponent(remoteServerTexttField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portNumberLabel)
                    .addComponent(portNumberTexttField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userNameLabel)
                    .addComponent(userNameTexttField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(connectToServerButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void connectToServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectToServerButtonActionPerformed
        /*info provided by the user(Server address,portno,username) correct or not*/
        boolean fieldCorrectOrNot = true;

        int port_no = 0;

        /* gets the server address,username and portnumber form the user*/
        String server_addr = remoteServerTexttField.getText();
        String userName = userNameTexttField.getText();
        String str = portNumberTexttField.getText();

        /*trim the textfield for spaces infront and after the required word*/
        userName.trim();
        server_addr.trim();
        str.trim();

        /*check the correctness of the fields entered by the user*/
        if ((userName.length() <= 0) || (server_addr.length() <= 0) || str.length() <= 0) {
            JOptionPane.showMessageDialog(null, "All Fields Required!");
            remoteServerTexttField.setText("");
            portNumberTexttField.setText("");
            userNameTexttField.setText("");
            fieldCorrectOrNot = false;
        }
        /*checks if the portno is digit or not*/
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                JOptionPane.showMessageDialog(null, "Invalid Port Number, Please Input again!");
                fieldCorrectOrNot = false;
                break;
            }
        }
        /*if the provided information are correct*/
        if (fieldCorrectOrNot == true) {
            port_no = Integer.parseInt(portNumberTexttField.getText());
            /*connect the mainboard to the server */
            mainBoard.ConnectServer(server_addr, port_no, userName);
            this.setVisible(false);
            this.mainBoard.setVisible(true);
        }
}//GEN-LAST:event_connectToServerButtonActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectToServerButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel portNumberLabel;
    private javax.swing.JTextField portNumberTexttField;
    private javax.swing.JLabel remoteServerLabel;
    private javax.swing.JTextField remoteServerTexttField;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JTextField userNameTexttField;
    // End of variables declaration//GEN-END:variables
}
