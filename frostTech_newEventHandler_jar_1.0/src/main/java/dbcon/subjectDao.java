package dbcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class subjectDao {
public static int saveSubject(String subjName, String subjCode, String subjSection) {
    int status = 0;
    Connection conn = null;
    try {
        conn = DB.getConnection();

        // Concatenate subjCode and subjSection to create classIdentity
        String classIdentity = subjCode + "-" + subjSection;

        PreparedStatement insertPs = conn.prepareStatement(
            "INSERT INTO subjects (subjName, subjCode, subjSection, classIdentity) VALUES (?, ?, ?, ?)");
        insertPs.setString(1, subjName);
        insertPs.setString(2, subjCode);
        insertPs.setString(3, subjSection);
        insertPs.setString(4, classIdentity);

        status = insertPs.executeUpdate();
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // Handle the exception when closing the connection if needed
        }
    }
    return status;
}
public static int updateSubject(String subjName, String subjCode, String subjSection) {
    int status = 0;
    Connection conn = null;
    try {
        conn = DB.getConnection();

        // Concatenate subjCode and subjSection to create classIdentity
        String classIdentity = subjCode + "-" + subjSection;

        PreparedStatement updatePs = conn.prepareStatement(
            "UPDATE subjects SET subjName = ? WHERE subjCode = ? AND subjSection = ?");
        updatePs.setString(1, subjName);
        updatePs.setString(2, subjCode);
        updatePs.setString(3, subjSection);

        status = updatePs.executeUpdate();
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // Handle the exception when closing the connection if needed
        }
    }
    return status;
}

}
