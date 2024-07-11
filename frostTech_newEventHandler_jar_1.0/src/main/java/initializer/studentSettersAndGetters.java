/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package initializer;

import dbcon.DB;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class studentSettersAndGetters {

    String addedBy;
    String firstName, middleName, lastName, extension, email, mobileNo, addedby, dateAdded, timeAdded, rfid, yrLevel;
    String imgname, oldStudentID, newStudentiD;
    byte[] imageData, qrCodeImageData;
    byte[] qrBytes;

    public studentSettersAndGetters(String studentID) {

        try (Connection con = DB.getConnection(); PreparedStatement preparedStatement = con.prepareStatement("SELECT firstName, middleName, lastName, extension, email, mobileNo, qr, addedby, dateAdded, timeAdded, rfid, img, yearlvl FROM students WHERE studentID = ?")) {
            preparedStatement.setString(1, studentID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    firstName = resultSet.getString("firstName");
                    middleName = resultSet.getString("middleName");
                    lastName = resultSet.getString("lastName");
                    extension = resultSet.getString("extension");
                    email = resultSet.getString("email");
                    mobileNo = resultSet.getString("mobileNo");
                    addedby = resultSet.getString("addedby");
                    dateAdded = resultSet.getString("dateAdded");
                    timeAdded = resultSet.getString("timeAdded");
                    rfid = resultSet.getString("rfid");
                    // Corrected: Get image as a byte array
                    imageData = resultSet.getBytes("img");
                    // Corrected: Get year level

                     yrLevel = resultSet.getString("yearlvl");
                    this.qrCodeImageData = resultSet.getBytes("qr");
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
  // Getters for addedBy
    public String getAddedBy() {
        return addedBy;
    }

    // Getters for firstName
    public String getFirstName() {
        return firstName;
    }

    // Getters for middleName
    public String getMiddleName() {
        return middleName;
    }

    // Getters for lastName
    public String getLastName() {
        return lastName;
    }

    // Getters for extension
    public String getExtension() {
        return extension;
    }

    // Getters for email
    public String getEmail() {
        return email;
    }

    // Getters for mobileNo
    public String getMobileNo() {
        return mobileNo;
    }

    // Getters for addedby
    public String getAddedby() {
        return addedby;
    }

    // Getters for dateAdded
    public String getDateAdded() {
        return dateAdded;
    }

    // Getters for timeAdded
    public String getTimeAdded() {
        return timeAdded;
    }

    // Getters for rfid
    public String getRfid() {
        return rfid;
    }

    // Getters for yrLevel
    public String getYrLevel() {
        return yrLevel;
    }

    // Getters for imgname
    public String getImgname() {
        return imgname;
    }

    // Getters for oldStudentID
    public String getOldStudentID() {
        return oldStudentID;
    }

    // Getters for newStudentiD
    public String getNewStudentiD() {
        return newStudentiD;
    }

    // Getters for imageData
    public byte[] getImageData() {
        return imageData;
    }

    // Getters for qrCodeImageData
    public byte[] getQrCodeImageData() {
        return qrCodeImageData;
    }

    // Getters for qrBytes
    public byte[] getQrBytes() {
        return qrBytes;
    }

}
