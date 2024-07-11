/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package initializer;

import add.addEvent;
import formatting.inputFormatting;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class validators {
    // Define a function for input validation

    public static boolean validateInputs(String studentID, String firstName, String middleName, String lastName, String email, String mobileNo, byte[] imageData, JPanel pane) {
        // Validate student ID
        boolean isStudentIDValid = Pattern.matches("^\\d{2}-\\d{4}-\\d{3}$", studentID);

        // Capitalize first letter of each word in firstName, middleName, and lastName
        inputFormatting nameFormat = new inputFormatting();
        firstName = nameFormat.capitalizeWords(firstName);
        lastName = nameFormat.capitalizeWords(lastName);
        if (!middleName.isEmpty()) {
            middleName = nameFormat.capitalizeWords(middleName);
        }

        // Validate name format
        boolean isNameValid = Pattern.matches("^[a-zA-ZñÑ\\s-]+$", firstName)
                && Pattern.matches("^[a-zA-ZñÑ\\s-]*$", middleName)
                && Pattern.matches("^[a-zA-ZñÑ\\s-]+$", lastName);

        // Validate email format
        boolean isEmailValid = Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email);

        // Validate mobile number format
        boolean isMobileNoValid = mobileNo.matches("\\d{11}");

        // Validate image data
        boolean isImageValid = imageData != null;

        // Display error message if any validation fails
        if (!isStudentIDValid || !isNameValid || !isEmailValid || !isMobileNoValid || !isImageValid) {
            String errorMessage = "Invalid input! Please check your entries.\n\n";

            if (!isStudentIDValid) {
                errorMessage += "- Student ID must be in the format XX-XXXX-XXX.\n";
            }

            if (!isNameValid) {
                errorMessage += "- Invalid name format. Only letters, hyphens, and spaces are allowed.\n";
            }
            if (!isEmailValid) {
                errorMessage += "- Invalid email address.\n";
            }
            if (!isMobileNoValid) {
                errorMessage += "Mobile no be an 11 digit number and starts with 09\n";
            }
            if (!isImageValid) {
                errorMessage += "Insert an Image File\n";
            }
            JOptionPane.showMessageDialog(pane, errorMessage);

            return true; // Validation failed
        }

        return true; // Validation successful
    }

//event validator
    public static String validateEventInputs(String eventCode, String eventName, String eventDate,
            String morningIn, String morningOut, String noonIn,
            String noonOut, String nightIn, String nightOut, String participants,
            byte[] img, String stringFines) {

        String errorMessage = "";

        // Check all String fields are not null or empty
        if (isNullOrEmpty(eventCode) || isNullOrEmpty(eventName) || isNullOrEmpty(eventDate)
                || isNullOrEmpty(stringFines) || isNullOrEmpty(participants)) {

            errorMessage += "Invalid input! Please check your entries.\n";
            
        }
        if( isNullOrEmpty(morningIn) && isNullOrEmpty(morningOut) && isNullOrEmpty(noonIn)
                && isNullOrEmpty(noonOut) && isNullOrEmpty(nightIn) && isNullOrEmpty(nightOut)){
        errorMessage+="Event must have atleast one and time in and time out\n";
        }
        
           if( (!isNullOrEmpty(morningIn) && isNullOrEmpty(morningOut)) || (isNullOrEmpty(morningIn) && !isNullOrEmpty(morningOut))  ){
        errorMessage+="Attendance for Morning must have both Time In and Time Out\n";
        }
        if( (!isNullOrEmpty(noonIn) && isNullOrEmpty(noonOut)) || (isNullOrEmpty(noonIn) && !isNullOrEmpty(noonOut))  ){
        errorMessage+="Attendance for Afternoon must have both Time In and Time Out\n";
        }
        if( (!isNullOrEmpty(nightIn) && isNullOrEmpty(nightOut)) || (isNullOrEmpty(nightIn) && !isNullOrEmpty(nightOut))  ){
        errorMessage+="Attendance for Night must have both Time In and Time Out\n";
        }
        // Check image
        if (img == null || img.length == 0) {
            errorMessage += "Insert an Image File\n";
            
        }

        // All validations passed
        return errorMessage;
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String timeValidator(String time) {
        if(!time.equals("")){
        String regex12Hour = "^(1[0-2]|0?[1-9]):([0-5][0-9]) ([AP][M])$";
        String regex24Hour = "^([01]?[0-9]|2[0-3]):([0-5][0-9])(:([0-5][0-9]))?$";

        Pattern pattern12Hour = Pattern.compile(regex12Hour);
        Pattern pattern24Hour = Pattern.compile(regex24Hour);

        Matcher matcher12Hour = pattern12Hour.matcher(time);
        Matcher matcher24Hour = pattern24Hour.matcher(time);

        String timeToStore;

        if (matcher12Hour.matches()) {
            // Step 1: Parse the time string into a LocalTime object
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime localTime = LocalTime.parse(time, inputFormatter);

            // Step 2: Convert the LocalTime object to a 24-hour format string
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm");
            timeToStore = localTime.format(outputFormatter);

        } else if (matcher24Hour.matches()) {
            // If the input is already in 24-hour format, do nothing
            timeToStore = time;
        } else {
            // Handle invalid time format if necessary
            throw new IllegalArgumentException("Invalid time format: " + time);
        }
        return timeToStore;
        }
        return "";
    }

    public static String dateValidator(String eDate, String mIn, String mOut,String aIn,String aOut,String nIn,String nOut) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String errorMessage = "";

        try {
           
            errorMessage += checkAvailability(mIn, mOut,aIn,aOut,nIn,nOut);

            Date selectedDate = dateFormat.parse(eDate);
            // Remove the time component from selectedDate
            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.setTime(selectedDate);
            selectedDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
            selectedDateCalendar.set(Calendar.MINUTE, 0);
            selectedDateCalendar.set(Calendar.SECOND, 0);
            selectedDateCalendar.set(Calendar.MILLISECOND, 0);
            selectedDate = selectedDateCalendar.getTime();
 System.out.println("validator e date" + selectedDate);
            // Remove the time component from currentDate
            Calendar currentDateCalendar = Calendar.getInstance();
            currentDateCalendar.setTime(currentDate);
            currentDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
            currentDateCalendar.set(Calendar.MINUTE, 0);
            currentDateCalendar.set(Calendar.SECOND, 0);
            currentDateCalendar.set(Calendar.MILLISECOND, 0);
            currentDate = currentDateCalendar.getTime();
 System.out.println("Validator " + currentDate);
            if (selectedDate.before(currentDate)) {
                errorMessage += "Event date must be today or in the future.\n";
            }else{
            System.out.println("Validator class: Valid Daate ");}
            

            return errorMessage;
        } catch (ParseException e) {
            return "Invalid date or time format.";
        }
    }
    
    
public static String dateSqlFormat(String eDate) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    try {
        Date selectedDate = dateFormat.parse(eDate);
        // Convert the Date object back to a string in the same format
        return dateFormat.format(selectedDate);
    } catch (ParseException e) {
        return "Invalid date or time format.";
    }
}

    
    public static String checkAvailability(String mIn, String mOut, String aIn, String aOut, String nIn, String nOut) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String errorMessage = "";
        long differenceInMillis;
        long differenceInMinutes;
        try {
            if (!mIn.isEmpty() && !mOut.isEmpty()) {
                Date mornIn = timeFormat.parse(mIn);
                Date mornOut = timeFormat.parse(mOut);

                boolean morningIn = isTimeWithinRange(mIn, "05:00", "11:59");
                boolean morningOut = isTimeWithinRange(mOut, "05:00", "11:59");

                if (!morningIn) {
                    errorMessage += "Morning Time In must be between 5:00 AM to 11:30 AM\n";
                }
                if (!morningOut) {
                    errorMessage += "Morning Time Out must be between 5:00 AM to 11:30 AM\n";
                }
                if (morningIn && morningOut) {
                    if (mornIn.equals(mornOut)) {
                        errorMessage += "Morning Time In cannot be equal to Morning Time Out\n";
                    } else if (mornIn.after(mornOut)) {
                        errorMessage += "Morning Time In must be before Morning Time Out\n";
                    } else {
                        differenceInMillis = mornOut.getTime() - mornIn.getTime();
                        differenceInMinutes = differenceInMillis / (1000 * 60); // convert milliseconds to minutes

                        if (differenceInMinutes < 30) {
                            errorMessage += "Morning Time In must have a 30-minute allowance before Morning Time Out\n";
                        }
                    }
                }
            }
            if (!aIn.isEmpty() && !aOut.isEmpty()) {
                Date noonIn = timeFormat.parse(aIn);
                Date noonOut = timeFormat.parse(aOut);

                boolean afterNoonIn = isTimeWithinRange(aIn, "12:00", "17:30");
                boolean afterNoonOut = isTimeWithinRange(aOut, "12:00", "17:30");

                if (!afterNoonIn) {
                    errorMessage += "Afternoon Time In must be between 12:00 PM to 5:30 PM\n";
                }
                if (!afterNoonOut) {
                    errorMessage += "Afternoon Time Out must be between 12:00 PM to 5:30 PM\n";
                }
                if (afterNoonIn && afterNoonOut) {
                    if (noonIn.equals(noonOut)) {
                        errorMessage += "Afternoon Time In cannot be equal to Afternoon Time Out\n";
                    } else if (noonIn.after(noonOut)) {
                        errorMessage += "Afternoon Time In must be before Afternoon Time Out\n";
                    } else {
                        differenceInMillis = noonOut.getTime() - noonIn.getTime();
                        differenceInMinutes = differenceInMillis / (1000 * 60); // convert milliseconds to minutes

                        if (differenceInMinutes < 30) {
                            errorMessage += "Afternoon Time In must have a 30-minute allowance before Afternoon Time Out\n";
                        }
                    }
                }
            }

            if (!nIn.isEmpty() && !nOut.isEmpty()) {
                Date eveIn = timeFormat.parse(nIn);
                Date eveOut = timeFormat.parse(nOut);

                boolean nightIn = isTimeWithinRange(nIn, "18:00", "23:30");
                boolean nightOut = isTimeWithinRange(nOut, "18:00", "23:30");

                if (!nightIn) {
                    errorMessage += "Evening Time In must be between 6:00 PM to 11:30 PM\n";
                }
                if (!nightOut) {
                    errorMessage += "Evening Time Out must be between 6:00 PM to 11:30 PM\n";
                }
                if (nightIn && nightOut) {
                    if (eveIn.equals(eveOut)) {
                        errorMessage += "Evening Time In cannot be equal to Evening Time Out\n";
                    } else if (eveIn.after(eveOut)) {
                        errorMessage += "Evening Time In must be before Evening Time Out\n";
                    } else {
                        differenceInMillis = eveOut.getTime() - eveIn.getTime();
                        differenceInMinutes = differenceInMillis / (1000 * 60); // convert milliseconds to minutes

                        if (differenceInMinutes < 30) {
                            errorMessage += "Evening Time In must have a 30-minute allowance before Evening Time Out\n";
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("In class Validator: " + errorMessage);
        return errorMessage;
    }

    private static boolean isTimeWithinRange(String time, String start, String end) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date timeDate = timeFormat.parse(time);
        Date startDate = timeFormat.parse(start);
        Date endDate = timeFormat.parse(end);
        return !timeDate.before(startDate) && !timeDate.after(endDate);
    }

   
    public static boolean validFinesFormat(String stringFines) {
        if (!stringFines.matches("^\\d{1,3}(\\.\\d{1,2})?$")) {
            return false;
        }
        return true;
    }
    
     public static String handleEmptyString(String value) {
    return value.equals("") ? null : value;
}
}
