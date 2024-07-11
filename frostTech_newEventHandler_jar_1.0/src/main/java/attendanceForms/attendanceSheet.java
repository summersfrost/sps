/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package attendanceForms;

import tablesForm.*;
import add.addEvent;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dbcon.DB;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
import update.updateStud;
import view.viewStudent;

/**
 *
 * @author User
 */
public class attendanceSheet extends javax.swing.JPanel {

    /**
     * Creates new form overlay
     */
      private TableRowSorter<DefaultTableModel> sorter;
      private void filterTable() {
        String searchText = searchTextField.getText();
        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + searchText); // Case-insensitive search
        sorter.setRowFilter(rowFilter);
    }

    String eventCodeLabel, eventName, eventDate, formattedEventDate, eventCode;
    JpanelLoader jpload = new JpanelLoader();
    String username;
    public attendanceSheet(String eventCode,String username) {
        this.eventCode = eventCode;
        this.username=username;
        initComponents();
               TableCustom.apply(jScrollPane1, TableCustom.TableType.MULTI_LINE);
        displayTable(table,eventCode);
        
            TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                   String studentID = (String) table.getValueAt(row, 1);
                       updateStud updateStudent = new updateStud(studentID);
        jpload.jPanelLoader(body, updateStudent);
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
  
            }
        };
       table.getColumnModel().getColumn(9).setCellRenderer(new TableActionCellRender());
       table.getColumnModel().getColumn(9).setCellEditor(new TableActionCellEditor(event));
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

public void displayTable(JTable table, String eventCode) {
    try {
        Connection con = DB.getConnection();

        // Retrieve the event name and date for the given event code
        String eventNameQuery = "SELECT eventName, eventDate FROM events WHERE eventCode = ?";
        PreparedStatement eventNamePs = con.prepareStatement(eventNameQuery);
        eventNamePs.setString(1, eventCode);
        ResultSet eventNameRs = eventNamePs.executeQuery();

   
        String eventDateStr = "";
        if (eventNameRs.next()) {
            eventName = eventNameRs.getString("eventName");
            eventDateStr = eventNameRs.getString("eventDate");

            try {
                // Parse the eventDate string into a Date object
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date eventDate = inputDateFormat.parse(eventDateStr);

                // Format the Date object into the desired format
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM d, yyyy");
               formattedEventDate = outputDateFormat.format(eventDate);

                // Update UI elements with event details
                System.out.println(formattedEventDate); // For debugging
                // Assuming these are JLabels in your UI
                eventDateLabel.setText(formattedEventDate);
                eventNameLabel.setText(eventName);
                

                // Query attendance data for the event
                String sqlQuery = "SELECT S.studentID, S.yearlvl, " +
                        "CONCAT(S.firstName, ' ', S.middleName, ' ', S.lastName, ' ', S.extension) AS studentName, " +
                        "A.morningTimeIn, A.morningTimeOut, A.noonTimeIn, A.noonTimeOut, A.nightTimeIn, A.nightTimeOut " +
                        "FROM students AS S " +
                        "LEFT JOIN attendance AS A ON S.rfid = A.studRFID AND A.eventCode = ?";
                PreparedStatement ps = con.prepareStatement(sqlQuery);
                ps.setString(1, eventCode);
                ResultSet rs = ps.executeQuery();

                // Create a DefaultTableModel to hold the data
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Student ID");
                model.addColumn("Student Name");
                model.addColumn("Year Level");
                model.addColumn("Morning In");
                model.addColumn("Morning Out");
                model.addColumn("Noon In");
                model.addColumn("Noon Out");
                model.addColumn("Night In");
                model.addColumn("Night Out");
                model.addColumn("Action");

                // Populate the table model with data
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("studentID"));
                    row.add(rs.getString("studentName"));
                    row.add(rs.getString("yearlvl"));
                    row.add(rs.getString("morningTimeIn"));
                    row.add(rs.getString("morningTimeOut"));
                    row.add(rs.getString("noonTimeIn"));
                    row.add(rs.getString("noonTimeOut"));
                    row.add(rs.getString("nightTimeIn"));
                    row.add(rs.getString("nightTimeOut"));
                    model.addRow(row);
                }

                // Set the populated model to the JTable
                table.setModel(model);
                        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
     

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Close ResultSets and Statements properly
        eventNameRs.close();
        eventNamePs.close();
        con.close();

    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    }
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
        jLabel1 = new javax.swing.JLabel();
        eventNameLabel = new javax.swing.JLabel();
        eventDateLabel = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        body.setBackground(new java.awt.Color(0, 204, 204));
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
        jButton1.setText("Save as PDF");
        jButton1.setToolTipText("");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, java.awt.Color.darkGray));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Ebrima", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-attendance-80.png"))); // NOI18N
        jLabel1.setText("ATTENDANCE SHEET");

        eventNameLabel.setFont(new java.awt.Font("Gill Sans MT", 1, 18)); // NOI18N
        eventNameLabel.setForeground(new java.awt.Color(0, 0, 0));
        eventNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eventNameLabel.setText("EVENT NAME:");

        eventDateLabel.setFont(new java.awt.Font("Gill Sans MT", 1, 18)); // NOI18N
        eventDateLabel.setForeground(new java.awt.Color(0, 0, 0));
        eventDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eventDateLabel.setText("EVENT DATE:");

        jButton2.setBackground(new java.awt.Color(255, 51, 153));
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("BACK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(bodyLayout.createSequentialGroup()
                        .addComponent(Search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(eventNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(eventDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eventNameLabel)
                .addGap(18, 18, 18)
                .addComponent(eventDateLabel)
                .addGap(33, 33, 33)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Search)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(7, 7, 7))
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
             // Specify the folder path where you want to save the PDF file
    String folderPath = "C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\pdf\\pdf\\";

    // Ensure that the folder exists; create it if it doesn't
    File folder = new File(folderPath);
    if (!folder.exists()) {
        folder.mkdirs();
    }

    // Specify the output PDF file path
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        String outputPath = folderPath + "attendance- "+ eventCode +"-" + timestamp + ".pdf";


    try {
       System.out.println("In pdftanle"+ eventName + " " + formattedEventDate);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        // Header Content
        // You can add any content you want here, such as images, text, and formatting.
        // Example: Title and logos
        Image compesaLogo = Image.getInstance(getClass().getResource("/icons/compe.png"));
        Image cetLogo = Image.getInstance(getClass().getResource("/icons/cetafa.png"));

        // Set image sizes as needed
        compesaLogo.scaleAbsolute(72, 72);
        cetLogo.scaleAbsolute(72, 72);

        // Create a PdfPTable for the header content
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        float[] headerColumnWidths = {18f, 68f, 16f};
        headerTable.setWidths(headerColumnWidths);

        PdfPCell headerCell1 = new PdfPCell(compesaLogo);
        headerCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell1.setBorderWidth(0);
        headerTable.addCell(headerCell1);

        // Create a PdfPCell for headerCell2
        PdfPCell headerCell2 = new PdfPCell();

        // Create a paragraph for each element you want to center-align
        Paragraph paragraph1 = new Paragraph("Computer Engineering Students Association", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        Paragraph paragraph2 = new Paragraph("UNIVERSITY OF BOHOL", FontFactory.getFont(FontFactory.HELVETICA, 8));
        Paragraph paragraph3 = new Paragraph("www.universityofbohol.edu.ph (038)-411-3484, Fax No. (038) 411-3101", FontFactory.getFont(FontFactory.HELVETICA, 8));
        Paragraph paragraph4 = new Paragraph("COLLEGE OF ENGINEERING, TECHNOLOGY, ARCHITECTURE, AND FINE ARTS", FontFactory.getFont(FontFactory.HELVETICA, 8));

        // Set alignment for each paragraph to center
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        paragraph4.setAlignment(Element.ALIGN_CENTER);

        // Add the paragraphs to headerCell2
        headerCell2.addElement(paragraph1);
        headerCell2.addElement(paragraph2);
        headerCell2.addElement(paragraph3);
        headerCell2.addElement(paragraph4);

        // Set the overall alignment for headerCell2 to center
        headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Set border width
        headerCell2.setBorderWidth(0);

        // Add headerCell2 to the headerTable
        headerTable.addCell(headerCell2);

        PdfPCell headerCell3 = new PdfPCell(cetLogo);
        headerCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell3.setBorderWidth(0);
        headerTable.addCell(headerCell3);

        document.add(headerTable); // Add the header table to the document

        // Add a newline before the table
        document.add(Chunk.NEWLINE);

// Create a paragraph for the event name and set its alignment and font
Paragraph eventNameParagraph = new Paragraph(eventName, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD));
eventNameParagraph.setAlignment(Element.ALIGN_CENTER);
document.add(eventNameParagraph);

// Add a paragraph for the event date and set its alignment and font
Paragraph eventDateParagraph = new Paragraph(formattedEventDate, FontFactory.getFont(FontFactory.HELVETICA, 12));
eventDateParagraph.setAlignment(Element.ALIGN_CENTER);
document.add(eventDateParagraph);

        document.add(Chunk.NEWLINE);

// Create a PDF table
PdfPTable pdfTable = new PdfPTable(table.getColumnCount() - 1);
pdfTable.setWidthPercentage(100); // Table width as a percentage of page width

// Set the font for the table content (smaller font size)
Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 6); // Set the desired font size
Font tableFontHeader = FontFactory.getFont(FontFactory.HELVETICA, 8);
tableFontHeader.setStyle(tableFont.getStyle() | Font.BOLD);
// Add table headers excluding the last column
for (int i = 0; i < table.getColumnCount() - 1; i++) { // Loop until the second-to-last column
    // Make the font bold

    PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), tableFontHeader));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);

    pdfTable.addCell(cell);
}

// Add table data excluding the last column
for (int row = 0; row < table.getRowCount(); row++) {
    for (int column = 0; column < table.getColumnCount() - 1; column++) { // Loop until the second-to-last column
        Object cellValue = table.getValueAt(row, column);
        String cellText = (cellValue != null) ? cellValue.toString() : "";
        PdfPCell cell = new PdfPCell(new Phrase(cellText, tableFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell);
    }
}

// Now 'pdfTable' contains the PDF representation of your JTable excluding the last column

        // Add the PDF table to the document
        document.add(pdfTable);

        // Close the document
        document.close();

        JOptionPane.showMessageDialog(null, "PDF report saved successfully: " + outputPath);

    } catch (DocumentException | IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error creating PDF report: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        eventList cus = new eventList(username);
        jpload.jPanelLoader(this, cus);
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Search;
    private javax.swing.JPanel body;
    private javax.swing.JLabel eventDateLabel;
    private javax.swing.JLabel eventNameLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
