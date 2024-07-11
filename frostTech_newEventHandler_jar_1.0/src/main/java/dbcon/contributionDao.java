/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class contributionDao {
  public static int save(String eventCode, String eventName, String stringFines) {
    int status = 0;
    try {
      Connection conn = DB.getConnection();

      // Check for overlapping time slots

   

          BigDecimal fines = new BigDecimal(stringFines);
      PreparedStatement insertPs = conn.prepareStatement(
        "INSERT INTO contribution (contributionCode, contributionName, amount) VALUES (?, ?, ?)");
      insertPs.setString(1, eventCode);
      insertPs.setString(2, eventName);
      insertPs.setString(3, stringFines);

      status = insertPs.executeUpdate();

      conn.close();
    } catch (SQLException e) {
          String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
    }
    return status;
  }

public static int update(String eventCode, String eventName, String eventDate, String timeIn, String timeOut, String stringFines, String newEventCode) {
    int status = 0;
    Connection conn = null;
    PreparedStatement updatePs = null;

    try {
        conn = DB.getConnection();

        // Check if the new eventCode is different from the existing eventCode
        if (!eventCode.equals(newEventCode)) {
            PreparedStatement codeCheckPs = conn.prepareStatement("SELECT COUNT(*) FROM events WHERE eventCode = ?");
            codeCheckPs.setString(1, newEventCode);
            ResultSet codeCheckResult = codeCheckPs.executeQuery();

            codeCheckResult.next();
            int codeCount = codeCheckResult.getInt(1);

            if (codeCount > 0) {
                // The new eventCode already exists, handle accordingly (e.g., return an error code)
                JOptionPane.showMessageDialog(null, "New Event Code already exists");
                return status; // Return a specific error code to indicate duplicate new eventCode
            }
        }

        BigDecimal fines = new BigDecimal(stringFines);
        updatePs = conn.prepareStatement(
                "UPDATE events SET eventCode = ?, eventName = ?, eventDate = ?, timeIn = ?, timeOut = ?, fines = ? WHERE eventCode = ?");
        updatePs.setString(1, newEventCode); // Update the eventCode to the new value
        updatePs.setString(2, eventName);
        updatePs.setString(3, eventDate);
        updatePs.setString(4, timeIn);
        updatePs.setString(5, timeOut);
        updatePs.setBigDecimal(6, fines);
        updatePs.setString(7, eventCode); // Specify the old eventCode for the WHERE clause

        status = updatePs.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace(); // Print the complete error stack trace for debugging
        JOptionPane.showMessageDialog(null, "An error occurred while updating the event.");
    } finally {
        // Close resources in a finally block to ensure they are always closed.
        try {
            if (updatePs != null) {
                updatePs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return status;
}
public static int updateContributionCollection(String contributionCode) {
    int status = 0;
    Connection conn = null;
    PreparedStatement finePs = null;
    PreparedStatement attendancePs = null;
    PreparedStatement updateFinesPs = null;
    ResultSet fineResultSet = null;
    ResultSet attendanceResultSet = null;

    try {
        conn = DB.getConnection();

        // Retrieve the fine value for the specified event
        String getFineQuery = "SELECT amount FROM contribution WHERE contributionCode = ?";
        finePs = conn.prepareStatement(getFineQuery);
        finePs.setString(1, contributionCode);
        fineResultSet = finePs.executeQuery();

        BigDecimal eventFine = BigDecimal.ZERO;
        Date eventDate = null;
        if (fineResultSet.next()) {
            eventFine = fineResultSet.getBigDecimal("amount");
        }

        fineResultSet.close();
        finePs.close();

        // Now, retrieve the RFIDs from the collection table
        String query = "SELECT rfid FROM collection WHERE contributionCode = ?";
        attendancePs = conn.prepareStatement(query);
        attendancePs.setString(1, contributionCode);
        attendanceResultSet = attendancePs.executeQuery();

        while (attendanceResultSet.next()) {
            String rfid = attendanceResultSet.getString("rfid");

            // Initialize counts for "No attendance" for timein and timeout
            // Check if there's "No attendance" for timein

            // Calculate fines based on the counts and eventFine
            BigDecimal fines = eventFine;

            // Update the fines in the attendance table for this student and event
            updateFinesPs = conn.prepareStatement(
                    "UPDATE collection SET amount = ? WHERE rfid = ? AND contributionCode = ? AND paid IS NULL");
            updateFinesPs.setBigDecimal(1, fines);
            updateFinesPs.setString(2, rfid);
            updateFinesPs.setString(3, contributionCode);

            int updatedRows = updateFinesPs.executeUpdate();

            if (updatedRows > 0) {
                status++;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (updateFinesPs != null) {
                updateFinesPs.close();
            }
            if (attendancePs != null) {
                attendancePs.close();
            }
            if (finePs != null) {
                finePs.close();
            }
            if (attendanceResultSet != null) {
                attendanceResultSet.close();
            }
            if (fineResultSet != null) {
                fineResultSet.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return status;
}

public static int updateContributorList(String contributionCode) {
    int status = 0;
    try {
        Connection conn = DB.getConnection();
        String query = "SELECT contributionName FROM contribution WHERE contributionCode=?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, contributionCode);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Event with the given eventCode and in the past exists
            // Proceed to update attendance for this event

            String query1 = "SELECT studentID, yearlvl, rfid FROM students WHERE studentID NOT IN (SELECT rfid FROM collection WHERE contributionCode = ?)";
            PreparedStatement missingAttendancePs = conn.prepareStatement(query1);
            missingAttendancePs.setString(1, contributionCode);
            ResultSet missingAttendanceResultSet = missingAttendancePs.executeQuery();

            while (missingAttendanceResultSet.next()) {
                String studentID = missingAttendanceResultSet.getString("rfid");
                String yearlvl = missingAttendanceResultSet.getString("yearlvl");

                // Check if a record with the same eventCode and studRFID already exists
                String checkQuery = "SELECT COUNT(*) FROM collection WHERE contributionCode = ? AND rfid = ?";
                PreparedStatement checkAttendancePs = conn.prepareStatement(checkQuery);
                checkAttendancePs.setString(1, contributionCode);
                checkAttendancePs.setString(2, studentID);
                ResultSet checkResultSet = checkAttendancePs.executeQuery();
                checkResultSet.next();
                int existingAttendanceCount = checkResultSet.getInt(1);

                if (existingAttendanceCount == 0) {
                    // Insert a new attendance record since it doesn't exist
                    PreparedStatement updateAttendancePs = conn.prepareStatement(
                        "INSERT INTO collection (contributionCode,rfid,amount) VALUES (?, ?, ?)");
                    updateAttendancePs.setString(1, contributionCode);
                    updateAttendancePs.setString(2, studentID);
                    updateAttendancePs.setBigDecimal(3, new BigDecimal("0.00")); // Set fines to 20.00 for time in and time out

                    int updatedRows = updateAttendancePs.executeUpdate();

                    if (updatedRows > 0) {
                        status++;
                    }
                }

                checkResultSet.close();
                checkAttendancePs.close();
            }

            missingAttendanceResultSet.close();
            missingAttendancePs.close();
        }

        resultSet.close();
        preparedStatement.close();
        conn.close();
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    }
    return status;
}

}