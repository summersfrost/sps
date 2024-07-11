/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formatting;

import java.util.regex.Pattern;
import javax.swing.JTextField;

/**
 *
 * @author User
 */
public class userValidation {
    
public static String getValidID(JTextField id,JTextField fname,JTextField mName, JTextField lName ){

   String studentID = id.getText();
   boolean IDValidation= Pattern.matches("^\\d{2}-\\d{4}-\\d{3}$", studentID);
   if(IDValidation){
       return studentID;
   }
return "";
}


}
