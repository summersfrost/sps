/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dbcon.DB;
import static dbcon.StudentDao.save;
import formatting.inputFormatting;
import static initializer.displayOps.displayImageStringSource;
import static initializer.validators.validateInputs;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import tablesForm.JpanelLoader;
import tablesForm.studentsList;


/**
 *
 * @author User
 */
public class viewStudent extends javax.swing.JPanel {

    /**
     * Creates new form addStudent
     */

     byte[] qrBytes;
     String studID;
             String username;
    public viewStudent(String studID,String username) {
      this.username=username;
        initComponents();
          this.studID=studID;
             try (Connection con = DB.getConnection();
         PreparedStatement preparedStatement = con.prepareStatement("SELECT firstName, middleName, lastName, extension, email, mobileNo, qr, addedby, dateAdded, timeAdded, rfid, img, yearlvl FROM students WHERE studentID = ?")) {
        preparedStatement.setString(1, studID);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String middleName = resultSet.getString("middleName");
                String lastName = resultSet.getString("lastName");
                String extension = resultSet.getString("extension");
                String email = resultSet.getString("email");
                String mobileNo = resultSet.getString("mobileNo");
                String addedby = resultSet.getString("addedby");
                String dateAdded = resultSet.getString("dateAdded");
                String timeAdded = resultSet.getString("timeAdded");
                // Corrected: Get image as a byte array
                byte[] imgBytes = resultSet.getBytes("img");
                // Corrected: Get year level
                
                
                String yrLevel = resultSet.getString("yearlvl");

                String fullName = firstName + " " + (middleName != null ? middleName + " " : "") + lastName + " " + extension;
                String addingDetails = "Added by: " + addedby + " " + dateAdded + " @ " + timeAdded;

                // Corrected: Display image
                if (imgBytes != null) {
                    displayImage(imgBytes);
                } else {
                    System.out.println("No image found for student ID: " + studID);
                }

                ID.setText(studID);
                studName.setText(fullName);
                studentEmail.setText(email);
                studMobNo.setText(mobileNo);
                addedBy.setText(addingDetails);
                yrlvl.setText(yrLevel);

                // Save QR code image
                // Assuming qr is a byte array column, you can directly save it as an image file
              qrBytes = resultSet.getBytes("qr");
    
            } else {
                System.out.println("Student not found.");
            }
        }
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    }

    }
    private void displayImage(byte[] img) {
    if (img == null || img.length == 0) {
        System.out.println("Error: Image data is null or empty");
        return;
    }
    
    try {
        ImageIcon imageIcon = new ImageIcon(img);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        imageLabel.setIcon(scaledImageIcon);
    } catch (IllegalArgumentException | NullPointerException e) {
        System.out.println("Error displaying image: " + e.getMessage());
    }
    }
    private void saveConvertedImage(BufferedImage toSave,String saveDirectory,String fileName) throws IOException {
        if (qrBytes != null) {
        // Convert byte array to BufferedImage
        ByteArrayInputStream bis = new ByteArrayInputStream(qrBytes);
        BufferedImage qrImage = ImageIO.read(bis);

        // Resize the image to 200x200
        BufferedImage resizedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(qrImage, 0, 0, 200, 200, null);

        // Now you have your resized image in resizedImage variable
    } else {
        // Handle case where qrBytes is null
    }
    File directory = new File(saveDirectory);
    if (!directory.exists()) {
        directory.mkdirs();
    }
       

    try {
        // Where to save the image andn its filename
             String fullPath = Paths.get(saveDirectory, fileName).toString();
        // Save the grayscale image
        ImageIO.write(toSave, "jpg", new File(fullPath));

 System.out.println("Save file");
    } catch (IOException e) {
        e.printStackTrace();
    }

    
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();
        ID = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        studName = new javax.swing.JLabel();
        studentEmail = new javax.swing.JLabel();
        studMobNo = new javax.swing.JLabel();
        addedBy = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        yrlvl = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        showQR = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.darkGray, java.awt.Color.gray));

        imageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        ID.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        ID.setForeground(new java.awt.Color(0, 0, 0));
        ID.setText("Student ID:");

        jLabel4.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Name:");

        jLabel5.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Email:");

        jLabel7.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Mobile No.:");

        jLabel8.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Student ID:");

        studName.setBackground(new java.awt.Color(102, 204, 255));
        studName.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        studName.setForeground(new java.awt.Color(0, 0, 0));
        studName.setText("Name");

        studentEmail.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        studentEmail.setForeground(new java.awt.Color(0, 0, 0));
        studentEmail.setText("Email");

        studMobNo.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        studMobNo.setForeground(new java.awt.Color(0, 0, 0));
        studMobNo.setText("Mobile No");

        addedBy.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        addedBy.setForeground(new java.awt.Color(0, 0, 0));
        addedBy.setText("Added by:");

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("BACK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        yrlvl.setBackground(new java.awt.Color(102, 204, 255));
        yrlvl.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        yrlvl.setForeground(new java.awt.Color(0, 0, 0));
        yrlvl.setText("Year Level");

        jLabel9.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Year Level:");

        jLabel1.setFont(new java.awt.Font("Ebrima", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-identity-100.png"))); // NOI18N
        jLabel1.setText("Student Information");

        showQR.setText("Save QR");
        showQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showQRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(showQR, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(studName, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(studentEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(studMobNo, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(yrlvl, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ID, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(addedBy, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(ID))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(studName))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(yrlvl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(studentEmail))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(studMobNo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addedBy)
                        .addGap(44, 44, 44))
                    .addComponent(showQR, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents
    JpanelLoader jpload = new JpanelLoader();
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
                 studentsList cus = new studentsList(username);
        jpload.jPanelLoader(this, cus);
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void showQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showQRActionPerformed
        // TODO add your handling code here:
        //   saveQRCode(qrBytes,studID,200,200);
                String imgpath_name = "C:\\Downloads\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\img\\img" ;

        // Only save the image if a new image has been selected

        saveDisplayedImageToFile(imgpath_name, studID);
        JOptionPane.showMessageDialog(this, "Qr Code Save Successfully to C:\\Downloads\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\img\\img ");
    }//GEN-LAST:event_showQRActionPerformed
private void saveDisplayedImageToFile(String saveDirectory, String fileName) {
   try {
        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Convert byte array to InputStream
        InputStream inputStream = new ByteArrayInputStream(qrBytes);

        // Read bytes from InputStream and create an image
        BufferedImage originalImage = ImageIO.read(inputStream);

        // Close the InputStream
        inputStream.close();

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Resize the image to 720x720 pixels
        Image scaledImage = originalImage.getScaledInstance(1080, 1080, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(1080, 1080, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

        // Construct the full path to save the image
        String savePath = Paths.get(saveDirectory, fileName + ".jpg").toString(); // Add ".jpg" extension

        // Write the resized image to the file
        File outputFile = new File(savePath);
        ImageIO.write(resizedImage, "jpg", outputFile);

        System.out.println("Image saved to: " + savePath);
    } catch (IOException ex) {
        Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
    }
}

           

           
           
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ID;
    private javax.swing.JLabel addedBy;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton showQR;
    private javax.swing.JLabel studMobNo;
    private javax.swing.JLabel studName;
    private javax.swing.JLabel studentEmail;
    private javax.swing.JLabel yrlvl;
    // End of variables declaration//GEN-END:variables
}
