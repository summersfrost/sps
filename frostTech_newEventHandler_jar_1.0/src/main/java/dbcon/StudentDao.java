/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class StudentDao {
     private static  int existingStudentIDCount, existingRFIDCount, existingStudentNameCount;
       private static  PreparedStatement checkIDPs = null;
    private static PreparedStatement checkRFIDPs = null;
    private static PreparedStatement checkNamePs = null;
        private static PreparedStatement checkChangesPs = null;
     private static ResultSet checkIDRs = null;
    private static ResultSet checkRFIDRs = null;
   private static ResultSet checkNameRs = null;
public static String save(String studentID, String firstName, String middleName, String lastName, String extension, String email, String mobileNo, byte[] qr, String addedBy, String dateAdded, String timeAdded, String rfid, byte[] img, String yrLvl,String course) {
    String status = "";
    Connection conn = null;

    PreparedStatement insertPs = null;

    
    try {
        conn = DB.getConnection();

        // Check if student with the same studentID already exists
        checkIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE studentID = ?");
        checkIDPs.setString(1, studentID);
       checkIDRs = checkIDPs.executeQuery();
        checkIDRs.next();
        existingStudentIDCount = checkIDRs.getInt(1);

        if (existingStudentIDCount > 0) {
            status += "Student ID already Exist\n";
        }

        // Check if student with the same RFID already exists
        checkRFIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE rfid = ?");
        checkRFIDPs.setString(1, rfid);
       checkRFIDRs = checkRFIDPs.executeQuery();
        checkRFIDRs.next();
        existingRFIDCount = checkRFIDRs.getInt(1);

        if (existingRFIDCount > 0) {
            status += "RFID Already Exist\n";
        }

        // Check if student with the same name and extension already exists
        checkNamePs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE firstName = ? AND middleName = ? AND lastName = ? AND extension = ?");
        checkNamePs.setString(1, firstName);
        checkNamePs.setString(2, middleName);
        checkNamePs.setString(3, lastName);
        checkNamePs.setString(4, extension);
      checkNameRs = checkNamePs.executeQuery();
        checkNameRs.next();
        existingStudentNameCount = checkNameRs.getInt(1);

        if (existingStudentNameCount > 0) {
            status += "Student Name Already Exist\n";
        }

        if (existingStudentNameCount > 0 || existingStudentIDCount > 0 || existingRFIDCount > 0) {
            return status;
        }

        // Insert the new student record along with subjects
        insertPs = conn.prepareStatement(
                "INSERT INTO students(studentID, firstName, middleName, lastName, extension, email, mobileno, qr, addedBy, dateAdded, timeAdded, rfid, img, yearlvl,course) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        insertPs.setString(1, studentID);
        insertPs.setString(2, firstName);
        insertPs.setString(3, middleName);
        insertPs.setString(4, lastName);
        insertPs.setString(5, extension);
        insertPs.setString(6, email);
        insertPs.setString(7, mobileNo);
        insertPs.setBytes(8, qr);
        insertPs.setString(9, addedBy);
        insertPs.setString(10, dateAdded);
        insertPs.setString(11, timeAdded);
        insertPs.setString(12, rfid);
        insertPs.setBytes(13, img);
        insertPs.setString(14, yrLvl);
        insertPs.setString(15, course);

        // Execute the insertion query for students table
        insertPs.executeUpdate();

        // Now, insert into activeStudents table
        insertPs = conn.prepareStatement(
                "INSERT INTO activeStudent(studentID, firstName, middleName, lastName, extension,  qr,  rfid, img, yearlvl) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Set parameters for activeStudents table
        insertPs.setString(1, studentID);
        insertPs.setString(2, firstName);
        insertPs.setString(3, middleName);
        insertPs.setString(4, lastName);
        insertPs.setString(5, extension);

        insertPs.setBytes(6, qr);

        insertPs.setString(7, rfid);
        insertPs.setBytes(8, img);
        insertPs.setString(9, yrLvl);

        // Execute the insertion query for activeStudents table
        insertPs.executeUpdate();

        status = "Student Saved Successfully!";
    } catch (SQLException e) {
        e.printStackTrace(); // Log the exception for debugging purposes
        // Modify this to show a user-friendly error message
        JOptionPane.showMessageDialog(null, "An error occurred while saving the student information.");
    } finally {
        // Close resources in a finally block to ensure they're always closed
        try {
            if (checkIDPs != null) {
                checkIDPs.close();
            }
            if (checkRFIDPs != null) {
                checkRFIDPs.close();
            }
            if (checkNamePs != null) {
                checkNamePs.close();
            }
            if (insertPs != null) {
                insertPs.close();
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

public static String updateStud(String oldStudentID, String newStudentID, String oldFirstName, String newFirstName, String oldMiddleName, String newMiddleName, String oldLastName, String newLastName, String oldExtension, String newExtension,  String email, String mobileNo, byte[] qr, String oldRfid,String newRfid, byte[] img, String yrLvl) {
    String status = "";
    Connection conn = null;

    PreparedStatement updatePs = null;
    PreparedStatement deletePs=null;
    PreparedStatement insertPs=null;
    
    try {
        conn = DB.getConnection();
        
         checkChangesPs = conn.prepareStatement("SELECT * FROM students WHERE studentID = ?");
        checkChangesPs.setString(1, oldStudentID);
        ResultSet rs = checkChangesPs.executeQuery();
        
        if (!rs.next()) {
            // No record found for the old student ID
            status+="Student Not Found\n";
        }
        
        String dbStudentID = rs.getString("studentID");
        String dbFirstName = rs.getString("firstName");
        String dbMiddleName = rs.getString("middleName");
        String dbLastName = rs.getString("lastName");
        String dbExtension = rs.getString("extension");
        String dbEmail = rs.getString("email");
        String dbMobileNo = rs.getString("mobileNo");
        byte[] dbQr = rs.getBytes("qr");
        String dbRfid = rs.getString("rfid");
        byte[] dbImg = rs.getBytes("img");
        String dbYrLvl = rs.getString("yearlvl");

        if (dbStudentID.equals(newStudentID) && dbFirstName.equals(newFirstName) && dbMiddleName.equals(newMiddleName) &&
            dbLastName.equals(newLastName) && dbExtension.equals(newExtension) && dbEmail.equals(email) &&
            dbMobileNo.equals(mobileNo)  && dbRfid.equals(newRfid) &&
             dbYrLvl.equals(yrLvl)) {
            // No changes in student data
            return status="Cannot Update Details. No changes made";
        }

        
            if(!oldStudentID.equals(newStudentID)){
        // Check if student with the same studentID already exists
        checkIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE studentID = ?");
        checkIDPs.setString(1, newStudentID);
        checkIDRs = checkIDPs.executeQuery();
        checkIDRs.next();
       existingStudentIDCount = checkIDRs.getInt(1);

        if (existingStudentIDCount > 0) {
          status += "StudentID Already Exist\n"; // Student with the same ID already exists
        }
            }
            if(!oldFirstName.equals(newFirstName)||!oldMiddleName.equals(newMiddleName)||!oldLastName.equals(newLastName)||!oldExtension.equals(newExtension)){
        // Check if student with the same name and extension already exists
        checkNamePs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE firstName = ? AND middleName = ? AND lastName = ? AND extension = ?");
        checkNamePs.setString(1, newFirstName);
        checkNamePs.setString(2, newMiddleName);
        checkNamePs.setString(3, newLastName);
        checkNamePs.setString(4, newExtension);
        checkNameRs = checkNamePs.executeQuery();
        checkNameRs.next();
       existingStudentNameCount = checkNameRs.getInt(1);

        if (existingStudentNameCount > 0) {
           status += "Student Already Exist\n"; // Student with the same name and extension already exists
        } }
            
            if (!oldRfid.equals(newRfid)){
  // Check if student with the same RFID already exists
        checkRFIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE rfid = ?");
        checkRFIDPs.setString(1, newRfid);
        checkRFIDRs = checkRFIDPs.executeQuery();
        checkRFIDRs.next();
        existingRFIDCount = checkRFIDRs.getInt(1);

        if (existingRFIDCount > 0) {
            status += "RFID Already Exist\n";
        }
            }
        
        if (existingStudentNameCount > 0 || existingStudentIDCount > 0 || existingRFIDCount > 0) {
            return status;
        }

        
        // Insert the new student record along with subjects
        updatePs = conn.prepareStatement(
                "UPDATE students SET studentID=?, firstName=?, middleName=?, lastName=?, extension=?, email=?, mobileNo=?, qr=?, rfid=?, img=?, yearlvl=? WHERE studentID=?");

        updatePs.setString(1, newStudentID);
        updatePs.setString(2, newFirstName);
        updatePs.setString(3, newMiddleName);
        updatePs.setString(4, newLastName);
        updatePs.setString(5, newExtension);
        updatePs.setString(6, email);
        updatePs.setString(7, mobileNo);
        updatePs.setBytes(8, qr);
        updatePs.setString(9, newRfid);
        updatePs.setBytes(10, img);
        updatePs.setString(11, yrLvl);
      
        updatePs.setString(12, oldStudentID);

        int execute = updatePs.executeUpdate();
        if(yrLvl.equals("Graduated")||yrLvl.equals("Shifted")){
        deletePs = conn.prepareStatement( "DELETE FROM activestudent WHERE studentID = ?");
        deletePs.setString(1, oldStudentID);
        deletePs.executeUpdate();}
        else{
                updatePs = conn.prepareStatement(
                "UPDATE activestudent SET studentID=?, firstName=?, middleName=?, lastName=?, extension=?, qr=?, rfid=?, img=?, yearlvl=? WHERE studentID=?");

        updatePs.setString(1, newStudentID);
        updatePs.setString(2, newFirstName);
        updatePs.setString(3, newMiddleName);
        updatePs.setString(4, newLastName);
        updatePs.setString(5, newExtension);

        updatePs.setBytes(6, qr);
        updatePs.setString(7, newRfid);
        updatePs.setBytes(8, img);
        updatePs.setString(9, yrLvl);
      
        updatePs.setString(10, oldStudentID);

        int executeActive = updatePs.executeUpdate();
        
        if(executeActive==0){
                insertPs = conn.prepareStatement(
                "INSERT INTO activeStudent(studentID, firstName, middleName, lastName, extension,  qr,  rfid, img, yearlvl) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Set parameters for activeStudents table
        insertPs.setString(1, newStudentID);
        insertPs.setString(2, newFirstName);
        insertPs.setString(3, newMiddleName);
        insertPs.setString(4, newLastName);
        insertPs.setString(5, newExtension);

        insertPs.setBytes(6, qr);

        insertPs.setString(7, newRfid);
        insertPs.setBytes(8, img);
        insertPs.setString(9, yrLvl);

        // Execute the insertion query for activeStudents table
        insertPs.executeUpdate();
        }
        }
        
        if (execute == 1) {
            status = "Student Updated Successfully!";
        }

    } catch (SQLException e) {
        e.printStackTrace(); // Log the exception for debugging purposes
        JOptionPane.showMessageDialog(null, "An error occurred while saving the student information.");
    } finally {
        try {
            // Close resources in a finally block to ensure they're always closed
            if (checkIDRs != null) {
                checkIDRs.close();
            }
            if (checkRFIDRs != null) {
                checkRFIDRs.close();
            }
            if (checkNameRs != null) {
                checkNameRs.close();
            }
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
            if (checkChangesPs != null){
                checkChangesPs.close();
            }
            if(insertPs != null){
            insertPs.close();}
            if(deletePs!=null){
            deletePs.close();}
            if (conn != null) {
                conn.close();
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception for debugging purposes
        }
    }
    return status;
}




public static String updateStudent(String oldStudentID, String newStudentID, String firstName, String middleName, String lastName, String extension, String email, String mobileNo, byte[] qr, String rfid, byte[] img, String yrLvl) {
    String status=" ";
    Connection conn = null;
    PreparedStatement checkIDPs = null;
    PreparedStatement checkRFIDPs = null;
    PreparedStatement checkNamePs = null;
    PreparedStatement updatePs = null;
    PreparedStatement checkChangesPs = null;

    try {
        conn = DB.getConnection();
        int existingStudentIDCount=0;
        int existingRFIDCount=0;
           int existingStudentNameCount=0;
        
          checkChangesPs = conn.prepareStatement("SELECT * FROM students WHERE studentID = ?");
        checkChangesPs.setString(1, oldStudentID);
        ResultSet rs = checkChangesPs.executeQuery();
        
        if (!rs.next()) {
            // No record found for the old student ID
            status+="Student Not Found\n";
        }
        
        String dbStudentID = rs.getString("studentID");
        String dbFirstName = rs.getString("firstName");
        String dbMiddleName = rs.getString("middleName");
        String dbLastName = rs.getString("lastName");
        String dbExtension = rs.getString("extension");
        String dbEmail = rs.getString("email");
        String dbMobileNo = rs.getString("mobileNo");
        byte[] dbQr = rs.getBytes("qr");
        String dbRfid = rs.getString("rfid");
        byte[] dbImg = rs.getBytes("img");
        String dbYrLvl = rs.getString("yearlvl");

        if (dbStudentID.equals(newStudentID) && dbFirstName.equals(firstName) && dbMiddleName.equals(middleName) &&
            dbLastName.equals(lastName) && dbExtension.equals(extension) && dbEmail.equals(email) &&
            dbMobileNo.equals(mobileNo)  && dbRfid.equals(rfid) &&
             dbYrLvl.equals(yrLvl)) {
            // No changes in student data
            return status="Cannot Update Details. No changes made";
        }

        
        if (!oldStudentID.equals(newStudentID)) {
            // Check if student with the same studentID already exists
            checkIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE studentID = ?");
            checkIDPs.setString(1, newStudentID);
            ResultSet checkIDRs = checkIDPs.executeQuery();
            checkIDRs.next();
             existingStudentIDCount = checkIDRs.getInt(1);
             System.out.println(existingStudentIDCount);
            if (existingStudentIDCount > 0) {
               status +="Student with the same ID already exists.\n"; // Student with the same ID already exists
            }
        } 
        // Check if student with the same RFID already exists
        checkRFIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE rfid = ?");
        checkRFIDPs.setString(1, rfid);
        ResultSet checkRFIDRs = checkRFIDPs.executeQuery();
        checkRFIDRs.next();
        existingRFIDCount = checkRFIDRs.getInt(1);

        if (existingRFIDCount > 1) {
               System.out.println(existingRFIDCount);
      status+= "Student with the same RFID already exist.\n"; // Student with the same RFID already exists
         
        }

        // Check if student with the same name and extension already exists
        checkNamePs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE firstName = ? AND middleName = ? AND lastName = ? AND extension = ?");
        checkNamePs.setString(1, firstName);
        checkNamePs.setString(2, middleName);
        checkNamePs.setString(3, lastName);
        checkNamePs.setString(4, extension);
        ResultSet checkNameRs = checkNamePs.executeQuery();
        checkNameRs.next();
        existingStudentNameCount = checkNameRs.getInt(1);

        if (existingStudentNameCount > 1) {
            System.out.println(existingStudentNameCount);
        status+=  "Student with the same name and extension already exists.\n"; // Student with the same name and extension already exists
        }
        if (existingStudentNameCount > 0 || existingStudentIDCount > 0 || existingRFIDCount > 0) {
            return status;
        }
        // Update the student record
        updatePs = conn.prepareStatement("UPDATE students SET studentID=?, firstName=?, middleName=?, lastName=?, extension=?, email=?, mobileNo=?, qr=?, rfid=?, img=?, yearlvl=? WHERE studentID=?");

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
                updatePs.executeUpdate();
        status = "Student Updated Successfully!";

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
            if (  checkChangesPs!=null){
            checkChangesPs.close();
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

/*
public static int update(String oldStudentID, String newStudentID, String firstName, String middleName, String lastName, String extension, String email, String mobileNo, byte[] qr,   String rfid, byte[] img, String yrLvl) {
    int status = 0;
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
            return status; // Student with the same ID already exists
        }

        // Check if student with the same RFID already exists
        checkRFIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE rfid = ?");
        checkRFIDPs.setString(1, rfid);
        ResultSet checkRFIDRs = checkRFIDPs.executeQuery();
        checkRFIDRs.next();
        int existingRFIDCount = checkRFIDRs.getInt(1);

        if (existingRFIDCount > 0) {
            return status; // Student with the same RFID already exists
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
            return status; // Student with the same name and extension already exists
        }

        // Insert the new student record along with subjects
   updatePs = conn.prepareStatement("UPDATE students SET studentID=?, firstName=?, middleName=?, lastName=?, extension=?, email=?, mobileNo=?, qr=?, rfid=?, img=?, yrLvl=? WHERE studentID=?");


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

status = updatePs.executeUpdate();

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
*/
private static String getFieldValue(Connection conn, String studentID, String fieldName) throws SQLException {
    String value = null;

    PreparedStatement getFieldPs = conn.prepareStatement("SELECT " + fieldName + " FROM students WHERE studentID = ?");
    getFieldPs.setString(1, studentID);
    ResultSet fieldRs = getFieldPs.executeQuery();

    if (fieldRs.next()) {
        value = fieldRs.getString(fieldName);
    }

    fieldRs.close();
    getFieldPs.close();

    return value;
}

private static boolean isFieldEmpty(Connection conn, String studentID, String fieldName) throws SQLException {
    PreparedStatement checkFieldPs = conn.prepareStatement("SELECT " + fieldName + " FROM students WHERE studentID = ?");
    checkFieldPs.setString(1, studentID);
    ResultSet checkFieldRs = checkFieldPs.executeQuery();

    if (checkFieldRs.next()) {
        String fieldValue = checkFieldRs.getString(fieldName);
        return fieldValue == null || fieldValue.isEmpty();
    }

    return false;
}

private static void updateField(Connection conn, String studentID, String fieldName, String newValue) throws SQLException {
    PreparedStatement updateFieldPs = conn.prepareStatement("UPDATE students SET " + fieldName + " = ? WHERE studentID = ?");
    updateFieldPs.setString(1, newValue);
    updateFieldPs.setString(2, studentID);
    updateFieldPs.executeUpdate();
}

public static int updateRFID(String studentID, String rfid) {
    int status = 0;
    Connection conn = null;
    PreparedStatement updatePs = null;

    try {
        conn = DB.getConnection();

        // Check if student with the given studentID exists
        PreparedStatement checkIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE studentID = ?");
        checkIDPs.setString(1, studentID);
        ResultSet checkIDRs = checkIDPs.executeQuery();
        checkIDRs.next();
        int existingStudentIDCount = checkIDRs.getInt(1);

        if (existingStudentIDCount == 0) {
            JOptionPane.showMessageDialog(null, "Student with studentID " + studentID + " does not exist.");
            return status;
        }

        // Update the RFID for the student
        updatePs = conn.prepareStatement("UPDATE students SET rfid = ? WHERE studentID = ?");
        updatePs.setString(1, rfid);
        updatePs.setString(2, studentID);

        status = updatePs.executeUpdate();
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    } finally {
        try {
            if (updatePs != null) {
                updatePs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // Handle the closing of resources
        }
    }
    return status;
}


  public static int delete(String studentID) {
    int status = 0;

    try {
      Connection conn = DB.getConnection();

      // Check if the equipment has a borrowing history
      PreparedStatement checkBorrowingStatement = conn.prepareStatement("SELECT * FROM borrowed_equipment WHERE student_id = ?");
      checkBorrowingStatement.setString(1, studentID);
      ResultSet checkBorrowingResultSet = checkBorrowingStatement.executeQuery();

      if (checkBorrowingResultSet.next()) {
        // Equipment has a borrowing history, display an error message or handle it as per your requirements
        JOptionPane.showMessageDialog(null, "The equipment has a borrowing history and cannot be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        // No borrowing history, proceed with deleting the equipment
        PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM students WHERE studentID = ?");
        deleteStatement.setString(1, studentID);

        status = deleteStatement.executeUpdate();
      }

      conn.close();
    } catch (SQLException e) {
                  String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
    }

    return status;
  }

}