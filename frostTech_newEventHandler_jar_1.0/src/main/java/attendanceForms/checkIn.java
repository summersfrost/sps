/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceForms;

import dbcon.DB;
import dbcon.attendanceDao;
import static initializer.displayOps.displayImageByteSource;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author RavenPC
 */
public class checkIn extends javax.swing.JPanel {

    /**
     * Creates new form Panel1
     */
    Timer scanTimer;
    String eventName, fullName, username;

    public checkIn(int day,String eventName, String username) {
 
      this.eventName = eventName;
      initComponents();
      if (day==1){
        program.setText("Morning " +eventName + "Time In");
      
      }else if(day==2){
        program.setText("Afternoon " +eventName + "Time In");
      }else{
        program.setText("Night " +eventName + "Time In");}
          

      this.username = username;
      this.eventName = eventName;
      this.scanTimer = new Timer(100, new ActionListener() {
        StringBuilder scannedValue = new StringBuilder();
        String lastInput = ""; // Store the previous input to detect changes

        @Override
        public void actionPerformed(ActionEvent e) {
          String currentInput = rfid.getText();

          if (!currentInput.equals(lastInput)) {
            lastInput = currentInput;
            scanTimer.restart(); // Reset the timer if input changes
            return; // Wait for the next timer tick to process complete value
          }

          if (!currentInput.isEmpty()) {
            scannedValue.append(currentInput);

            // Clear the text field
            rfid.setText("");

            // Restart the timer after appending input
            scanTimer.restart();
          } else if (scannedValue.length() > 0) {
            // No new input, process the complete scanned value
            String completeScannedValue = scannedValue.toString();
            System.out.println("Scanned RFID: " + completeScannedValue);

            try {
              Connection con = DB.getConnection();
              String query = "SELECT studentID, firstName, middleName, lastName, extension, img,yearlvl FROM activestudent WHERE rfid = ?";
              PreparedStatement preparedStatement = con.prepareStatement(query);
              preparedStatement.setString(1, completeScannedValue);

              ResultSet resultSet = preparedStatement.executeQuery();

              if (resultSet.next()) {
                String studID = resultSet.getString("studentID");

                String firstName = resultSet.getString("firstName");
                String middleName = resultSet.getString("middleName");
                String lastName = resultSet.getString("lastName");
                String extension = resultSet.getString("extension");
                byte[] img = resultSet.getBytes("img");
                String yr = resultSet.getString("yearlvl");

                fullName = firstName + " " + (middleName != null ? middleName + " " : "") + lastName + " " + extension;

      
        displayImageByteSource1(img, imageLabel);
                studentID.setText(studID);
                studName.setText(fullName);
                yearLevel.setText(yr);
                System.out.println("Student Info: " + fullName);
                
                int rowsAffected = attendanceDao.save(completeScannedValue, username, eventName,yr);

                if (rowsAffected > 0) {
                  jLabel6.setText(fullName + "Timed-In Successfully");
                } else {
                  jLabel6.setText("Student already Timed-In");
                }

              } else {
                studentID.setText(" ");
                studName.setText(" ");
                yearLevel.setText(" ");
                imageLabel.setIcon(null); // Clear any previous image
                jLabel6.setText("Student Is not Registered.");
              }

              con.close();
            } catch (SQLException ex) {
           String error=ex.getMessage();
          JOptionPane.showMessageDialog(null, error);
            }

            
            scannedValue.setLength(0); // Clear the buffer
            lastInput = ""; // Reset lastInput for the next scan
          }
        }
      });


      Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          // Update the label with the current time
          SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss ");
          String currentTime = sdf.format(new Date());
          timeLabel.setText(currentTime);
        }
      });

      timer.start();

    }
    public JTextField getRfid() {
      return rfid;
    }
    public void displayImage(String imagePath) {
      if (imagePath != null && !imagePath.isEmpty()) {
        File imageFile = new File(imagePath);

        if (imageFile.exists() && !imageFile.isDirectory()) {
          ImageIcon imageIcon = new ImageIcon(imagePath);
          Image image = imageIcon.getImage();
          Image scaledImage = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
          imageIcon = new ImageIcon(scaledImage);
          imageLabel.setText("");
          imageLabel.setIcon(imageIcon);
        } else {
          imageLabel.setIcon(null); // Clear any previous image
          imageLabel.setText("No image found");
        }
      } else {
        imageLabel.setIcon(null); // Clear any previous image
        imageLabel.setText("No image found");
      }
    }
              public static void displayImageByteSource1(byte[] img, JLabel imageLabel) {
        if (img == null || img.length == 0) {
            System.out.println("Error: Image data is null or empty");
            return;
        }

        try {
            ImageIcon imageIcon = new ImageIcon(img);
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(310, 317, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(scaledImageIcon);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("Error displaying image: " + e.getMessage());
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

        program = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        studentID = new javax.swing.JLabel();
        yearLevel = new javax.swing.JLabel();
        studName = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rfid = new javax.swing.JTextField();

        setBackground(new java.awt.Color(153, 255, 255));
        setForeground(new java.awt.Color(0, 153, 255));

        program.setBackground(new java.awt.Color(0, 51, 255));
        program.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        program.setForeground(new java.awt.Color(0, 0, 0));
        program.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        program.setText("   EventName");
        program.setOpaque(true);

        imageLabel.setText("Image not Found");
        imageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        timeLabel.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        timeLabel.setForeground(new java.awt.Color(0, 0, 0));
        timeLabel.setText("Time:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Student ID:");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Student Name:");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Year Level:");

        studentID.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        studentID.setForeground(new java.awt.Color(0, 0, 0));

        yearLevel.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        yearLevel.setForeground(new java.awt.Color(0, 0, 0));

        studName.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        studName.setForeground(new java.awt.Color(0, 0, 0));

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));

        rfid.setBackground(new java.awt.Color(153, 255, 255));
        rfid.setForeground(new java.awt.Color(153, 255, 255));
        rfid.setBorder(null);
        rfid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rfidActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(program, javax.swing.GroupLayout.DEFAULT_SIZE, 1388, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(timeLabel)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(yearLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(studentID, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(studName, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rfid, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(program, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(studentID, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(studName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(yearLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(timeLabel)
                .addGap(78, 78, 78)
                .addComponent(rfid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rfidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rfidActionPerformed
        // TODO add your handling code here:
         if (!scanTimer.isRunning()) {
         scanTimer.start();
                }
   
  
    }//GEN-LAST:event_rfidActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel program;
    public javax.swing.JTextField rfid;
    private javax.swing.JLabel studName;
    private javax.swing.JLabel studentID;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel yearLevel;
    // End of variables declaration//GEN-END:variables
}
