/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;


import static initializer.validators.dateSqlFormat;
import static initializer.validators.handleEmptyString;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
 
public class eventDao {
          private static  PreparedStatement checkPs =null;
          private static  PreparedStatement codeCheckPs =null;
     
               private static  PreparedStatement ps=null;
               
               
    public static int save(String eventCode, String eventName, String eventDate, String morningIn, String morningOut, String noonIn, String noonOut, String nightIn, String nightOut,String participants, byte[] img, String stringFines) {
    int status = 0;
    Connection conn = null;

 morningIn = handleEmptyString(morningIn);
morningOut = handleEmptyString(morningOut);
noonIn = handleEmptyString(noonIn);
noonOut = handleEmptyString(noonOut);
nightIn = handleEmptyString(nightIn);
nightOut = handleEmptyString(nightOut);
  // eventDate=dateSqlFormat(eventDate);
   System.out.println("EVent DAo: " + eventDate);
    try {
        conn = DB.getConnection();

        // Check for overlapping time slots
        PreparedStatement checkPs = conn.prepareStatement(
            "SELECT COUNT(*) FROM events WHERE eventDate = ? AND " +
            "((? BETWEEN morningIn AND morningOut) OR (? BETWEEN morningIn AND morningOut) OR " +
            "(? BETWEEN noonIn AND noonOut) OR (? BETWEEN noonIn AND noonOut) OR " +
            "(? BETWEEN nightIn AND nightOut) OR (? BETWEEN nightIn AND nightOut) OR " +
            "(morningIn BETWEEN ? AND ?) OR (morningOut BETWEEN ? AND ?) OR " +
            "(noonIn BETWEEN ? AND ?) OR (noonOut BETWEEN ? AND ?) OR " +
            "(nightIn BETWEEN ? AND ?) OR (nightOut BETWEEN ? AND ?))");

        checkPs.setString(1, eventDate);
        checkPs.setString(2, morningIn);
        checkPs.setString(3, morningOut);
        checkPs.setString(4, noonIn);
        checkPs.setString(5, noonOut);
        checkPs.setString(6, nightIn);
        checkPs.setString(7, nightOut);
        checkPs.setString(8, morningIn);
        checkPs.setString(9, morningOut);
        checkPs.setString(10, noonIn);
        checkPs.setString(11, noonOut);
        checkPs.setString(12, nightIn);
        checkPs.setString(13, nightOut);
        checkPs.setString(14, morningIn);
        checkPs.setString(15, morningOut);
        checkPs.setString(16, noonIn);
        checkPs.setString(17, noonOut);
        checkPs.setString(18, nightIn);
        checkPs.setString(19, nightOut);

        ResultSet resultSet = checkPs.executeQuery();
        resultSet.next();
        int overlappingEvents = resultSet.getInt(1);

        if (overlappingEvents > 0) {
            // There are overlapping events, handle accordingly (e.g., return an error code)
            JOptionPane.showMessageDialog(null, "Time Conflict for events");
            return status; // Return a specific error code to indicate overlapping events
        }

        codeCheckPs = conn.prepareStatement("SELECT COUNT(*) FROM events WHERE eventCode = ?");
        codeCheckPs.setString(1, eventCode);
        ResultSet codeCheckResult = codeCheckPs.executeQuery();
        codeCheckResult.next();
        int codeCount = codeCheckResult.getInt(1);

        if (codeCount > 0) {
            // The same eventCode already exists, handle accordingly (e.g., return an error code)
            JOptionPane.showMessageDialog(null, "Event Code already exists");
            return status; // Return a specific error code to indicate duplicate eventCode
        }


ps = conn.prepareStatement(
    "INSERT INTO events(eventCode, eventName, eventDate, morningIn, morningOut, noonIn, noonOut, nightIn, nightOut, participants, eventPoster, fines) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

ps.setString(1, eventCode);
ps.setString(2, eventName);
ps.setString(3, eventDate); // Ensure eventDate is in the correct format for your database
ps.setString(4, morningIn);
ps.setString(5, morningOut);
ps.setString(6, noonIn);
ps.setString(7, noonOut);
ps.setString(8, nightIn);
ps.setString(9, nightOut);
ps.setString(10, participants);
ps.setBytes(11, img); // Assuming img is a byte array containing the image data
ps.setString(12, stringFines); // Ensure stringFines matches the column type and content you want to insert

        status = ps.executeUpdate();
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    } finally {
        // Close resources in a finally block to ensure they're always closed
        try {
            if (checkPs != null) {
                checkPs.close();
            }
            if (codeCheckPs != null) {
             codeCheckPs.close();
            }
            if (ps != null) {
             ps.close();
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


    public static int updateEvents(int id,String eventCode, String eventName, String eventDate, String morningIn, String morningOut, String noonIn, String noonOut, String nightIn, String nightOut,String participants, byte[] img, String stringFines) {
    int status = 0;
    Connection conn = null;
    if(morningIn.equals("")){
    morningIn=null;
    }
 morningIn = handleEmptyString(morningIn);
morningOut = handleEmptyString(morningOut);
noonIn = handleEmptyString(noonIn);
noonOut = handleEmptyString(noonOut);
nightIn = handleEmptyString(nightIn);
nightOut = handleEmptyString(nightOut);
  // eventDate=dateSqlFormat(eventDate);
   System.out.println("EVent DAo: " + eventDate);
    try {
        conn = DB.getConnection();

        // Check for overlapping time slots
        PreparedStatement checkPs = conn.prepareStatement(
            "SELECT COUNT(*) FROM events WHERE eventDate = ? AND " +
            "((? BETWEEN morningIn AND morningOut) OR (? BETWEEN morningIn AND morningOut) OR " +
            "(? BETWEEN noonIn AND noonOut) OR (? BETWEEN noonIn AND noonOut) OR " +
            "(? BETWEEN nightIn AND nightOut) OR (? BETWEEN nightIn AND nightOut) OR " +
            "(morningIn BETWEEN ? AND ?) OR (morningOut BETWEEN ? AND ?) OR " +
            "(noonIn BETWEEN ? AND ?) OR (noonOut BETWEEN ? AND ?) OR " +
            "(nightIn BETWEEN ? AND ?) OR (nightOut BETWEEN ? AND ?))");

        checkPs.setString(1, eventDate);
        checkPs.setString(2, morningIn);
        checkPs.setString(3, morningOut);
        checkPs.setString(4, noonIn);
        checkPs.setString(5, noonOut);
        checkPs.setString(6, nightIn);
        checkPs.setString(7, nightOut);
        checkPs.setString(8, morningIn);
        checkPs.setString(9, morningOut);
        checkPs.setString(10, noonIn);
        checkPs.setString(11, noonOut);
        checkPs.setString(12, nightIn);
        checkPs.setString(13, nightOut);
        checkPs.setString(14, morningIn);
        checkPs.setString(15, morningOut);
        checkPs.setString(16, noonIn);
        checkPs.setString(17, noonOut);
        checkPs.setString(18, nightIn);
        checkPs.setString(19, nightOut);

        ResultSet resultSet = checkPs.executeQuery();
        resultSet.next();
        int overlappingEvents = resultSet.getInt(1);

        if (overlappingEvents > 1) {
            // There are overlapping events, handle accordingly (e.g., return an error code)
            JOptionPane.showMessageDialog(null, "Time Conflict for events");
            return status; // Return a specific error code to indicate overlapping events
        }

        codeCheckPs = conn.prepareStatement("SELECT COUNT(*) FROM events WHERE eventCode = ?");
        codeCheckPs.setString(1, eventCode);
        ResultSet codeCheckResult = codeCheckPs.executeQuery();
        codeCheckResult.next();
        int codeCount = codeCheckResult.getInt(1);

        if (codeCount > 1) {
            // The same eventCode already exists, handle accordingly (e.g., return an error code)
            JOptionPane.showMessageDialog(null, "Event Code already exists");
            return status; // Return a specific error code to indicate duplicate eventCode
        }

// Prepare the SQL update statement
ps = conn.prepareStatement(
    "UPDATE events SET eventCode = ?, eventName = ?, eventDate = ?, morningIn = ?, morningOut = ?, noonIn = ?, noonOut = ?, nightIn = ?, nightOut = ?, participants = ?, eventPoster = ?, fines = ? WHERE eventID = ?");

// Set the parameters for the prepared statement
ps.setString(1, eventCode);
ps.setString(2, eventName);
ps.setString(3, eventDate); // Ensure eventDate is in the correct format for your database
ps.setString(4, morningIn);
ps.setString(5, morningOut);
ps.setString(6, noonIn);
ps.setString(7, noonOut);
ps.setString(8, nightIn);
ps.setString(9, nightOut);
ps.setString(10, participants);
ps.setBytes(11, img); // Assuming img is a byte array containing the image data
ps.setString(12, stringFines); // Ensure stringFines matches the column type and content you want to insert
ps.setInt(13, id); // Set the eventID parameter

// Execute the update statement
status = ps.executeUpdate();

    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    } finally {
        // Close resources in a finally block to ensure they're always closed
        try {
            if (checkPs != null) {
                checkPs.close();
            }
            if (codeCheckPs != null) {
             codeCheckPs.close();
            }
            if (ps != null) {
             ps.close();
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