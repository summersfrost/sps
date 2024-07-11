/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class finesDao {
 public static int saveFines() {
    int status = 0;
    Connection conn = DB.getConnection();

    try {
        // SQL query to select attendance records with fines not equal to zero
        String selectSQL = "SELECT * FROM attendance WHERE fines <> 0";

        // Prepare the statement
        PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

        // Execute the query
        ResultSet resultSet = preparedStatement.executeQuery();

        // Iterate through the results and insert them into the fines table
        while (resultSet.next()) {
            // Retrieve values from the attendance table
            String eventCode = resultSet.getString("eventCode");
            String studRFID = resultSet.getString("studRFID");
            String fines = resultSet.getString("fines");

            // Check if a record with the same eventCode and studRFID already exists in the fines table
            String checkExistenceSQL = "SELECT COUNT(*) FROM fines WHERE eventCode = ? AND studRFID = ?";
            PreparedStatement checkExistenceStatement = conn.prepareStatement(checkExistenceSQL);
            checkExistenceStatement.setString(1, eventCode);
            checkExistenceStatement.setString(2, studRFID);
            ResultSet existenceResult = checkExistenceStatement.executeQuery();
            
            // Check if a record with the same eventCode and studRFID already exists
            existenceResult.next();
            int count = existenceResult.getInt(1);
            if (count == 0) {
                // Insert the values into the fines table
                String insertSQL = "INSERT INTO fines (eventCode, studRFID, fines) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertSQL);
                insertStatement.setString(1, eventCode);
                insertStatement.setString(2, studRFID);
                insertStatement.setString(3, fines);

                // Execute the insert statement
                insertStatement.executeUpdate();
            }
        }

        // Close the database connection
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return status;
}

}
