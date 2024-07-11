/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tablesForm;

import add.addEvent;
import attendanceForms.attendanceSheet;
import update.updateEvents;
import view.viewStudent;
import update.updateStud;
import dbcon.DB;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import raven.cell.TableActionCellEditor;
import raven.cell.TableActionCellRender;
import raven.cell.TableActionEvent;
import table.TableCustom;
import update.updateEvent;



/**
 *
 * @author User
 */
public class eventList extends javax.swing.JPanel {

    /**
     * Creates new form overlay
     */
     JpanelLoader jpload = new JpanelLoader();
       private TableRowSorter<DefaultTableModel> sorter;
       String username;
      private void filterTable() {
        String searchText = searchTextField.getText();
        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + searchText); // Case-insensitive search
        sorter.setRowFilter(rowFilter);
    }
    public eventList(String username) {
        initComponents();
       this.username=username;
           TableCustom.apply(jScrollPane1, TableCustom.TableType.MULTI_LINE);
        displayTable(table);
        
              TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                   String code = (String) table.getValueAt(row, 0);
                   
                       updateEvent updateEvent = new updateEvent(username,code);
        jpload.jPanelLoader(body, updateEvent);
            }

            @Override
            public void onDelete(int row) {
                if (table.isEditing()) {
                 table.getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(row);
            }

            @Override
            public void onView(int row) {
                System.out.println("View row : " + row);
                 String eventCode= (String) table.getValueAt(row, 0);
                  attendanceSheet attendance= new attendanceSheet(eventCode,username);
                          jpload.jPanelLoader(body, attendance);
            }
        };
       table.getColumnModel().getColumn(10).setCellRenderer(new TableActionCellRender());
       table.getColumnModel().getColumn(10).setCellEditor(new TableActionCellEditor(event));
       table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            }
        });
  
       
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
    
    public void displayTable(JTable table) {
    try {
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM events ORDER BY eventDate ASC");
        ResultSet rs = ps.executeQuery();

        // Create a DefaultTableModel to hold the data
        DefaultTableModel model = new DefaultTableModel();

        // Add columns to the model based on the table structure
        model.addColumn("Event Code");
        model.addColumn("Event Name");
        model.addColumn("Event Date");
        model.addColumn("Morning In");
        model.addColumn("Morning Out");
        model.addColumn("Noon In");
        model.addColumn("Noon Out");
        model.addColumn("Night In");
        model.addColumn("Night Out");
        model.addColumn("Fines");
        model.addColumn("Action");

        // Add rows to the model with the retrieved data
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("eventCode")); // Use column name "eventCode"
            row.add(rs.getString("eventName")); // Use column name "eventName"
            
            // Format the date if not null
            Date eventDate = rs.getDate("eventDate");
            if (eventDate != null) {
                DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                row.add(dateFormat.format(eventDate));
            } else {
                row.add("No Event"); // Handle case where date is null
            }

            // Add time fields with AM/PM format or "N/A" if null
            row.add(formatTime(rs.getTime("morningIn"))); // Assuming column name is "morningIn"
            row.add(formatTime(rs.getTime("morningOut"))); // Assuming column name is "morningOut"
            row.add(formatTime(rs.getTime("noonIn"))); // Assuming column name is "noonIn"
            row.add(formatTime(rs.getTime("noonOut"))); // Assuming column name is "noonOut"
            row.add(formatTime(rs.getTime("nightIn"))); // Assuming column name is "nightIn"
            row.add(formatTime(rs.getTime("nightOut"))); // Assuming column name is "nightOut"
            row.add(rs.getBigDecimal("fines")); // Assuming column name is "fines"

            model.addRow(row);
        }

        // Set the model to the table
        table.setModel(model);
                       sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        con.close();
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    }
}

// Helper method to format time with AM/PM or return "N/A" if null
private String formatTime(Time time) {
    if (time == null) {
        return "N/A";
    }
    DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    return timeFormat.format(time);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();
        Search = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        body.setBackground(new java.awt.Color(0, 102, 204));
        body.setForeground(new java.awt.Color(51, 102, 255));

        Search.setFont(new java.awt.Font("Eras Demi ITC", 0, 24)); // NOI18N
        Search.setForeground(new java.awt.Color(0, 0, 0));
        Search.setText("Search:");

        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });

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
        jScrollPane1.setViewportView(table);

        jButton1.setBackground(new java.awt.Color(255, 0, 204));
        jButton1.setFont(new java.awt.Font("Ebrima", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add Event");
        jButton1.setToolTipText("");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, java.awt.Color.darkGray));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE))
                    .addGroup(bodyLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(Search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bodyLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Search))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
               addEvent add =new  addEvent("Benj");
        jpload.jPanelLoader(body, add);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Search;
    private javax.swing.JPanel body;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
