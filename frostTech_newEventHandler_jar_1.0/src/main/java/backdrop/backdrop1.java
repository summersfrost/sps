/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backdrop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.ImageIcon;

/**
 *
 * @author RavenPC
 */
public class backdrop1 extends javax.swing.JPanel {

    /**
     * Creates new form Panel1
     */
    public backdrop1() {
        initComponents();
                jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Background.png")));
        jLabel1.setOpaque(true);

        // Set BorderLayout as the layout manager for this panel
        setLayout(new BorderLayout());

        // Add the JLabel containing the image to the CENTER of the panel
        add(jLabel1, BorderLayout.CENTER);
            addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Update the image size when the frame is resized
                updateImageSize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }
private void updateImageSize() {
    // Get the size of the JFrame's content pane
    Dimension frameSize = this.getParent().getSize();

    // Load the original image
    ImageIcon originalIcon = new ImageIcon(getClass().getResource("/icons/Background.png"));

    // Scale the original image to match the new frame size
    Image scaledImage = originalIcon.getImage().getScaledInstance(frameSize.width, frameSize.height, Image.SCALE_SMOOTH);

    // Create a new ImageIcon with the scaled image and set it as the label's icon
    jLabel1.setIcon(new ImageIcon(scaledImage));
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/COMPESA Cover photo.png"))); // NOI18N
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 721, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 408, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
