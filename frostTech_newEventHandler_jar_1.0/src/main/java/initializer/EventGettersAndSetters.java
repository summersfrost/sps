package initializer;

import dbcon.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class EventGettersAndSetters {

    // Member variables corresponding to the columns in the "events" table
    private int eventID;
    private String eventCode;
    private String eventName;
    private String eventDate;
    private String morningIn;
    private String morningOut;
    private String noonIn;
    private String noonOut;
    private String nightIn;
    private String nightOut;
    private String participants;
    private byte[] eventPoster;
    private String fines;

    // Constructor
    public EventGettersAndSetters(String eventCode) {
        this.eventCode = eventCode; // Initialize eventCode

        try (Connection con = DB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "SELECT eventID, eventCode, eventName, eventDate, morningIn, morningOut, noonIn, noonOut, nightIn, nightOut, participants, eventPoster, fines FROM events WHERE eventCode = ?")) {

            preparedStatement.setString(1, eventCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    this.eventID = resultSet.getInt("eventID");
                    this.eventCode = resultSet.getString("eventCode");
                    this.eventName = resultSet.getString("eventName");
                    this.eventDate = resultSet.getString("eventDate");
                    this.morningIn = resultSet.getString("morningIn");
                    this.morningOut = resultSet.getString("morningOut");
                    this.noonIn = resultSet.getString("noonIn");
                    this.noonOut = resultSet.getString("noonOut");
                    this.nightIn = resultSet.getString("nightIn");
                    this.nightOut = resultSet.getString("nightOut");
                    this.participants = resultSet.getString("participants");
                    this.eventPoster = resultSet.getBytes("eventPoster");
                   this.fines = Double.toString(resultSet.getDouble("fines"));

                } else {
                    JOptionPane.showMessageDialog(null, "Event not found with Code: " + eventCode);
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            JOptionPane.showMessageDialog(null, error);
        }
    }

    // Getter methods for all member variables
    public int getEventID() {
        return eventID;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getMorningIn() {
        return morningIn;
    }

    public String getMorningOut() {
        return morningOut;
    }

    public String getNoonIn() {
        return noonIn;
    }

    public String getNoonOut() {
        return noonOut;
    }

    public String getNightIn() {
        return nightIn;
    }

    public String getNightOut() {
        return nightOut;
    }

    public String getParticipants() {
        return participants;
    }

    public byte[] getEventPoster() {
        return eventPoster;
    }

    public String getFines() {
        return fines;
    }
}
