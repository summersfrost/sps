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
public class adminAdd {

  public static boolean validate(String name, String password) {
    boolean status = false;
    try {
      Connection con = DB.getConnection();
      PreparedStatement ps = con.prepareStatement("SELECT * FROM admin WHERE BINARY name = ? AND BINARY password = ?");
      ps.setString(1, name);
      ps.setString(2, password);
      ResultSet rs = ps.executeQuery();
      status = rs.next();
      con.close();
    } catch (Exception e) {
                   String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
    }
    return status;
  }

}