/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class updateStudent {
   
public static String update(String oldStudentID, String newStudentID, String firstName, String middleName, String lastName, String extension, String email, String mobileNo, byte[] qr,   String rfid, byte[] img, String yrLvl) {
    String status = "";
    Connection conn = null;
    PreparedStatement checkIDPs = null;
    PreparedStatement checkRFIDPs = null;
    PreparedStatement checkNamePs = null;
    PreparedStatement updatePs = null;

    try {
        conn = DB.getConnection();

        // Check if student with the same studentID already exists
        checkIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE studentID = ?");
        checkIDPs.setString(1, newStudentID);
        ResultSet checkIDRs = checkIDPs.executeQuery();
        checkIDRs.next();
        int existingStudentIDCount = checkIDRs.getInt(1);

        if (existingStudentIDCount > 0) {
            return status="StudentID Already Exist"; // Student with the same ID already exists
        }

            // Check if student with the same studentID already exists
     checkRFIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE rfid= ?");
      checkRFIDPs.setString(1, rfid);
      ResultSet checkRFIDRs = checkRFIDPs.executeQuery();
      checkRFIDRs.next();
      int existingRFIDCount = checkRFIDRs.getInt(1);

      if (existingRFIDCount > 0) {
      //  JOptionPane.showMessageDialog(null, "Student with RFID " + rfid + " already exists.");

      }

        // Check if student with the same name and extension already exists
        checkNamePs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE firstName = ? AND middleName = ? AND lastName = ? AND extension = ?");
        checkNamePs.setString(1, firstName);
        checkNamePs.setString(2, middleName);
        checkNamePs.setString(3, lastName);
        checkNamePs.setString(4, extension);
        ResultSet checkNameRs = checkNamePs.executeQuery();
        checkNameRs.next();
        int existingStudentNameCount = checkNameRs.getInt(1);

        if (existingStudentNameCount > 0) {
            JOptionPane.showMessageDialog(null, "Student with the same name and extension already exists.");
               return status="Student Already Exist"; // Student with the same name and extension already exists
        }

        // Insert the new student record along with subjects
       updatePs = conn.prepareStatement(
    "UPDATE your_table_name SET newStudentID=?, firstName=?, middleName=?, lastName=?, extension=?, email=?, mobileNo=?, qr=?,  rfid=?, img=?, yrLvl=? WHERE oldStudentID=?");

updatePs.setString(1, newStudentID);
updatePs.setString(2, firstName);
updatePs.setString(3, middleName);
updatePs.setString(4, lastName);
updatePs.setString(5, extension);
updatePs.setString(6, email);
updatePs.setString(7, mobileNo);
updatePs.setBytes(8, qr);

updatePs.setString(9, rfid);
updatePs.setBytes(10, img);
updatePs.setString(11, yrLvl);
updatePs.setString(12, oldStudentID);

 int execute=updatePs.executeUpdate();

 if (execute==1){
    return status="Student Updated Successfuly!";}
 
    } catch (SQLException e) {
        e.printStackTrace(); // Log the exception for debugging purposes
        JOptionPane.showMessageDialog(null, "An error occurred while saving the student information.");
    } finally {
        try {
            // Close resources in a finally block to ensure they're always closed
            if (checkIDPs != null) {
                checkIDPs.close();
            }
            if (checkRFIDPs != null) {
                checkRFIDPs.close();
            }
            if (checkNamePs != null) {
                checkNamePs.close();
            }
            if (updatePs != null) {
                updatePs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception for debugging purposes
        }
    }
    return status;
} 
}
