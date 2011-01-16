/*
 * WhiteBoardHelp.java
 * Form to be displayed when user clicks on Help in the main program.
 * Created on April 21, 2007, 10:11 PM
 */
package mainBoard;

public class WhiteBoardHelp extends javax.swing.JFrame {

    /** Creates new form WhiteBoardHelp */
    public WhiteBoardHelp() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        helpScrollPane = new javax.swing.JScrollPane();
        descriptionPanel = new javax.swing.JTextPane();
        softwareNameField = new javax.swing.JTextField();

        setTitle("Help");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        descriptionPanel.setBackground(new java.awt.Color(204, 204, 255));
        descriptionPanel.setFont(new java.awt.Font("Comic Sans MS", 1, 14));
        descriptionPanel.setForeground(new java.awt.Color(255, 255, 255));
        descriptionPanel.setText("\n\nThis Application can be used by multiple clients simulaneously.\nEach client can draw different images on the WhiteBoard and the changes are visible to all the clients using the WhiteBoard and connected to the same server.\nThe client can \n\t1) Draw Images and Freehand Design \n\t2) Color them\n\t3) Perform Cut, Copy, Paste and Delete actions\n\t4) Can Open and Save the objects\n\nThis eWhiteBoard is Easy to Use Open Source software.\n\n\nTHANKS for Downloading.\nDeveloped By:\nPrabesh Shrestha");
        descriptionPanel.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        descriptionPanel.setEnabled(false);
        helpScrollPane.setViewportView(descriptionPanel);

        softwareNameField.setBackground(new java.awt.Color(204, 204, 255));
        softwareNameField.setEditable(false);
        softwareNameField.setFont(new java.awt.Font("Tahoma", 0, 32));
        softwareNameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        softwareNameField.setText("eWhiteBoard");
        softwareNameField.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        softwareNameField.setEnabled(false);
        softwareNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                softwareNameFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(softwareNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(helpScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(softwareNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helpScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
// TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
// TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void softwareNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_softwareNameFieldActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_softwareNameFieldActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane descriptionPanel;
    private javax.swing.JScrollPane helpScrollPane;
    private javax.swing.JTextField softwareNameField;
    // End of variables declaration//GEN-END:variables
}
