/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceForms;

import dbcon.DB;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import dbcon.attendanceDao;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
/**
 *
 * @author RavenPC
 */
public class manualCheckin1 extends javax.swing.JPanel {

    /**
     * Creates new form Panel1
     */

    private TableRowSorter < DefaultTableModel > sorter;
    private DefaultTableModel model;
String fullName, event;
     String username;
        String completeScannedValue ;
     
    public void displayTable() {

      try {
        Connection con = DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement("SELECT * FROM students");
        ResultSet rs = ps.executeQuery();

        // Create a DefaultTableModel to hold the data
        DefaultTableModel model = new DefaultTableModel();

        // Add columns to the model based on the table structure
        model.addColumn("RFID");
        model.addColumn("Student ID");
        model.addColumn("Last Name");
        model.addColumn("Extension");
        model.addColumn("First Name");
        model.addColumn("Middle Name");

        /*   model.addColumn("Added By");
           model.addColumn("Date Added");
           model.addColumn("Time Added");*/
     
        model.addColumn("Time In");


        // Add rows to the model with the retrieved data
        while (rs.next()) {
          Vector < Object > row = new Vector < > ();
          row.add(rs.getString("rfid"));
          row.add(rs.getString("studentID"));
          row.add(rs.getString("lastName"));
          row.add(rs.getString("extension"));
          row.add(rs.getString("firstName"));
          row.add(rs.getString("middleName"));
  

      
          row.add("Time In"); // Use text instead of JButton for initial rendering


          model.addRow(row);
        }

        // Set the model to the table
        table.setModel(model);

        // Create a TableRowSorter and apply it to the table
        sorter = new TableRowSorter < > (model);
        table.setRowSorter(sorter);

        // Sort the table by "Last Name" column in ascending order
        ArrayList < RowSorter.SortKey > sortKeys = new ArrayList < > ();
        sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING)); // 2 represents the index of "Last Name" column
        sorter.setSortKeys(sortKeys);
        table.getColumn("Time In").setCellRenderer(new manualCheckin1.ButtonRenderer());
        table.getColumn("Time In").setCellEditor(new manualCheckin1.ButtonEditor(new JCheckBox()));
 

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(true);

        con.close();
      } catch (SQLException e) {
                       String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
      }
    }

    // Custom cell renderer for JButton class
    class ButtonRenderer extends JButton implements TableCellRenderer {
      public ButtonRenderer() {
        setOpaque(true);
      }

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());

        // Set the button background color based on the column
        if (column == 6) {
          setBackground(Color.PINK); // Set the background color to red for "Delete" button
        } else {
          setBackground(UIManager.getColor("Button.background"));
        }

        return this;
      }
    }

    // Custom cell editor for JButton class
    class ButtonEditor extends DefaultCellEditor {
      protected JButton button;

      public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
            int selectedColumn = table.convertColumnIndexToModel(table.getEditingColumn());

            if (selectedColumn == 6) {
                     completeScannedValue =   (String) table.getModel().getValueAt(selectedRow, 0);
        
           

            try {
              Connection con = DB.getConnection();
              String query = "SELECT studentID, firstName, middleName, lastName, extension, img,yearlvl FROM students WHERE rfid = ?";
              PreparedStatement preparedStatement = con.prepareStatement(query);
              preparedStatement.setString(1, completeScannedValue);

              ResultSet resultSet = preparedStatement.executeQuery();

              if (resultSet.next()) {
              
                String firstName = resultSet.getString("firstName");
                String middleName = resultSet.getString("middleName");
                String lastName = resultSet.getString("lastName");
                String extension = resultSet.getString("extension");
        
                String yr = resultSet.getString("yearlvl");

                fullName = firstName + " " + (middleName != null ? middleName + " " : "") + lastName + " " + extension;

         
                System.out.println("Student Info: " + fullName);
                       System.out.println("Complete Scanned Value: " + completeScannedValue);
                              System.out.println("Username: " + username);
                                     System.out.println("yearlv: " +yr);
                                     System.out.println(event);
                                     
                int rowsAffected = attendanceDao.save(completeScannedValue, username, event,yr);

                if (rowsAffected > 0) {
                  JOptionPane.showMessageDialog(null, fullName + "Time In Successfully", "Time In Status", JOptionPane.INFORMATION_MESSAGE);

                } else {
                 JOptionPane.showMessageDialog(null, fullName+" Student already Time In", "Time In Status", JOptionPane.WARNING_MESSAGE);

                }

              } else {
       
             JOptionPane.showMessageDialog(null, fullName+" Student is Not Registered", "Time In Status", JOptionPane.WARNING_MESSAGE);
              }

              con.close();
            } catch (SQLException ex) {
           String error=ex.getMessage();
          JOptionPane.showMessageDialog(null, error);
            }
            } 

            fireEditingStopped();
          }
        });
      }

      public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
          button.setForeground(table.getSelectionForeground());
          button.setBackground(table.getSelectionBackground());
        } else {
          button.setForeground(table.getForeground());
          button.setBackground(UIManager.getColor("Button.background"));
        }

        button.setText((value == null) ? "" : value.toString());
        return button;
      }

      public Object getCellEditorValue() {
        return button.getText();
      }
    }
   
    public manualCheckin1(int i, String eventName,String user) {
      username = user;
      event=eventName;
      //eventName= this.eventName;
      System.out.println("manualCheckin.java " + eventName);
      initComponents();
      displayTable();

      searchTextField.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
          filterTable();

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
          filterTable();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
          filterTable();
        }
      });
    }
    private void filterTable() {
      String searchText = searchTextField.getText();
      RowFilter < DefaultTableModel, Object > rowFilter = RowFilter.regexFilter("(?i)" + searchText); // Case-insensitive search
      sorter.setRowFilter(rowFilter);
    }
    /*
public void displayImage(String imagePath) {
    if (imagePath != null && !imagePath.isEmpty()) {
        File imageFile = new File(imagePath);

        if (imageFile.exists() && !imageFile.isDirectory()) {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);
            imageLabel.setText("");
            imageLabel.setIcon(imageIcon);
        } else {
            imageLabel.setIcon(null); // Clear any previous image
            imageLabel.setText("No image found");
        }
    } else {
        imageLabel.setIcon(null); // Clear any previous image
        imageLabel.setText("No image found");
    }
}
 */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setBackground(new java.awt.Color(0, 0, 0));
        setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(155, 156, 237));
        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setText("    Students");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Search:");

        searchTextField.setBackground(new java.awt.Color(255, 255, 255));
        searchTextField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        searchTextField.setForeground(new java.awt.Color(0, 0, 0));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 399, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
    /*    
           System.out.println(retrievedpath);
          int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
         
            displayImage(retrievedpath);
            retrievedpath="";
        }*/
    }//GEN-LAST:event_tableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
