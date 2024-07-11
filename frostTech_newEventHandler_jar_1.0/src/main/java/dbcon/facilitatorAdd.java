/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Frost
 */
public class facilitatorAdd {

  public static int save(String name, String password, String email, String address, String city, String contact) {
    int status = 0;
    try {
      Connection con = DB.getConnection();

      // Check if the name already exists in the table
      PreparedStatement checkStmt = con.prepareStatement("SELECT COUNT(*) FROM facilitator WHERE name = ?");
      checkStmt.setString(1, name);
      ResultSet resultSet = checkStmt.executeQuery();
      resultSet.next();
      int count = resultSet.getInt(1);
      if (count > 0) {
        JOptionPane.showMessageDialog(null, "Name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for duplicate name
      }

      PreparedStatement ps = con.prepareStatement("INSERT INTO facilitator(name, password, email, address, city, contact) VALUES (?, ?, ?, ?, ?, ?)");

      // Validate name (characters and spaces only)
      if (!name.matches("^[a-zA-Z ]+$")) {
        JOptionPane.showMessageDialog(null, "Name must contain only characters and spaces.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for invalid name
      }
      // Capitalize each word in the name
      String capitalizedName = capitalizeEachWord(name);
      ps.setString(1, capitalizedName);

      // Validate password (uppercase, lowercase, numbers, special characters, minimum length of 8)
      if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
        JOptionPane.showMessageDialog(null, "Password must have at least 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character, and be at least 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for invalid password
      }
      ps.setString(2, password);

      // Validate email address
      if (!isValidEmail(email)) {
        JOptionPane.showMessageDialog(null, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for invalid email address
      }
      ps.setString(3, email);

      // Validate contact number (11 digits)
      if (!contact.matches("\\d{11}")) {
        JOptionPane.showMessageDialog(null, "Contact number must be an 11-digit number.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for invalid contact number
      }
      ps.setString(6, contact);

      // Capitalize each word in the address
      String capitalizedAddress = capitalizeEachWord(address);
      ps.setString(4, capitalizedAddress);

      // Capitalize each word in the city
      String capitalizedCity = capitalizeEachWord(city);
      ps.setString(5, capitalizedCity);

      // Set the remaining parameters

      status = ps.executeUpdate();
      con.close();
    } catch (Exception e) {
    
       
    }
    return status;
  }

  // Helper method to capitalize each word in a string
  private static String capitalizeEachWord(String str) {
    String[] words = str.split("\\s");
    StringBuilder result = new StringBuilder();
    for (String word: words) {
      if (word.length() > 0) {
        String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        result.append(capitalizedWord).append(" ");
      }
    }
    return result.toString().trim();
  }

  // Helper method to validate email address
  private static boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    return email.matches(emailRegex);
  }

  public static int update(int id, String name, String password, String email, String address, String city, String contact) {
    int status = 0;
    try {
      Connection con = DB.getConnection();

      // Check if the name already exists in the table excluding the current record
      PreparedStatement checkStmt = con.prepareStatement("SELECT COUNT(*) FROM facilitator WHERE name = ? AND id <> ?");
      checkStmt.setString(1, name);
      checkStmt.setInt(2, id);
      ResultSet resultSet = checkStmt.executeQuery();
      resultSet.next();
      int count = resultSet.getInt(1);
      if (count > 0) {
        JOptionPane.showMessageDialog(null, "Name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for duplicate name
      }

      PreparedStatement ps = con.prepareStatement("UPDATE facilitator SET name=?, password=?, email=?, address=?, city=?, contact=? WHERE id=?");

      // Validate name (characters and spaces only)
      if (!name.matches("^[a-zA-Z ]+$")) {
        JOptionPane.showMessageDialog(null, "Name must contain only characters and spaces.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for an invalid name
      }
      // Capitalize each word in the name
      String capitalizedName = capitalizeEachWord(name);
      ps.setString(1, capitalizedName);

      // Validate password (uppercase, lowercase, numbers, special characters, minimum length of 8)
      if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
        JOptionPane.showMessageDialog(null, "Password must have at least 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character, and be at least 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for an invalid password
      }
      ps.setString(2, password);

      // Validate email address
      if (!isValidEmail(email)) {
        JOptionPane.showMessageDialog(null, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for invalid email address
      }
      ps.setString(3, email);

      // Validate contact number (11 digits)
      if (!contact.matches("\\d{11}")) {
        JOptionPane.showMessageDialog(null, "Contact number must be an 11-digit number.", "Error", JOptionPane.ERROR_MESSAGE);
        return status; // Return 0 for an invalid contact number
      }
      ps.setString(6, contact);

      // Capitalize each word in the address
      String capitalizedAddress = capitalizeEachWord(address);
      ps.setString(4, capitalizedAddress);

      // Capitalize each word in the city
      String capitalizedCity = capitalizeEachWord(city);
      ps.setString(5, capitalizedCity);

      // Set the remaining parameters
      ps.setInt(7, id);

      status = ps.executeUpdate();
      con.close();
    } catch (Exception e) {
          String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
    }
    return status;
  }

  public static int delete(int id) {
    int status = 0;
    try {
      Connection con = DB.getConnection();
      PreparedStatement ps = con.prepareStatement("delete from facilitator where id=?");
      ps.setInt(1, id);
      status = ps.executeUpdate();
      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return status;
  }
  public static boolean validate(String name, String password) {
    boolean status = false;
    try {
      Connection con = DB.getConnection();
      PreparedStatement ps = con.prepareStatement("SELECT * FROM facilitator WHERE BINARY name = ? AND BINARY password = ?");
      ps.setString(1, name);
      ps.setString(2, password);
      ResultSet rs = ps.executeQuery();
      status = rs.next();
      con.close();
    } catch (Exception e) {

    }
    return status;
  }

}