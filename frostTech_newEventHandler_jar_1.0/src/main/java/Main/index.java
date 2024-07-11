/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Main;

import add.addEvent;
import add.autoAddStudent;
import attendanceForms.checkIn;
import attendanceForms.checkOut;

import attendanceForms.manualCheckin1;
import attendanceForms.manualCheckout;

import backdrop.backdrop1;

import dbcon.DB;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import raven.menu.MenuEvent;
import tablesForm.JpanelLoader;
import tablesForm.eventList;
import tablesForm.studentsList;

/**
 *
 * @author User
 */
public class index extends javax.swing.JFrame {

    /**
     * Creates new form index
     */
    String username;
    JpanelLoader jpload = new JpanelLoader();

    private void setIconImage() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/compe.png")));
    }

    public index(String username) {
        this.username = username;

        initComponents();
        setIconImage();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        menu1.setEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex) {
                if (index == 0) {
                    //    showForm(new backdrop());

                    jpload.jPanelLoader(body, new studentsList(username));

                } else if (index == 1) {

                    jpload.jPanelLoader(body, new eventList(username));

                } else if (index == 2) {
                    try {
                        Connection con = DB.getConnection();

                        // Get today's date as a string in 'YYYY-MM-DD' format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String todayDate = dateFormat.format(new Date());

                        // Prepare an SQL query to check for events happening today
                        String sql = "SELECT eventName, morningIn, morningOut, noonIn, noonOut, nightIn, nightOut FROM events WHERE eventDate = ?";
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, todayDate);

                        // Execute the query
                        ResultSet resultSet = preparedStatement.executeQuery();

                        // Create a list to store the events happening today
                        List<String> eventsList = new ArrayList<>();

                        // Create a map to store event details
                        Map<String, String[]> eventDetails = new HashMap<>();

                        // Collect all the events happening today and their details
                        while (resultSet.next()) {
                            String eventName = resultSet.getString("eventName");
                            String morningIn = resultSet.getString("morningIn");
                            String morningOut = resultSet.getString("morningOut");
                            String noonIn = resultSet.getString("noonIn");
                            String noonOut = resultSet.getString("noonOut");
                            String nightIn = resultSet.getString("nightIn");
                            String nightOut = resultSet.getString("nightOut");
                            eventsList.add(eventName);
                            eventDetails.put(eventName, new String[]{morningIn, morningOut, noonIn, noonOut, nightIn, nightOut});
                        }

                        // Close the database connections
                        resultSet.close();
                        preparedStatement.close();
                        con.close();

                        if (!eventsList.isEmpty()) {
                            // Convert the list of events to an array for JOptionPane
                            String[] eventsArray = eventsList.toArray(new String[0]);

                            // Display the events in a JOptionPane for the user to choose from
                            String selectedEvent = (String) JOptionPane.showInputDialog(
                                    null,
                                    "Select an event happening today:",
                                    "Event Selection",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    eventsArray,
                                    eventsArray[0]
                            );

                            if (selectedEvent != null) {
                                // You can use the 'selectedEvent' variable for further processing
                                System.out.println("You selected: " + selectedEvent);

                                // Get the current time
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                String currentTime = timeFormat.format(new Date());

                                String[] eventTimes = eventDetails.get(selectedEvent);
                                String morningIn = eventTimes[0];
                                String morningOut = eventTimes[1];
                                String noonIn = eventTimes[2];
                                String noonOut = eventTimes[3];
                                String nightIn = eventTimes[4];
                                String nightOut = eventTimes[5];

                                Calendar calCurrentTime = Calendar.getInstance();
                                calCurrentTime.setTime(timeFormat.parse(currentTime));

                                boolean eventFound = false;

                                if (morningIn != null && morningOut != null) {
                                    Calendar calMorningInStart = Calendar.getInstance();
                                    calMorningInStart.setTime(timeFormat.parse(morningIn));

                                    Calendar calMorningInEnd = (Calendar) calMorningInStart.clone();
                                    calMorningInEnd.add(Calendar.MINUTE, 30);

                                    Calendar calMorningOutStart = Calendar.getInstance();
                                    calMorningOutStart.setTime(timeFormat.parse(morningOut));

                                    Calendar calMorningOutEnd = (Calendar) calMorningOutStart.clone();
                                    calMorningOutEnd.add(Calendar.MINUTE, 30);

                                    if (calCurrentTime.equals(calMorningInStart) || (calCurrentTime.after(calMorningInStart) && calCurrentTime.before(calMorningInEnd))) {

                                        checkIn cus = new checkIn(1, selectedEvent, "Benj");
                                        jpload.jPanelLoader(body, cus);
                                        cus.getRfid().requestFocusInWindow();
                                        eventFound = true;
                                    } else if (calCurrentTime.equals(calMorningOutStart) || (calCurrentTime.after(calMorningOutStart) && calCurrentTime.before(calMorningOutEnd))) {

                                        checkOut cus = new checkOut(selectedEvent, "Benj");
                                        jpload.jPanelLoader(body, cus);
                                        cus.getRfid().requestFocusInWindow();
                                        eventFound = true;
                                    }
                                }

                                if (!eventFound && noonIn != null && noonOut != null) {
                                    Calendar calNoonInStart = Calendar.getInstance();
                                    calNoonInStart.setTime(timeFormat.parse(noonIn));

                                    Calendar calNoonInEnd = (Calendar) calNoonInStart.clone();
                                    calNoonInEnd.add(Calendar.MINUTE, 30);

                                    Calendar calNoonOutStart = Calendar.getInstance();
                                    calNoonOutStart.setTime(timeFormat.parse(noonOut));

                                    Calendar calNoonOutEnd = (Calendar) calNoonOutStart.clone();
                                    calNoonOutEnd.add(Calendar.MINUTE, 30);

                                    if (calCurrentTime.equals(calNoonInStart) || (calCurrentTime.after(calNoonInStart) && calCurrentTime.before(calNoonInEnd))) {
                                        checkIn cus = new checkIn(2, selectedEvent, "Benj");
                                        jpload.jPanelLoader(body, cus);
                                        cus.getRfid().requestFocusInWindow();
                                        eventFound = true;
                                    } else if (calCurrentTime.equals(calNoonOutStart) || (calCurrentTime.after(calNoonOutStart) && calCurrentTime.before(calNoonOutEnd))) {
                                        checkOut cus = new checkOut(selectedEvent, "Benj");
                                        jpload.jPanelLoader(body, cus);
                                        cus.getRfid().requestFocusInWindow();
                                        eventFound = true;
                                    }
                                }

                                if (!eventFound && nightIn != null && nightOut != null) {
                                    Calendar calNightInStart = Calendar.getInstance();
                                    calNightInStart.setTime(timeFormat.parse(nightIn));

                                    Calendar calNightInEnd = (Calendar) calNightInStart.clone();
                                    calNightInEnd.add(Calendar.MINUTE, 30);

                                    Calendar calNightOutStart = Calendar.getInstance();
                                    calNightOutStart.setTime(timeFormat.parse(nightOut));

                                    Calendar calNightOutEnd = (Calendar) calNightOutStart.clone();
                                    calNightOutEnd.add(Calendar.MINUTE, 30);

                                    if (calCurrentTime.equals(calNightInStart) || (calCurrentTime.after(calNightInStart) && calCurrentTime.before(calNightInEnd))) {
                                        checkIn cus = new checkIn(3, selectedEvent, "Benj");
                                        jpload.jPanelLoader(body, cus);
                                        cus.getRfid().requestFocusInWindow();
                                        eventFound = true;
                                    } else if (calCurrentTime.equals(calNightOutStart) || (calCurrentTime.after(calNightOutStart) && calCurrentTime.before(calNightOutEnd))) {
                                        checkOut cus = new checkOut(selectedEvent, "Benj");
                                        jpload.jPanelLoader(body, cus);
                                        cus.getRfid().requestFocusInWindow();
                                        eventFound = true;
                                    }
                                }

                                if (!eventFound) {
                                    JOptionPane.showMessageDialog(null, "Attendance Checking for " + selectedEvent + " is currently not Available ");
                                    backdrop1 cus = new backdrop1();
                                    jpload.jPanelLoader(body, cus);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No events Today");
                            backdrop1 cus = new backdrop1();
                            jpload.jPanelLoader(body, cus);
                        }
                    } catch (SQLException | ParseException e) {
                        String error = e.getMessage();
                        JOptionPane.showMessageDialog(index.this, error);
                        // Handle the SQLException or ParseException as needed
                    }

                } else if (index == 3) {
                    try {
                        Connection con = DB.getConnection();

                        // Get today's date as a string in 'YYYY-MM-DD' format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String todayDate = dateFormat.format(new Date());

                        // Prepare an SQL query to check for events happening today
                        String sql = "SELECT eventName, morningIn, morningOut, noonIn, noonOut, nightIn, nightOut FROM events WHERE eventDate = ?";
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, todayDate);

                        // Execute the query
                        ResultSet resultSet = preparedStatement.executeQuery();

                        // Create a list to store the events happening today
                        List<String> eventsList = new ArrayList<>();

                        // Create a map to store event details
                        Map<String, String[]> eventDetails = new HashMap<>();

                        // Collect all the events happening today and their details
                        while (resultSet.next()) {
                            String eventName = resultSet.getString("eventName");
                            String morningIn = resultSet.getString("morningIn");
                            String morningOut = resultSet.getString("morningOut");
                            String noonIn = resultSet.getString("noonIn");
                            String noonOut = resultSet.getString("noonOut");
                            String nightIn = resultSet.getString("nightIn");
                            String nightOut = resultSet.getString("nightOut");
                            eventsList.add(eventName);
                            eventDetails.put(eventName, new String[]{morningIn, morningOut, noonIn, noonOut, nightIn, nightOut});
                        }

                        // Close the database connections
                        resultSet.close();
                        preparedStatement.close();
                        con.close();

                        if (!eventsList.isEmpty()) {
                            // Convert the list of events to an array for JOptionPane
                            String[] eventsArray = eventsList.toArray(new String[0]);

                            // Display the events in a JOptionPane for the user to choose from
                            String selectedEvent = (String) JOptionPane.showInputDialog(
                                    null,
                                    "Select an event happening today:",
                                    "Event Selection",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    eventsArray,
                                    eventsArray[0]
                            );

                            if (selectedEvent != null) {
                                // You can use the 'selectedEvent' variable for further processing
                                System.out.println("You selected: " + selectedEvent);

                                // Get the current time
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                String currentTime = timeFormat.format(new Date());

                                String[] eventTimes = eventDetails.get(selectedEvent);
                                String morningIn = eventTimes[0];
                                String morningOut = eventTimes[1];
                                String noonIn = eventTimes[2];
                                String noonOut = eventTimes[3];
                                String nightIn = eventTimes[4];
                                String nightOut = eventTimes[5];

                                Calendar calCurrentTime = Calendar.getInstance();
                                calCurrentTime.setTime(timeFormat.parse(currentTime));

                                boolean eventFound = false;

                                if (morningIn != null && morningOut != null) {
                                    Calendar calMorningInStart = Calendar.getInstance();
                                    calMorningInStart.setTime(timeFormat.parse(morningIn));

                                    Calendar calMorningInEnd = (Calendar) calMorningInStart.clone();
                                    calMorningInEnd.add(Calendar.MINUTE, 30);

                                    Calendar calMorningOutStart = Calendar.getInstance();
                                    calMorningOutStart.setTime(timeFormat.parse(morningOut));

                                    Calendar calMorningOutEnd = (Calendar) calMorningOutStart.clone();
                                    calMorningOutEnd.add(Calendar.MINUTE, 30);

                                    if (calCurrentTime.equals(calMorningInStart) || (calCurrentTime.after(calMorningInStart) && calCurrentTime.before(calMorningInEnd))) {

                                        manualCheckin1 cus = new manualCheckin1(1, selectedEvent, username);
                                        jpload.jPanelLoader(body, cus);

                                        eventFound = true;
                                    } else if (calCurrentTime.equals(calMorningOutStart) || (calCurrentTime.after(calMorningOutStart) && calCurrentTime.before(calMorningOutEnd))) {

                                             manualCheckout cus = new   manualCheckout(3,selectedEvent, username);
                                        jpload.jPanelLoader(body, cus);
                              
                                        eventFound = true;
                                    }
                                }

                                if (!eventFound && noonIn != null && noonOut != null) {
                                    Calendar calNoonInStart = Calendar.getInstance();
                                    calNoonInStart.setTime(timeFormat.parse(noonIn));

                                    Calendar calNoonInEnd = (Calendar) calNoonInStart.clone();
                                    calNoonInEnd.add(Calendar.MINUTE, 30);

                                    Calendar calNoonOutStart = Calendar.getInstance();
                                    calNoonOutStart.setTime(timeFormat.parse(noonOut));

                                    Calendar calNoonOutEnd = (Calendar) calNoonOutStart.clone();
                                    calNoonOutEnd.add(Calendar.MINUTE, 30);

                                    if (calCurrentTime.equals(calNoonInStart) || (calCurrentTime.after(calNoonInStart) && calCurrentTime.before(calNoonInEnd))) {
                                        manualCheckin1 cus = new manualCheckin1(2, selectedEvent, username);
                                        jpload.jPanelLoader(body, cus);
                                        eventFound = true;
                                    } else if (calCurrentTime.equals(calNoonOutStart) || (calCurrentTime.after(calNoonOutStart) && calCurrentTime.before(calNoonOutEnd))) {
                                           manualCheckout cus = new   manualCheckout(3,selectedEvent, username);
                                        jpload.jPanelLoader(body, cus);
                              
                                        eventFound = true;
                                    }
                                }

                                if (!eventFound && nightIn != null && nightOut != null) {
                                    Calendar calNightInStart = Calendar.getInstance();
                                    calNightInStart.setTime(timeFormat.parse(nightIn));

                                    Calendar calNightInEnd = (Calendar) calNightInStart.clone();
                                    calNightInEnd.add(Calendar.MINUTE, 30);

                                    Calendar calNightOutStart = Calendar.getInstance();
                                    calNightOutStart.setTime(timeFormat.parse(nightOut));

                                    Calendar calNightOutEnd = (Calendar) calNightOutStart.clone();
                                    calNightOutEnd.add(Calendar.MINUTE, 30);

                                    if (calCurrentTime.equals(calNightInStart) || (calCurrentTime.after(calNightInStart) && calCurrentTime.before(calNightInEnd))) {
                                        manualCheckin1 cus = new manualCheckin1(3, selectedEvent, username);
                                        jpload.jPanelLoader(body, cus);
                                        eventFound = true;
                                    } else if (calCurrentTime.equals(calNightOutStart) || (calCurrentTime.after(calNightOutStart) && calCurrentTime.before(calNightOutEnd))) {
                                        manualCheckout cus = new   manualCheckout(3,selectedEvent, username);
                                        jpload.jPanelLoader(body, cus);
                              
                                        eventFound = true;
                                    }
                                }

                                if (!eventFound) {
                                    JOptionPane.showMessageDialog(null, "Attendance Checking for " + selectedEvent + " is currently not Available ");
                                    backdrop1 cus = new backdrop1();
                                    jpload.jPanelLoader(body, cus);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No events Today");
                            backdrop1 cus = new backdrop1();
                            jpload.jPanelLoader(body, cus);
                        }
                    } catch (SQLException | ParseException e) {
                        String error = e.getMessage();
                        JOptionPane.showMessageDialog(index.this, error);
                        // Handle the SQLException or ParseException as needed
                    }

                }
                else if (index==4){
                autoAddStudent cus=new autoAddStudent();
                   jpload.jPanelLoader(body, cus);
                }
            }
        });
    }

    private void showForm(Component com) {
        body.removeAll();
        body.add(com);
        body.repaint();
        body.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        header1 = new component.Header();
        scrollPaneWin111 = new raven.scroll.win11.ScrollPaneWin11();
        menu1 = new raven.menu.Menu();
        body = new javax.swing.JPanel();
        backdrop1 = new backdrop.backdrop();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("COMPESA EVENT HANDLER");
        setMinimumSize(new java.awt.Dimension(1100, 900));
        setPreferredSize(new java.awt.Dimension(1100, 900));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        scrollPaneWin111.setPreferredSize(new java.awt.Dimension(150, 100));
        scrollPaneWin111.setViewportView(menu1);

        body.setLayout(new java.awt.BorderLayout());
        body.add(backdrop1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(scrollPaneWin111, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(header1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneWin111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new index("Benjie S. Juabot").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private backdrop.backdrop backdrop1;
    private javax.swing.JPanel body;
    private component.Header header1;
    private javax.swing.JPanel jPanel1;
    private raven.menu.Menu menu1;
    private raven.scroll.win11.ScrollPaneWin11 scrollPaneWin111;
    // End of variables declaration//GEN-END:variables
}
