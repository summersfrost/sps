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

/**
 *
 * @author User
 */
public class displayOps {
        public static void displayImageStringSource(String imagePath, JLabel imageLabel) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage);
        imageLabel.setText("");
        imageLabel.setIcon(imageIcon);
    }
        
            public static void displayImageByteSource(byte[] img, JLabel imageLabel) {
        if (img == null || img.length == 0) {
            System.out.println("Error: Image data is null or empty");
            return;
        }

        try {
            ImageIcon imageIcon = new ImageIcon(img);
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(scaledImageIcon);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("Error displaying image: " + e.getMessage());
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
            Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(scaledImageIcon);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("Error displaying image: " + e.getMessage());
        }

    }
            
     public static byte[] getImage(int id) {
    Connection conn = null;
    PreparedStatement selectPs = null;
    ResultSet rs = null;
    byte[] img = null;

    try {
        conn = DB.getConnection();

        // Prepare the SELECT statement to fetch the image based on the ID
        selectPs = conn.prepareStatement("SELECT logo FROM logo WHERE id = ?");
        selectPs.setInt(1, id);

        // Execute the query
        rs = selectPs.executeQuery();

        if (rs.next()) {
            img = rs.getBytes("logo");
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Log the exception for debugging purposes
        // Handle error appropriately
    } finally {
        // Close all resources
        try {
            if (rs != null) {
                rs.close();
            }
            if (selectPs != null) {
                selectPs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log the exception for debugging purposes
        }
    }

    return img;
}
       
}

